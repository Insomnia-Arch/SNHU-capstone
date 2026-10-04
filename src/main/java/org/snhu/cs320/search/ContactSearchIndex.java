package org.snhu.cs320.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.snhu.cs320.contact.Contact;
import org.snhu.cs320.validation.Validation;

public final class ContactSearchIndex {

    private final NameTrie nameTrie = new NameTrie();
    //using both Map<String> and Set<String> because two contacts can have the same phone number. if another is entered then Map<String, String> would overrite the first contacts phone number.

    //source for this idea https://stackoverflow.com/questions/32770176/in-java-how-can-i-map-string-to-a-setstring-if-i-do-not-know-how-many-keys-in because I had to look it up.

    private final Map<String, Set<String>> phoneIndex = new HashMap<>();

    public void add(Contact contact) {
        Validation.validateNotNull(contact, "contact");

        nameTrie.insert(contact.getFirstName(), contact.getId());
        nameTrie.insert(contact.getLastName(), contact.getId());

        //looks for the phone number in the existing set, if there isn't one create and store te empty set and add it to the set.
        phoneIndex
            .computeIfAbsent(contact.getPhoneNumber(), key -> new HashSet<>())
            .add(contact.getId());
    }

    public Set<String> searchByNamePrefix(String prefix) {
        return nameTrie.searchByPrefix(prefix);
    }

    public Set<String> searchByPhoneNumber(String phoneNumber) {
        Validation.validateNotBlank(phoneNumber, "phoneNumber");
        Validation.validateLength(
            phoneNumber,
            "phoneNumber",
            Validation.PHONE_NUMBER_LENGTH,
            Validation.PHONE_NUMBER_LENGTH
        );

        Validation.validateDigits(phoneNumber, "phoneNumber");

        Set<String> contactIds = phoneIndex.get(phoneNumber);

        if (contactIds == null) {
            return new HashSet<>();
        }
        //making sure to return a copy of the set, not the original.
        return new HashSet<>(contactIds);
    }

    public void remove(Contact contact) {
        Validation.validateNotNull(contact, "contact");

        nameTrie.remove(contact.getFirstName(), contact.getId());
        nameTrie.remove(contact.getLastName(), contact.getId());

        Set<String> contactIds = phoneIndex.get(contact.getPhoneNumber());
        if (contactIds != null) {
            contactIds.remove(contact.getId());
            if (contactIds.isEmpty()) {
                phoneIndex.remove(contact.getPhoneNumber());
            }
        }
    }
}
