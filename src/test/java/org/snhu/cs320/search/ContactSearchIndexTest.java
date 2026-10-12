package org.snhu.cs320.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.snhu.cs320.contact.Contact;

public class ContactSearchIndexTest {

    @Test
    void findsAllContactsSharingPhoneNumber() {
        ContactSearchIndex index = new ContactSearchIndex();

        index.add(
            new Contact("1", "John", "Doe", "8085551234", "1 Main Street")
        );
        index.add(
            new Contact("2", "Alice", "Jones", "8085551234", "2 Main Street")
        );
        index.add(
            new Contact("3", "Bob", "Mortimer", "8085556789", "3 Main Street")
        );

        assertEquals(
            new HashSet<>(Arrays.asList("1", "2")),
            index.searchByPhoneNumber("8085551234")
        );
    }

    @Test
    void changiingPhoneSearchResultDoesNotChangeIndex() {
        ContactSearchIndex index = new ContactSearchIndex();

        index.add(
            new Contact("1", "John", "Doe", "8085551234", "1 Main Street")
        );

        Set<String> results = index.searchByPhoneNumber("8085551234");
        results.clear();

        assertEquals(
            new HashSet<>(Arrays.asList("1")),
            index.searchByPhoneNumber("8085551234")
        );
    }

    @Test
    void removingContactPreservesSharedNameAndPhoneNumber() {
        ContactSearchIndex index = new ContactSearchIndex();

        Contact first = new Contact(
            "1",
            "John",
            "Doe",
            "8085551234",
            "1 Main Street"
        );
        Contact second = new Contact(
            "2",
            "John",
            "smith",
            "8085551234",
            "2 Main Street"
        );

        index.add(first);
        index.add(second);

        index.remove(first);
        //checks for 2nd name still existing and that the original was deleted
        assertEquals(
            new HashSet<>(Arrays.asList("2")),
            index.searchByNamePrefix("john")
        );

        assertEquals(new HashSet<String>(), index.searchByNamePrefix("doe"));

        assertEquals(
            new HashSet<>(Arrays.asList("2")),
            index.searchByPhoneNumber("8085551234")
        );

        index.remove(second);

        //checks that the original was deleted and that the 2nd name was deleted
        assertEquals(new HashSet<String>(), index.searchByNamePrefix("john"));

        assertEquals(
            new HashSet<String>(),
            index.searchByPhoneNumber("8085551234")
        );

        assertEquals(new HashSet<String>(), index.searchByNamePrefix("smith"));
    }
}
