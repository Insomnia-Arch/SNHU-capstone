package Contact;

import Validation.Validation;

public class Contact {
private String id;
private String firstName;
private String lastName;
private String phoneNumber;
private String address;
//constructor. the this.(variable) is inside of the setters for validation purposes.
public Contact(String id, String firstName, String lastName, String phoneNumber, String address)throws Exception {
	super();
	setID(id);
	setFirstName(firstName);
	setLastName(lastName);
	setPhoneNumber(phoneNumber);
	setAddress(address);

}
public String getFirstName() {
	return firstName;
}
//validation rules in the validation.java classes.
//verifies that the name cannot be set if the name is null, blank, or too long.
public void setFirstName(String firstName) throws Exception {
	Validation.validateNotNull(firstName,"firstName");
	Validation.validateNotBlank(firstName, "firstName");
	Validation.validateLength(firstName,  "firstName", 1, 10);
	
	this.firstName = firstName;
}
public String getLastName() {
	return lastName;
}
//verifies that the name cannot be set if the name is null, blank, or too long.
public void setLastName(String lastName) throws Exception {
	Validation.validateNotNull(lastName,"lastName");
	Validation.validateNotBlank(lastName, "lastName");
	Validation.validateLength(lastName,  "lastName", 1, 10);
	this.lastName = lastName;
}
public String getPhoneNumber() {
	return phoneNumber;
}
//verifies that the name cannot be set if the number is null, blank, or not 10 digits
public void setPhoneNumber(String phoneNumber)throws Exception {
	Validation.validateNotNull(phoneNumber,"phoneNumber");
	Validation.validateNotBlank(phoneNumber, "phoneNumber");
	Validation.validateLength(phoneNumber,  "phoneNumber", 10, 10);
	Validation.validateDigits(phoneNumber, "phoneNumber");
	this.phoneNumber = phoneNumber;
}
public String getAddress() {
	return address;
}
//verifies that the address cant be set if it is null, blank, or too long
public void setAddress(String address) throws Exception{
	Validation.validateNotNull(address,"address");
	Validation.validateNotBlank(address, "address");
	Validation.validateLength(address,  "address", 1, 30);
	this.address = address;
}
//verifies that the id cannot be created if the name is null, blank, or too long.
private void setID(String id) throws Exception{
	Validation.validateNotNull(id,"id");
	Validation.validateNotBlank(id, "id");
	Validation.validateLength(id,  "id", 1, 10);
	this.id = id;
}

public String getId() {
	return id;
}

}
