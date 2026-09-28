package org.snhu.cs320.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.snhu.cs320.exception.ValidationException;

public class ValidationTest {

    @Test
    void ValidateLengthRejectsNull() {
        ValidationException exception = assertThrowsExactly(
            ValidationException.class,
            () -> Validation.validateLength(null, "firstName", 1, 10)
        );
        assertEquals("firstName must not be null", exception.getMessage());
    }

    @Test
    void validateDigitsRejectsNull() {
        ValidationException exception = assertThrowsExactly(
            ValidationException.class,
            () -> Validation.validateDigits(null, "phoneNumber")
        );
        assertEquals("phoneNumber must not be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "A", "ABCDEFGHIJ" })
    void validateLengthAcceptsBoundries(String input) {
        assertDoesNotThrow(() ->
            Validation.validateLength(input, "firstName", 1, 10)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "ABCDEFGHIJK", " ABCDEFGHIJ" })
    void validateLengthRejectsOutsideLimits(String input) {
        assertThrowsExactly(
            ValidationException.class,
            () -> Validation.validateLength(input, "firstName", 1, 10)
        );
    }

    @Test
    void validateLengthCountsSurroundingSpaces() {
        assertDoesNotThrow(() -> Validation.validateLength(" A ", "value", 3, 3)
        );
    }

    @Test
    void validateDigitsAcceptingLeadingZeros() {
        assertDoesNotThrow(() ->
            Validation.validateDigits("0123456789", "phoneNumber")
        );
    }

    @ParameterizedTest
    @ValueSource(
        strings = { "", "123456789x", "123-456789", "12345678 ", "123456789\n" }
    )
    void validateDigitsRejectsNonDigits(String input) {
        assertThrowsExactly(
            ValidationException.class,
            () -> Validation.validateDigits(input, "phoneNumber")
        );
    }
}
