package org.snhu.cs320.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.snhu.cs320.validation.Validation;

public final class NameTrie {

    private final TrieNode root = new TrieNode();

    public void insert(String name, String contactId) {
        String normalizedName = normalizeName(name);
        Validation.validateId(contactId);

        TrieNode current = root;

        for (char character : normalizedName.toCharArray()) {
            current =
                current.children.computeIfAbsent(
                    character,
                    key -> new TrieNode()
                );
        }

        current.contactIds.add(contactId);
    }

    private static String normalizeName(String name) {
        Validation.validateNotBlank(name, "name");
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private static final class TrieNode {

        private final Map<Character, TrieNode> children = new HashMap<>();
        private final Set<String> contactIds = new HashSet<>();
    }

    public Set<String> searchByPrefix(String prefix) {
        String normalizedPrefix = normalizeName(prefix);
        TrieNode current = root;
        for (char character : normalizedPrefix.toCharArray()) {
            current = current.children.get(character);
            if (current == null) {
                return new HashSet<>();
            }
        }
        Set<String> results = new HashSet<>();
        collectContactIds(current, results);
        return results;
    }

    private void collectContactIds(TrieNode node, Set<String> results) {
        results.addAll(node.contactIds);
        for (TrieNode child : node.children.values()) {
            collectContactIds(child, results);
        }
    }

    public void remove(String name, String contactId) {
        String normalizedName = normalizeName(name);
        Validation.validateId(contactId);

        remove(root, normalizedName, contactId, 0);
    }

    private boolean remove(
        TrieNode node,
        String name,
        String contactId,
        int depth
    ) {
        if (depth == name.length()) {
            node.contactIds.remove(contactId);
        } else {
            char character = name.charAt(depth);
            TrieNode child = node.children.get(character);
            if (child == null) {
                return false;
            }

            boolean shouldPruneChild = remove(
                child,
                name,
                contactId,
                depth + 1
            );
            if (shouldPruneChild) {
                node.children.remove(character);
            }
        }

        return node.contactIds.isEmpty() && node.children.isEmpty();
    }
}
