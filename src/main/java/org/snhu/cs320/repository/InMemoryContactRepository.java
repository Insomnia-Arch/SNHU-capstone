package org.snhu.cs320.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.snhu.cs320.contact.Contact;
import org.snhu.cs320.exception.ContactNotFoundException;
import org.snhu.cs320.exception.DuplicateContactException;
import org.snhu.cs320.validation.Validation;

public final class InMemoryContactRepository implements ContactRepository {

    private final ConcurrentMap<String, Contact> contacts =
        new ConcurrentHashMap<>();

    //inserts a new contact and throws a duplicate contact exception if the ID already exists
    @Override
    public void save(Contact contact) {
        Validation.validateNotNull(contact, "contact");
        if (contacts.putIfAbsent(contact.getId(), contact) != null) {
            throw new DuplicateContactException(contact.getId());
        }
    }

    // returns an emptoy optional when an id is valid but absent
    @Override
    public Optional<Contact> findById(String id) {
        Validation.validateId(id);
        return Optional.ofNullable(contacts.get(id));
    }

    //returns the unmodifiable copy
    @Override
    public List<Contact> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(contacts.values()));
    }

    //replaces an existing contact, with a contact not found exception if absent
    @Override
    public void replace(Contact contact) {
        Validation.validateNotNull(contact, "contact");

        if (contacts.replace(contact.getId(), contact) == null) {
            throw new ContactNotFoundException(contact.getId());
        }
    }

    //deletes an existing contact, throws contact not found exception if absent
    @Override
    public void deleteById(String id) {
        Validation.validateId(id);
        if (contacts.remove(id) == null) {
            throw new ContactNotFoundException(id);
        }
    }

    @Override
    public boolean existsById(String id) {
        Validation.validateId(id);
        return contacts.containsKey(id);
    }
}
