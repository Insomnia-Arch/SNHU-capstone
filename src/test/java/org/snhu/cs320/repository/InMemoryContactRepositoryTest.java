package org.snhu.cs320.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.snhu.cs320.contact.Contact;
import org.snhu.cs320.exception.ContactNotFoundException;
import org.snhu.cs320.exception.DuplicateContactException;
import org.snhu.cs320.exception.ValidationException;

class InMemoryContactRepositoryTest {

    private ContactRepository repository;
    private Contact original;
    private Contact replacement;

    @BeforeEach
    void setUp() {
        repository = new InMemoryContactRepository();
        original =
            new Contact(
                "1",
                "firstName",
                "lastName",
                "1234567890",
                "123 Test Lane"
            );
        replacement =
            new Contact(
                "1",
                "update",
                "entry",
                "2345678901",
                "456 Test Success"
            );
    }

    @Test
    void newRepositoryIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
        assertTrue(!repository.findById("1").isPresent());
    }

    @Test
    void savesAndRetrievesContact() {
        repository.save(original);
        assertSame(original, repository.findById("1").orElse(null));
        assertTrue(repository.existsById("1"));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void duplicateIdDoesNotOverwriteOriginal() {
        repository.save(original);
        assertThrowsExactly(
            DuplicateContactException.class,
            () -> repository.save(replacement)
        );

        assertSame(original, repository.findById("1").orElse(null));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void replacesExistingContact() {
        repository.save(original);
        repository.replace(replacement);

        assertSame(replacement, repository.findById("1").orElse(null));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void replacementOfMissingcontactDoesNotInsertIt() {
        assertThrowsExactly(
            ContactNotFoundException.class,
            () -> repository.replace(replacement)
        );
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void deletesExistingContact() {
        repository.save(original);
        repository.deleteById("1");

        assertFalse(repository.existsById("1"));
        assertTrue(repository.findAll().isEmpty());
        assertFalse(repository.findById("1").isPresent());
    }

    @Test
    void missingDeletionPreservesExistingContacts() {
        repository.save(original);
        assertThrowsExactly(
            ContactNotFoundException.class,
            () -> repository.deleteById("missing")
        );

        assertSame(original, repository.findById("1").orElse(null));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void rejectsNullContactsWithoutChangingStorage() {
        repository.save(original);
        assertThrowsExactly(
            ValidationException.class,
            () -> repository.save(null)
        );
        assertThrowsExactly(
            ValidationException.class,
            () -> repository.replace(null)
        );
        assertSame(original, repository.findById("1").orElse(null));
        assertEquals(1, repository.findAll().size());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\t\n", "12345678901" })
    void rejectsInvalidIds(String id) {
        repository.save(original);
        assertThrowsExactly(
            ValidationException.class,
            () -> repository.findById(id)
        );
        assertThrowsExactly(
            ValidationException.class,
            () -> repository.deleteById(id)
        );
        assertThrowsExactly(
            ValidationException.class,
            () -> repository.existsById(id)
        );
        assertSame(original, repository.findById("1").orElse(null));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void retrievesAllContacts() {
        Contact second = new Contact(
            "2",
            "second",
            "entry",
            "3456789012",
            "789 Test Street"
        );
        repository.save(original);
        repository.save(second);

        List<Contact> result = repository.findAll();
        assertTrue(result.contains(original));
        assertTrue(result.contains(second));
        assertEquals(2, repository.findAll().size());
    }

    @Test
    void returnedListCannotBeModified() {
        repository.save(original);
        List<Contact> result = repository.findAll();
        assertThrowsExactly(
            UnsupportedOperationException.class,
            () -> result.clear()
        );
        assertSame(original, repository.findById("1").orElse(null));
    }

    @Test
    void returnedListKeepsItsContentAfterRepositoryChanges() {
        repository.save(original);
        List<Contact> result = repository.findAll();
        repository.replace(replacement);
        assertSame(original, result.get(0));
        assertSame(replacement, repository.findById("1").orElse(null));
        repository.deleteById("1");
        assertEquals(1, result.size());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void separateRepositoriesDoNotShareStorage() {
        ContactRepository otherRepository = new InMemoryContactRepository();
        repository.save(original);
        assertTrue(otherRepository.findAll().isEmpty());

        otherRepository.save(replacement);
        repository.deleteById("1");

        assertTrue(repository.findAll().isEmpty());
        assertSame(replacement, otherRepository.findById("1").orElse(null));
    }
}
