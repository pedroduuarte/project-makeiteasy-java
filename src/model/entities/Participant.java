package model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Participant extends User {
	
	List<Event> participatedEvents = new ArrayList<>();


	public Participant(String name, String cpf, String email, LocalDate birthDate, String phoneNumber, String address) {
		super(name, cpf, email, birthDate, phoneNumber, address);
	}
	
	public List<Event> getParticipatedEvents() {
		return participatedEvents;
	}
	
	public void addParticipatedEvent(Event event) {
		participatedEvents.add(event);
	}
}
