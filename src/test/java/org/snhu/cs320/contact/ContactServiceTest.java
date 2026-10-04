package org.snhu.cs320.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.snhu.cs320.exception.ContactNotFoundException;
import org.snhu.cs320.exception.DuplicateContactException;
import org.snhu.cs320.exception.ValidationException;
import org.snhu.cs320.repository.InMemoryContactRepository;

class ContactServiceTest {

    private ContactService service;
    private Contact original;

    @BeforeEach
    void setUp() {
        service = new ContactService(new InMemoryContactRepository());

        original =
            new Contact("1", "First", "Last", "1111111111", "123 Main Street");
    }

    @Test
    void newServiceHasNoContacts() {
        assertThat(service.getAllContacts()).isEmpty();
    }

    @Test
    void addsAndRetrievesContact() {
        Contact added = service.addContact(original);

        assertThat(added).isSameAs(original);
        assertThat(service.getContact("1")).isSameAs(original);
    }

    @Test
    void retrievesAllContacts() {
        Contact second = new Contact(
            "2",
            "Other",
            "Person",
            "2222222222",
            "456 Oak Street"
        );

        service.addContact(original);
        service.addContact(second);

        assertThat(service.getAllContacts())
            .containsExactlyInAnyOrder(original, second);
    }

    @Test
    void updatesEditableFieldsAndPreservesId() {
        service.addContact(original);

        ContactUpdateRequest request = new ContactUpdateRequest(
            "Updated",
            "Person",
            "2222222222",
            "456 Oak Street"
        );

        Contact updated = service.updateContact("1", request);

        assertThat(updated.getId()).isEqualTo("1");
        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Person");
        assertThat(updated.getPhoneNumber()).isEqualTo("2222222222");
        assertThat(updated.getAddress()).isEqualTo("456 Oak Street");

        assertThat(service.getContact("1")).isSameAs(updated);
        assertThat(service.getAllContacts()).containsExactly(updated);
    }

    @Test
    void updatePreservesPreviouslyRetrievedContact() {
        service.addContact(original);
        Contact beforeUpdate = service.getContact("1");

        ContactUpdateRequest request = new ContactUpdateRequest(
            "Updated",
            "Person",
            "2222222222",
            "456 Oak Street"
        );

        Contact updated = service.updateContact("1", request);

        assertThat(updated).isNotSameAs(beforeUpdate);
        assertThat(beforeUpdate.getId()).isEqualTo("1");
        assertThat(beforeUpdate.getFirstName()).isEqualTo("First");
        assertThat(beforeUpdate.getLastName()).isEqualTo("Last");
        assertThat(beforeUpdate.getPhoneNumber()).isEqualTo("1111111111");
        assertThat(beforeUpdate.getAddress()).isEqualTo("123 Main Street");
    }

    @Test
    void deletesOnlyRequestedContact() {
        Contact second = new Contact(
            "2",
            "Other",
            "Person",
            "2222222222",
            "456 Oak Street"
        );

        service.addContact(original);
        service.addContact(second);

        service.deleteContact("1");

        assertThat(service.getAllContacts()).containsExactly(second);
    }

    @Test
    void duplicateIdPreservesOriginalContact() {
        service.addContact(original);

        Contact duplicate = new Contact(
            "1",
            "Other",
            "Person",
            "2222222222",
            "456 Oak Street"
        );

        assertThatThrownBy(() -> service.addContact(duplicate))
            .isExactlyInstanceOf(DuplicateContactException.class);

        assertThat(service.getContact("1")).isSameAs(original);
        assertThat(service.getAllContacts()).containsExactly(original);

        //verify that the rejected duplicate neither removes the original entries or creates entries for the rejected contact.
        assertThat(service.searchByNamePrefix("fir")).containsExactly(original);
        assertThat(service.searchByNamePrefix("las")).containsExactly(original);
        assertThat(service.searchByPhoneNumber("1111111111"))
            .containsExactly(original);

        assertThat(service.searchByNamePrefix("oth")).isEmpty();
        assertThat(service.searchByPhoneNumber("2222222222")).isEmpty();
        assertThat(service.searchByNamePrefix("per")).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(
        nullValues = "NULL",
        value = {
            "NULL, Person, 2222222222, 456 Oak Street, firstName",
            "Updated, '', 2222222222, 456 Oak Street, lastName",
            "Updated, Person, 222222222, 456 Oak Street, phoneNumber",
            "Updated, Person, 2222222222, 1234567890123456789012345678901, address",
        }
    )
    void failedUpdatePreservesEveryField(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        String invalidField
    ) {
        service.addContact(original);

        ContactUpdateRequest request = new ContactUpdateRequest(
            firstName,
            lastName,
            phoneNumber,
            address
        );

        assertThatThrownBy(() -> service.updateContact("1", request))
            .isExactlyInstanceOf(ValidationException.class)
            .hasMessageContaining(invalidField);

        Contact stored = service.getContact("1");

        assertThat(stored).isSameAs(original);
        assertThat(stored.getId()).isEqualTo("1");
        assertThat(stored.getFirstName()).isEqualTo("First");
        assertThat(stored.getLastName()).isEqualTo("Last");
        assertThat(stored.getPhoneNumber()).isEqualTo("1111111111");
        assertThat(stored.getAddress()).isEqualTo("123 Main Street");

        //updated checks for the search
        assertThat(service.searchByNamePrefix("fir")).containsExactly(original);
        assertThat(service.searchByNamePrefix("las")).containsExactly(original);
        assertThat(service.searchByPhoneNumber("1111111111"))
            .containsExactly(original);

        assertThat(service.searchByNamePrefix("upd")).isEmpty();
        assertThat(service.searchByNamePrefix("per")).isEmpty();
        assertThat(service.searchByPhoneNumber("2222222222")).isEmpty();
    }

    @Test
    void rejectsNullArguments() {
        service.addContact(original);

        assertThatThrownBy(() -> new ContactService(null))
            .isExactlyInstanceOf(ValidationException.class);

        assertThatThrownBy(() -> service.addContact(null))
            .isExactlyInstanceOf(ValidationException.class);

        assertThatThrownBy(() -> service.updateContact("1", null))
            .isExactlyInstanceOf(ValidationException.class);

        assertThat(service.getAllContacts()).containsExactly(original);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\t\n", "12345678901" })
    void rejectsInvalidIds(String id) {
        service.addContact(original);

        assertThatThrownBy(() -> service.getContact(id))
            .isExactlyInstanceOf(ValidationException.class);

        assertThatThrownBy(() -> service.updateContact(id, validUpdate()))
            .isExactlyInstanceOf(ValidationException.class);

        assertThatThrownBy(() -> service.deleteContact(id))
            .isExactlyInstanceOf(ValidationException.class);

        assertThat(service.getAllContacts()).containsExactly(original);
    }

    @Test
    void separateServicesDoNotShareState() {
        ContactService otherService = new ContactService(
            new InMemoryContactRepository()
        );

        service.addContact(original);
        assertThat(otherService.getAllContacts()).isEmpty();

        Contact other = new Contact(
            "1",
            "Other",
            "Person",
            "2222222222",
            "456 Oak Street"
        );

        otherService.addContact(other);
        service.deleteContact("1");

        assertThat(service.getAllContacts()).isEmpty();
        assertThat(otherService.getContact("1")).isSameAs(other);
    }

    @Test
    void returnedListIsUnmodifiableAndIndependent() {
        service.addContact(original);
        List<Contact> result = service.getAllContacts();

        assertThatThrownBy(() -> result.clear())
            .isExactlyInstanceOf(UnsupportedOperationException.class);

        Contact updated = service.updateContact("1", validUpdate());

        assertThat(result).containsExactly(original);
        assertThat(service.getAllContacts()).containsExactly(updated);
    }

    private ContactUpdateRequest validUpdate() {
        return new ContactUpdateRequest(
            "Updated",
            "Person",
            "2222222222",
            "456 Oak Street"
        );
    }

    @Test
    void missingContactOperationsPreserveExistingContacts() {
        service.addContact(original);

        assertThatThrownBy(() -> service.getContact("missing"))
            .isExactlyInstanceOf(ContactNotFoundException.class);

        assertThatThrownBy(() -> service.updateContact("missing", validUpdate())
            )
            .isExactlyInstanceOf(ContactNotFoundException.class);

        assertThatThrownBy(() -> service.deleteContact("missing"))
            .isExactlyInstanceOf(ContactNotFoundException.class);

        assertThat(service.getAllContacts()).containsExactly(original);
    }

    @Test
    void searchesReturnContactsSortedbyLastNameFirstNameAndId() {
        service.addContact(
            new Contact("4", "Jo", "Zulu", "1111111111", "4 Main Street")
        );
        service.addContact(
            new Contact("3", "John", "smith", "1111111111", "3 Main Street")
        );
        service.addContact(
            new Contact("2", "john", "Smith", "1111111111", "2 Main Street")
        );
        service.addContact(
            new Contact("1", "Joan", "SMITH", "1111111111", "1 Main Street")
        );

        assertThat(service.searchByNamePrefix("JO"))
            .extracting(Contact::getId)
            .containsExactly("1", "2", "3", "4");

        assertThat(service.searchByPhoneNumber("1111111111"))
            .extracting(Contact::getId)
            .containsExactly("1", "2", "3", "4");
    }

    @Test
    void buildsSearchIndexesFromExistingRepositoryContacts() {
        InMemoryContactRepository repository = new InMemoryContactRepository();

        repository.save(original);

        ContactService loadedService = new ContactService(repository);

        assertThat(loadedService.searchByNamePrefix("fir"))
            .containsExactly(original);
        assertThat(loadedService.searchByNamePrefix("las"))
            .containsExactly(original);
        assertThat(loadedService.searchByPhoneNumber("1111111111"))
            .containsExactly(original);
    }

    @Test
    void updatingContactReplacesOldSearchEntries() {
        service.addContact(original);

        Contact updated = service.updateContact("1", validUpdate());

        assertThat(service.searchByNamePrefix("fir")).isEmpty();
        assertThat(service.searchByNamePrefix("las")).isEmpty();
        assertThat(service.searchByPhoneNumber("1111111111")).isEmpty();

        assertThat(service.searchByNamePrefix("upd")).containsExactly(updated);
        assertThat(service.searchByNamePrefix("per")).containsExactly(updated);
        assertThat(service.searchByPhoneNumber("2222222222"))
            .containsExactly(updated);
    }

    @Test
    void deletingContactPreservesOtherContactSearchEntries() {
        Contact second = new Contact(
            "2",
            "First",
            "Brown",
            "1111111111",
            "456 Oak Street"
        );
        service.addContact(original);
        service.addContact(second);

        service.deleteContact("1");

        assertThat(service.searchByNamePrefix("las")).isEmpty();
        assertThat(service.searchByNamePrefix("fir")).containsExactly(second);
        assertThat(service.searchByNamePrefix("bro")).containsExactly(second);
        assertThat(service.searchByPhoneNumber("1111111111"))
            .containsExactly(second);
    }

    @Test
    void updatingOneOfTwoIdenticalNamesPreservesOtherNameMatch() {
        Contact contact = new Contact(
            "1",
            "John",
            "JOHN",
            "1111111111",
            "123 Main Street"
        );

        service.addContact(contact);
        Contact updated = service.updateContact(
            "1",
            new ContactUpdateRequest(
                "john",
                "Doe",
                "1111111111",
                "123 Main Street"
            )
        );

        assertThat(service.searchByNamePrefix("jo")).containsExactly(updated);
        assertThat(service.searchByNamePrefix("do")).containsExactly(updated);
        assertThat(service.searchByPhoneNumber("1111111111"))
            .containsExactly(updated);
    }

    @Test
    void unmatchedSearchesReturnEmptyLists() {
        service.addContact(original);

        assertThat(service.searchByNamePrefix("zzz")).isEmpty();
        assertThat(service.searchByPhoneNumber("9999999999")).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "\t\n" })
    void rejectsBlankNameSearches(String prefix) {
        assertThatThrownBy(() -> service.searchByNamePrefix(prefix))
            .isExactlyInstanceOf(ValidationException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(
        strings = {
            " ", "123456789", "12345678901", "123456789a", "111-111-1111",
        }
    )
    void rejectsInvalidPhoneSearches(String phoneNumber) {
        assertThatThrownBy(() -> service.searchByPhoneNumber(phoneNumber))
            .isExactlyInstanceOf(ValidationException.class);
    }
}
