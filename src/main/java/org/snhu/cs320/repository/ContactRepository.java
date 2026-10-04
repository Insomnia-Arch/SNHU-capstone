package org.snhu.cs320.repository;

import java.util.List;
import java.util.Optional;
import org.snhu.cs320.contact.Contact;

/*
Methods are implemented in InMemoryContactRepository
Save - Insertes a contact, throws DuplicateContactException if the ID already exists
findByID- returns an empty Optional when a valid ID is absent
findAll- returns an unmodifiable copy with no sorting
replace - replaces an existing contact and throws ContactNotFound Exception if absent
deleteById - deletes an existing contact and throws ContactNotFoundException if absent

*/

public interface ContactRepository {
    void save(Contact contact);
    Optional<Contact> findById(String id);
    List<Contact> findAll();

    void replace(Contact contact);
    void deleteById(String id);
    boolean existsById(String id);
}
