package org.snhu.cs320.exception;

public final class DuplicateContactException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateContactException(String id) {
        super("Contact with id " + id + " already exists.");
    }
}
