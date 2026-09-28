package org.snhu.cs320.validation;

import org.snhu.cs320.exception.ValidationException;

public final class Validation {

    public static final int MIN_TEXT_LENGTH = 1;
    public static final int MAX_ID_LENGTH = 10;
    public static final int MAX_NAME_LENGTH = 10;
    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final int MAX_ADDRESS_LENGTH = 30;

    private Validation() {}

    public static void validateId(String id) {
        validateNotBlank(id, "id");
        validateLength(id, "id", MIN_TEXT_LENGTH, MAX_ID_LENGTH);
    }

    //validates that an entry isn't null
    public static void validateNotNull(Object input, String label)
        throws ValidationException {
        if (input == null) {
            throw new ValidationException(label + " must not be null");
        }
    }

    //validates that an entry isn't blank
    public static void validateNotBlank(String input, String label)
        throws ValidationException {
        validateNotNull(input, label);
        if (input.trim().length() < 1) {
            throw new ValidationException(label + " must not be blank");
        }
    }

    //validates that the length is between min and max
    public static void validateLength(
        String input,
        String label,
        int minLength,
        int maxLength
    ) throws ValidationException {
        validateNotNull(input, label);
        if (input.length() < minLength || input.length() > maxLength) {
            throw new ValidationException(
                label +
                " must be between " +
                minLength +
                " and " +
                maxLength +
                " characters"
            );
        }
    }

    //uses regex to verify that only digits are being passed for phone numbers
    public static void validateDigits(String input, String label)
        throws ValidationException {
        validateNotNull(input, label);
        if (!input.matches("[0-9]+")) {
            throw new ValidationException(
                label + " must only contain digits (0-9)"
            );
        }
    }
}
