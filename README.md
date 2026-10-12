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



## Enhancement 2

Added Trie to search contacts by first-name or last-name prefix
Searches ignore capitalization and whitespace while preserving original stored names
added a hash map from phone numbers from sets of contact IDs allowing multiple contacts to share a phone number 
Search results contain each contact once and are stored by last name, first name, then ID. 
Adding, updating, or deleting a contact also updates searches.
Added tests for shared names and phone numbers, matching prefixes, deletion, and isolating ID's from input.
Indexes use additional memory and require maintenance but avoid searching through every single index for a search.
Phone lookup averages O(1) plus time for retrieving and sorting matches. Trie traversal depends on prefix length and matching subtree. Sorting k results takes O(k log k)
This enhancement mostly fulfilled Course Outcome 3, chosing and evaluating data structures and explaining their trade-offs.

## Enhancement 3

Added MySQL, allowing contact records to remain stored after the program is closed.
Implimented MySQLRepository using the existing ContactRepository.
MySQLRepository keeps the abstraction, and methods can be supplied info either in-memory or from MySQL.
Created a database schema with ID as the primary key, required fields match existing data limits, matching fields, and checks for empty text.
Used prepared statements to keep values seperate from SQL instructions.
created an application account with CRUD permissions limited to the project database. 
used try-with-resources to close connections, statements and result sets.
Modified duplicate and missing record cases into existing exceptions and made Repository Exception for other SQL failures.
Rebuilt search indexs from persisted contacts when the service starts.
Downloaded 100 synthetic entries for the SQL database from https://www.mockaroo.com/ 
Added tests for database CRUD, Duplicates, unchanged updates, persistence across instances, search integration, and names containing punctuation.
114 total tests passed, including 11 MySQL repository tests. 
The MySQL integration requires server setup and connection configuration. Search indexes remain in memory and do not automatically reflect changes made outside the service. 
This enhancement fulfilled outcomes 3 and 4, with security practices also supporting outcome 5. 

## Issues encountered this enhancement 

For some reason this version of MySQL wouldn't work with my java version. I was getting an Unsupported class file major version 69 error. This was because my terminal was using Java 25, but the projects Gradle 8.7 version couldn't run it, so I fixed it by setting JAVA_HOME to my installed Java 21. I had done this previously but I had a power outage in the middle of editing, and when I came back it just wouldn't work. VS code was also having issues with my last file path, compiling gradle succesffuly but VSCode giving me import and package errors. The dependencies had to be updated, and I had to change the build path in general. Some of the classes that I had written in that build had to copied into this version, with imports adjusted project-wide. 