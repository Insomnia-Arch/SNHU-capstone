package org.snhu.cs320.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.snhu.cs320.config.DatabaseConfig;
import org.snhu.cs320.contact.Contact;
import org.snhu.cs320.contact.ContactService;
import org.snhu.cs320.contact.ContactUpdateRequest;
import org.snhu.cs320.exception.ContactNotFoundException;
import org.snhu.cs320.exception.DuplicateContactException;

@EnabledIfEnvironmentVariable(named = "CS499_DB_URL", matches = ".+")
public class MySQLContactRepositoryTest {

    private static final String TEST_ID = "test-001";

    private DataSource dataSource;
    private MySQLContactRepository repository;
    private boolean createdTestContact;

    @BeforeEach
    void setUp() {
        dataSource = DatabaseConfig.createDataSource();
        repository = new MySQLContactRepository(dataSource);

        assertFalse(
            repository.existsById(TEST_ID),
            "Test ID already exists; choose an unused test ID."
        );
    }

    @AfterEach
    void cleanUp() {
        if (createdTestContact && repository.existsById(TEST_ID)) {
            repository.deleteById(TEST_ID);
        }
    }

    private Contact saveTestContact() {
        Contact contact = new Contact(
            TEST_ID,
            "Test",
            "Person",
            "0123456789",
            "123 Test Street"
        );

        repository.save(contact);
        createdTestContact = true;

        return contact;
    }

    @Test
    void savedContactCanBeReadByNewRepository() {
        Contact original = saveTestContact();

        MySQLContactRepository anotherRepository = new MySQLContactRepository(
            dataSource
        );

        Contact retrieved = anotherRepository
            .findById(TEST_ID)
            .orElseThrow(() -> new AssertionError("Saved contact not found."));

        assertEquals(original.getId(), retrieved.getId());
        assertEquals(original.getFirstName(), retrieved.getFirstName());
        assertEquals(original.getLastName(), retrieved.getLastName());
        assertEquals(original.getPhoneNumber(), retrieved.getPhoneNumber());
        assertEquals(original.getAddress(), retrieved.getAddress());
    }

    @Test
    void duplicateIdPreservesOriginalContact() {
        Contact original = saveTestContact();

        Contact duplicate = new Contact(
            TEST_ID,
            "Other",
            "Name",
            "9876543210",
            "456 Other Street"
        );

        assertThrows(
            DuplicateContactException.class,
            () -> repository.save(duplicate)
        );

        Contact stored = repository
            .findById(TEST_ID)
            .orElseThrow(() -> new AssertionError("Original contact not found.")
            );

        assertEquals(original.getId(), stored.getId());
        assertEquals(original.getFirstName(), stored.getFirstName());
        assertEquals(original.getLastName(), stored.getLastName());
        assertEquals(original.getPhoneNumber(), stored.getPhoneNumber());
        assertEquals(original.getAddress(), stored.getAddress());
    }

    @Test
    void replacesEditableFields() {
        saveTestContact();

        Contact replacement = new Contact(
            TEST_ID,
            "Updated",
            "Contact",
            "2222222222",
            "456 Updated Street"
        );

        repository.replace(replacement);

        Contact stored = repository
            .findById(TEST_ID)
            .orElseThrow(() -> new AssertionError("Updated contact not found.")
            );

        assertEquals(TEST_ID, stored.getId());
        assertEquals(replacement.getFirstName(), stored.getFirstName());
        assertEquals(replacement.getLastName(), stored.getLastName());
        assertEquals(replacement.getPhoneNumber(), stored.getPhoneNumber());
        assertEquals(replacement.getAddress(), stored.getAddress());
    }

    @Test
    void replacingWithIdenticalValuesSucceeds() {
        Contact original = saveTestContact();

        repository.replace(original);

        assertTrue(repository.existsById(TEST_ID));
    }

    @Test
    void deletesExistingContact() {
        saveTestContact();

        repository.deleteById(TEST_ID);

        assertFalse(repository.existsById(TEST_ID));
        assertFalse(repository.findById(TEST_ID).isPresent());
    }

    @Test
    void missingContactHasExpectedBehavior() {
        Contact missing = new Contact(
            TEST_ID,
            "Missing",
            "Person",
            "1111111111",
            "123 Test Street"
        );

        assertFalse(repository.findById(TEST_ID).isPresent());

        assertThrows(
            ContactNotFoundException.class,
            () -> repository.replace(missing)
        );

        assertThrows(
            ContactNotFoundException.class,
            () -> repository.deleteById(TEST_ID)
        );

        assertFalse(repository.existsById(TEST_ID));
    }

    @Test
    void returnedListIncludesSavedContactAndCannotBeModified() {
        saveTestContact();

        List<Contact> results = repository.findAll();

        assertTrue(
            results
                .stream()
                .anyMatch(contact -> TEST_ID.equals(contact.getId()))
        );

        assertThrows(
            UnsupportedOperationException.class,
            () -> results.clear()
        );
    }

    @Test
    void earlierListKeepsOriginalContactAfterUpdateAndDeletion() {
        Contact original = saveTestContact();
        List<Contact> earlierResults = repository.findAll();
        int earlierSize = earlierResults.size();

        repository.replace(
            new Contact(
                TEST_ID,
                "Updated",
                "Contact",
                "2222222222",
                "456 Updated Street"
            )
        );

        repository.deleteById(TEST_ID);

        Contact earlierContact = earlierResults
            .stream()
            .filter(contact -> TEST_ID.equals(contact.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Earlier contact not found.")
            );

        assertEquals(earlierSize, earlierResults.size());
        assertEquals(original.getFirstName(), earlierContact.getFirstName());
        assertEquals(original.getLastName(), earlierContact.getLastName());
        assertEquals(
            original.getPhoneNumber(),
            earlierContact.getPhoneNumber()
        );
        assertEquals(original.getAddress(), earlierContact.getAddress());
        assertFalse(repository.existsById(TEST_ID));
    }

    @Test
    void newServiceRebuildsSearchIndexesFromStoredContacts() {
        Contact original = saveTestContact();

        ContactService service = new ContactService(
            new MySQLContactRepository(dataSource)
        );

        assertTrue(
            service
                .searchByNamePrefix("test")
                .stream()
                .anyMatch(contact -> TEST_ID.equals(contact.getId()))
        );
        assertTrue(
            service
                .searchByNamePrefix("person")
                .stream()
                .anyMatch(contact -> TEST_ID.equals(contact.getId()))
        );

        assertTrue(
            service
                .searchByPhoneNumber(original.getPhoneNumber())
                .stream()
                .anyMatch(contact -> TEST_ID.equals(contact.getId()))
        );
    }

    @Test
    void serviceUpdatesAndDeletesDatabaseContactAndSearchEntries() {
        Contact original = saveTestContact();

        ContactService service = new ContactService(
            new MySQLContactRepository(dataSource)
        );

        service.updateContact(
            TEST_ID,
            new ContactUpdateRequest(
                "Updated",
                "Record",
                "2222222222",
                "456 Updated Street"
            )
        );

        Contact stored = repository
            .findById(TEST_ID)
            .orElseThrow(() -> new AssertionError("Updated contact not found.")
            );

        assertEquals("Updated", stored.getFirstName());
        assertEquals("Record", stored.getLastName());
        assertEquals("2222222222", stored.getPhoneNumber());
        assertEquals("456 Updated Street", stored.getAddress());

        assertFalse(includesTestContact(service.searchByNamePrefix("test")));
        assertFalse(includesTestContact(service.searchByNamePrefix("person")));
        assertFalse(
            includesTestContact(
                service.searchByPhoneNumber(original.getPhoneNumber())
            )
        );

        assertTrue(includesTestContact(service.searchByNamePrefix("updated")));
        assertTrue(includesTestContact(service.searchByNamePrefix("record")));
        assertTrue(
            includesTestContact(service.searchByPhoneNumber("2222222222"))
        );

        service.deleteContact(TEST_ID);

        assertFalse(repository.existsById(TEST_ID));
        assertFalse(includesTestContact(service.searchByNamePrefix("updated")));
        assertFalse(includesTestContact(service.searchByNamePrefix("record")));
        assertFalse(
            includesTestContact(service.searchByPhoneNumber("2222222222"))
        );
    }

    private boolean includesTestContact(List<Contact> contacts) {
        return contacts
            .stream()
            .anyMatch(contact -> TEST_ID.equals(contact.getId()));
    }

    @Test
    void storesApostropheAsOrdinaryText() {
        saveTestContact();

        repository.replace(
            new Contact(
                TEST_ID,
                "Test",
                "O'Brien",
                "0123456789",
                "123 Test Street"
            )
        );

        Contact stored = repository
            .findById(TEST_ID)
            .orElseThrow(() -> new AssertionError("Contact not found."));

        assertEquals("O'Brien", stored.getLastName());
    }
}
