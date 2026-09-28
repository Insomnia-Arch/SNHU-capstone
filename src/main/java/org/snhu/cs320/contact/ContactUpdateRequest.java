package org.snhu.cs320.contact;

/*contact updates moved to it's own class, never even passed the id so the request cannot change it
There are no more setters and the fields are final so that the request cannot be changed after construction
there is also no field validation here, the service will contructed a validated replacement before updating storage
*/
public final class ContactUpdateRequest {

    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String address;

    public ContactUpdateRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String address
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
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
