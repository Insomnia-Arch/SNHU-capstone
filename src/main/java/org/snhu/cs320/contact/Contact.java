package org.snhu.cs320.contact;

import org.snhu.cs320.validation.Validation;

public final class Contact {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String address;

    public Contact(
        String id,
        String firstName,
        String lastName,
        String phoneNumber,
        String address
    ) {
        Validation.validateId(id);

        Validation.validateNotBlank(firstName, "firstName");
        Validation.validateLength(
            firstName,
            "firstName",
            Validation.MIN_TEXT_LENGTH,
            Validation.MAX_NAME_LENGTH
        );

        Validation.validateNotBlank(lastName, "lastName");
        Validation.validateLength(
            lastName,
            "lastName",
            Validation.MIN_TEXT_LENGTH,
            Validation.MAX_NAME_LENGTH
        );

        Validation.validateNotBlank(phoneNumber, "phoneNumber");
        Validation.validateLength(
            phoneNumber,
            "phoneNumber",
            Validation.PHONE_NUMBER_LENGTH,
            Validation.PHONE_NUMBER_LENGTH
        );
        Validation.validateDigits(phoneNumber, "phoneNumber");

        Validation.validateNotBlank(address, "address");
        Validation.validateLength(
            address,
            "address",
            Validation.MIN_TEXT_LENGTH,
            Validation.MAX_ADDRESS_LENGTH
        );

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}
