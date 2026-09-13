# CS320 Capstone

Java contact service project with unit tests for contact validation and adding, updating, and deleting contacts.

## Project structure

- `src/main/java/Contact`: Contact model and service.
- `src/main/java/Validation`: Shared input validation.
- `src/test/java/Contact`: JUnit tests.

## Run tests

On Windows:

```powershell
.\gradlew.bat test
```

On macOS or Linux:

```bash
./gradlew test
```

The Gradle build uses a Java 8 toolchain, JUnit 5, and AssertJ.

## Acknowledgment

The Gradle project setup originated from Clinton Bush's CS320 course starter project.
