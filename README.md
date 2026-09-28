# CS 499 Capstone - Enhancement of CS 320 contact service

# Enhancement 1, Architecture


This enhancement focuses on the software design and engineering, separatating storage from contact operations, protecting stored data, and making tests independent. For now the application will stay as a local memory contact service with the original cs320 requirements. 

`ContactService` receives a `ContactRepository` through it's constructor. `InMemoryContactRepository`impliments the interface and owns the storage map. `Contact` objects are immutable, preventing caller from changing the stored data through setters. 

`ContactUpdateRequest` contains four editable fields and no ID. the service receives the target ID seperately and constructes a fully validated replacement `Contact` before asking the repository to replace the stored contact. Previously returned contacts retain their original values

`addcContact` and `updateContact` return the stored contact. `getContact` retrieves one contact, `getAllContacts` returns an unmodifiable unsorted copy.

`Validation`
Original length limits, exactly 10 digits for the phone number, rejection of null or blank text and preservation of surrounding spaces. Text is stored exactly as provided and string.trim() is only used to detect blank text. leading and trailing spaces are retained and count towards length limits. IDs must match exactly, including spaces. Phone numbers must contain exactly ten digits.

Errors now more appropriately explain validation errors and errors have been expanded for duplicate ID and missing contacts. Callers can now handle the specific exceptions without broad `throws Exception` declarations and update arguments are validated before checking if the ID exists. 

## Build

The original project was a gradle project originating from a previous SNHU professor, Clinton Bush and the build retains some of the original Junit testing that I had implimented during that course. 

I used Java 21 to run Gradle, and Gradle was used to build and clean and I used spotlessApply to format the Java files. Test runs the tests and Clean Build rebuilt the project, runs the tests, and checks formatting and the latest full test run had 74 passing cases.


These are the test and build commands. 

.\gradlew.bat test
.\gradlew.bat spotlessApply
.\gradlew.bat clean build



There are current limitations for the application such as in-memory storage, unspecified collection order, and no search or database enhancement yet since those will be in the future enhancements. 