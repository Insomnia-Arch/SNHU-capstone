package org.snhu.cs320.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.snhu.cs320.exception.ContactNotFoundException;
import org.snhu.cs320.repository.ContactRepository;
import org.snhu.cs320.search.ContactSearchIndex;
import org.snhu.cs320.validation.Validation;

public final class ContactService {

    private static final Comparator<Contact> SEARCH_ORDER = Comparator
        .comparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER)
        .thenComparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER)
        .thenComparing(Contact::getId);

    private final ContactSearchIndex searchIndex = new ContactSearchIndex();
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        Validation.validateNotNull(repository, "repository");
        this.repository = repository;
        //adding this for the mysql implimentation later. It allows me to use in-memory storage for now.
        for (Contact contact : repository.findAll()) {
            searchIndex.add(contact);
        }
    }

    public Contact addContact(Contact contact) {
        Validation.validateNotNull(contact, "contact");
        repository.save(contact);

        //adding the contact to the search index
        searchIndex.add(contact);
        return contact;
    }

    public Contact getContact(String id) {
        Validation.validateId(id);
        return repository
            .findById(id)
            .orElseThrow(() -> new ContactNotFoundException(id));
    }

    public List<Contact> getAllContacts() {
        return repository.findAll();
    }

    public Contact updateContact(String id, ContactUpdateRequest request) {
        Validation.validateId(id);
        Validation.validateNotNull(request, "request");

        Contact replacement = new Contact(
            id,
            request.getFirstName(),
            request.getLastName(),
            request.getPhoneNumber(),
            request.getAddress()
        );

        //get the existing contact so we can remove it from the search index before adding the replacement.
        Contact existing = getContact(id);

        repository.replace(replacement);

        //update the search index by removing the existing contact and adding the replacement one.
        searchIndex.remove(existing);
        searchIndex.add(replacement);

        return replacement;
    }

    public void deleteContact(String id) {
        Contact existing = getContact(id);

        //removed Validation.validateId(id); since getContact() already validates the id and throws a more specific exception if it doesn't exist.
        repository.deleteById(id);
        searchIndex.remove(existing);
    }

    public List<Contact> searchByNamePrefix(String prefix) {
        return resolveAndSort(searchIndex.searchByNamePrefix(prefix));
    }

    public List<Contact> searchByPhoneNumber(String phoneNumber) {
        return resolveAndSort(searchIndex.searchByPhoneNumber(phoneNumber));
    }

    private List<Contact> resolveAndSort(Set<String> contactIds) {
        List<Contact> results = new ArrayList<>();
        for (String contactId : contactIds) {
            results.add(getContact(contactId));
        }

        results.sort(SEARCH_ORDER);
        return Collections.unmodifiableList(results);
    }
}
