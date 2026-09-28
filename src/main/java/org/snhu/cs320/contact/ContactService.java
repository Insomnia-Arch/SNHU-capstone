package org.snhu.cs320.contact;

import java.util.List;
import org.snhu.cs320.exception.ContactNotFoundException;
import org.snhu.cs320.repository.ContactRepository;
import org.snhu.cs320.validation.Validation;

public final class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        Validation.validateNotNull(repository, "repository");
        this.repository = repository;
    }

    public Contact addContact(Contact contact) {
        Validation.validateNotNull(contact, "contact");
        repository.save(contact);
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
        repository.replace(replacement);
        return replacement;
    }

    public void deleteContact(String id) {
        Validation.validateId(id);
        repository.deleteById(id);
    }
}
