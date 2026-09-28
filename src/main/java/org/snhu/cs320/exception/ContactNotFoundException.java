package org.snhu.cs320.exception;

public final class ContactNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ContactNotFoundException(String id) {
        super("Contact with id " + id + " not found.");
    }
}
