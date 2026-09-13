package Validation;
import org.snhu.cs320.Exceptions.ValidationException;
final public class Validation {

	private Validation() {}


	//validates that an entry isn't null
	public static void validateNotNull(String input, String label) throws ValidationException {
		if (input == null) {
			throw new ValidationException (label + " must not be null");
		}
	}
	//validates that an entry isn't blank
	public static void validateNotBlank(String input, String label) throws ValidationException {
		validateNotNull(input, label);
		if (input.trim().length() <1) {
			throw new ValidationException (label + " must not be blank");
		}
	}
	//validates that the length is between min and max
	public static void validateLength(String input, String label, int minLength, int maxLength) throws ValidationException {
		if (input.trim().length() < minLength || input.length() > maxLength)  {
			throw new ValidationException (label + " must be at least " + minLength + " and no greater than " + maxLength + "characters");
		}
	}
	//uses regex to verify that only digits are being passed for phone numbers
	public static void validateDigits(String input, String label) throws ValidationException {
		if (input.matches(".*\\D+.*")) {
			throw new ValidationException (label + " may only contain numbers");
		}
	}
	
}
