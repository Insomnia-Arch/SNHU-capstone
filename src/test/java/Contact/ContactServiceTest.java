package Contact;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContactServiceTest {
	//clears out the database of all ID's before each test
	@BeforeEach
	void init() {
		ContactService.getInstance().database.clear();
	}
	//tests if the database is null
	@Test
	void testGetInstance() {
		assertThat(ContactService.getInstance()).isNotNull();
	}
	//tests adding an ID
	@Test
	void testAdd() throws Exception {
		
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		assertThat(ContactService.getInstance().add(contact)).isTrue();
		assertThat(ContactService.getInstance().database)
			.containsEntry("1", contact);
	}
	//tests deleting an entry 
	@Test
	void testDelete() throws Exception {
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		assertThat(ContactService.getInstance().add(contact)).isTrue();
		assertThat(ContactService.getInstance().delete("1")).isTrue();
		assertThat(ContactService.getInstance().database).doesNotContainEntry("1", contact);
	}
	//tests updating an entry 
	@Test 
	void testUpdate() throws Exception {
		//valid contact first 
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		//double checks that it was successfully added
		assertThat(ContactService.getInstance().add(contact)).isTrue();
		//updated contact 
		Contact updated = new Contact ("2", "Last","First", "2222222222", "1234 Letters Street");
		assertThat(ContactService.getInstance().update("1",updated)).isTrue();
		assertThat(ContactService.getInstance().database)
			.extracting("1")
			.hasFieldOrPropertyWithValue("firstName","Last")
			.hasFieldOrPropertyWithValue("lastName","First")
			.hasFieldOrPropertyWithValue("phoneNumber","2222222222")
			.hasFieldOrPropertyWithValue("address","1234 Letters Street");
	}
	//tests adding an ID that already exists
	@Test 
	void testAddExisting() throws Exception {
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		assertThat(ContactService.getInstance().add(contact)).isTrue();
		
		//duplicate ID
		Contact duplicate = new Contact("1", "Last","First", "2222222222", "1234 Letters Street");
		boolean result = ContactService.getInstance().add(duplicate);
		//verifies that return was false since the Id already exists. 
		assertThat(result).isFalse();
		//verifies that the original contact is still in there
		Contact stored = ContactService.getInstance().database.get("1");
			assertThat(stored.getFirstName()).isEqualTo("First");
			assertThat(stored.getLastName()).isEqualTo("Last");
			assertThat(stored.getPhoneNumber()).isEqualTo("1111111111");
			assertThat(stored.getAddress()).isEqualTo("1234 Numbers Lane");
	}
	//test updating an ID that doesn't exist
	@Test
	void testUpdateNonExistingId() throws Exception {
		Contact updated = new Contact("3", "bobby", "hill", "3333333333", "1234 Sesame Street");
		boolean result = ContactService.getInstance().update("3",  updated);
		assertThat(result).isFalse();
		assertThat(ContactService.getInstance().database).doesNotContainKey("3");
	}
	//tests updating a phone number to be something that is out of bounds
	@Test 
	void testUpdateBadPhone() throws Exception {
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		assertThat(ContactService.getInstance().add(contact)).isTrue();
		//assert that updating throws and exception
		Exception exception = null;
	    try {
	    	contact.setPhoneNumber("12345P-90123");
	    } catch (Exception e) {
	        exception = e;
	    }
	    //verifies that the exception is from the phone Number
	    assertThat(exception)
	    .isInstanceOf(Exception.class)
	    .hasMessageContaining("phoneNumber");
	}
	//tests updating a contact's first name to be null
	@Test 
	void testUpdateBadFirstName() throws Exception {
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		assertThat(ContactService.getInstance().add(contact)).isTrue();
		
		Exception exception = null;
	    try {
	    	contact.setFirstName("");
	    } catch (Exception e) {
	        exception = e;
	    }
	    assertThat(exception)
	    .isInstanceOf(Exception.class)
	    .hasMessageContaining("firstName");
	}
	//tests updating an address to be too long
	@Test 
	void testUpdateBadAddress() throws Exception {
		Contact contact = new Contact("1", "First", "Last", "1111111111", "1234 Numbers Lane");
		assertThat(ContactService.getInstance().add(contact)).isTrue();
		
		Exception exception = null;
	    try {
	    	contact.setAddress("Smile. Get it? because there's a mile between each S, so this address is too long");
	    } catch (Exception e) {
	        exception = e;
	    }
	    assertThat(exception)
	    .isInstanceOf(Exception.class)
	    .hasMessageContaining("address");
	}
	
	
}
