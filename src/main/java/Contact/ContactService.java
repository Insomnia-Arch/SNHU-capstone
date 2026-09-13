package Contact;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContactService {
	
	//utilized a singleton pattern to verify that there can only ever be one instance running
	private static ContactService INSTANCE;
	//used hashmap to verify that time complexity is low following industry standards
	Map<String, Contact> database = new ConcurrentHashMap<>();
	
	private ContactService() {}
	
	//ensures that only one thread at a time of getInstance can be used. 
	public static synchronized ContactService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ContactService();
		}
		return INSTANCE;
	}
	//adds an Id if the ID being requested is null 
	public boolean add(Contact contact) {
		return database.putIfAbsent(contact.getId(), contact)== null;
		
	}
	//deletes an ID from the system when requested
	public boolean delete(String id) {
		return database.remove(id) != null;
		
	}
	//updates a contact with information
	public boolean update(String id, Contact updated) throws Exception {
		Contact existing = database.get(id);
		if(existing == null) return false;
		existing.setFirstName(updated.getFirstName());
		existing.setLastName(updated.getLastName());
		existing.setPhoneNumber(updated.getPhoneNumber());
		existing.setAddress(updated.getAddress());
		return true;
		
	}
	
	
	
}
