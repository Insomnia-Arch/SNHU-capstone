package org.snhu.cs320.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

public class NameTrieTest {

    @Test
    void findsExactNameAndLongerNamesWithSamePrefix() {
        NameTrie trie = new NameTrie();
        trie.insert("Ann", "1");
        trie.insert("Anna", "2");
        trie.insert("Anne", "3");
        trie.insert("Arch", "4");

        assertEquals(
            new HashSet<>(Arrays.asList("1", "2", "3")),
            trie.searchByPrefix("Ann")
        );
    }

    @Test
    void searchIgnoresCaseAndSurroundingSpaces() {
        NameTrie trie = new NameTrie();
        trie.insert(" Anna ", "1");

        assertEquals(
            new HashSet<>(Arrays.asList("1")),
            trie.searchByPrefix(" AN ")
        );
    }

    @Test
    void missingPrefixReturnsEmptySet() {
        NameTrie trie = new NameTrie();
        trie.insert("Anna", "1");

        assertEquals(new HashSet<>(), trie.searchByPrefix("Arch"));
    }

    @Test
    void matchingNamesReturnEachContactIdOnce() {
        NameTrie trie = new NameTrie();
        trie.insert("John", "1");
        trie.insert("Johnson", "1");
        trie.insert("John", "2");

        assertEquals(
            new HashSet<>(Arrays.asList("1", "2")),
            trie.searchByPrefix("Jo")
        );
    }

    @Test
    void removingOneContactPreservesAnotherWithSameName() {
        NameTrie trie = new NameTrie();
        trie.insert("Ann", "1");
        trie.insert("Ann", "2");

        trie.remove("Ann", "1");

        assertEquals(
            new HashSet<>(Arrays.asList("2")),
            trie.searchByPrefix("Ann")
        );
    }

    @Test
    void removingShorterNamePreservesLongerName() {
        NameTrie trie = new NameTrie();
        trie.insert("Ann", "1");
        trie.insert("Anna", "2");

        trie.remove("Ann", "1");

        assertEquals(
            new HashSet<>(Arrays.asList("2")),
            trie.searchByPrefix("ann")
        );
    }

    @Test
    void removingLongerNamePreservesShorterName() {
        NameTrie trie = new NameTrie();
        trie.insert("Ann", "1");
        trie.insert("Anna", "2");

        trie.remove("Anna", "2");

        assertEquals(
            new HashSet<>(Arrays.asList("1")),
            trie.searchByPrefix("ann")
        );
        //also checking that the original was deleted
        assertEquals(new HashSet<>(), trie.searchByPrefix("anna"));
    }

    @Test
    void removingMissingAssociationLeavesExistingContacts() {
        NameTrie trie = new NameTrie();
        trie.insert("Ann", "1");
        //delete two that don't exist
        trie.remove("Ann", "2");
        trie.remove("Arch", "1");

        assertEquals(
            new HashSet<>(Arrays.asList("1")),
            trie.searchByPrefix("ann")
        );
    }
}
