package model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Organizer extends User {
	
	List<Event> createdEvents = new ArrayList<>();

	public Organizer(String name, String cpf, String email, LocalDate birthDate, String phoneNumber, String address) {
		super(name, cpf, email, birthDate, phoneNumber, address);

	}
	
	public List<Event> getCreatedEvents() {
		return createdEvents;
	}

	public void addCreatedEvent(Event event) {
		createdEvents.add(event);
	}

}
