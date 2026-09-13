package Contact;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ContactTest {
	//tests creating entities 
	@Test
	void testSuccessfulCreation() throws Exception {
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		assertThat(contact)
			.hasFieldOrPropertyWithValue("id", "1")
			.hasFieldOrPropertyWithValue("firstName", "First")
			.hasFieldOrPropertyWithValue("lastName", "Last")
			.hasFieldOrPropertyWithValue("phoneNumber", "1111111111")
			.hasFieldOrPropertyWithValue("address", "1234 Numbers Lane");
	}
	//tests setting properties
	@Test
	void testSuccessfulSetters() throws Exception {
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		contact.setFirstName("Arch");
		contact.setLastName("Arch");
		contact.setPhoneNumber("2222222222");
		contact.setAddress("1234 Letters Street");
		
		assertThat(contact)
			.hasFieldOrPropertyWithValue("firstName", "Arch")
			.hasFieldOrPropertyWithValue("lastName", "Arch")
			.hasFieldOrPropertyWithValue("phoneNumber", "2222222222")
			.hasFieldOrPropertyWithValue("address", "1234 Letters Street");
	
	}
	//a csv of entries for testing purposes.
	@CsvSource({
		" ' ', First, Last, 1111111111, 1234 Numbers Lane",//blank ID
		", First, Last, 1111111111, 1234 Numbers Lane",//null ID
		"12121212121, First, Last, 1111111111, 1234 Numbers Lane",//ID that is way too long 
		"1, '', Last, 1111111111, 1234 Numbers Lane",//blank Name 
		"1,, Last, 1111111111, 1234 Numbers Lane",//null Name
		"1, FirstNameThatIsWayTooLong, Last, 1111111111, 1234 Numbers Lane",//Name that is way too long
		"1, First, '', 1111111111, 1234 Numbers Lane",
		"1, First,, 1111111111, 1234 Numbers Lane",
		"1, First, LastNameThatIsWayTooLong, 1111111111, 1234 Numbers Lane",
		"1, First, Last, 11111111111, 1234 Numbers Lane",//phone number too long
		"1, First, Last, 1111111a1z, 1234 Numbers Lane",//phone number that contains letters
		"1, First, Last, 111-111-1111, 1234 Numbers Lane", //phone number that has punctuation
		"1, First, Last, 111 111 1111, 1234 Numbers Lane", //phone number with spaces
		"1, First, Last, '', 1234 Numbers Lane", //blank phone number
		"1, First, Last,, 1234 Numbers Lane",//null phone number
		"1, First, Last, 1111111111, ''",//blank address
		"1, First, Last, 1111111111,",//null address
		"1, First, Last, 1111111111, 1234 Numbers and letters and way too many of them Lane", //address that is too long
	})
	
	
	//tests setting each of these invalid entries
	@ParameterizedTest
	void testFailedCreation(String id, String firstName, String lastName, String phoneNumber, String address) throws Exception{
		//System.out.println(id + "," + firstName + "," + lastName + "," + phoneNumber);
		assertThatThrownBy(() -> new Contact(id, firstName, lastName, phoneNumber, address))
			.isNotNull();
	}

	@CsvSource({
		",",
		"' ',",
		"TooLongFirstName,"
		
	})
	//tests setting a first name
	@ParameterizedTest
	void testSettingFirstName (String firstName) throws Exception {
		Contact contact = new Contact("1","First", "Last", "1111111111", "1234 Numbers Lane");
		assertThatThrownBy(() -> contact.setFirstName(firstName))
			.isNotNull();
	}
	
}
