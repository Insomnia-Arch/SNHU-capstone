package org.snhu.cs320.exception;

public final class ValidationException extends IllegalArgumentException {

    private static final long serialVersionUID = 4626660215334437295L;

    public ValidationException(String message) {
        super(message);
    }
}
