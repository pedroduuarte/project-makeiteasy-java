package model.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.exceptions.DomainException;

public class Event {

	private String eventName;
	private String eventPlace;
	private String eventDescription;
	private LocalDateTime eventDateTime;
	private boolean isRestrictedForMore18;
	private boolean demandsItems;
	private boolean demandsPayment;
	private User userOrganizer;
	private double paymentAmount;

	List<User> usersOnTheEvent = new ArrayList<>();

	public Event(String eventName, String eventPlace, String eventDescription, LocalDateTime eventDateTime,
			boolean isRestrictedForMore18, boolean demandsPayment, boolean demandsItems, User userOrganizer,
			double paymentAmount) {
		this.eventName = eventName;
		this.eventPlace = eventPlace;
		this.eventDescription = eventDescription;
		this.eventDateTime = eventDateTime;
		this.isRestrictedForMore18 = isRestrictedForMore18;
		this.demandsPayment = demandsPayment;
		this.demandsItems = demandsItems;
		this.userOrganizer = userOrganizer;
		this.paymentAmount = demandsPayment ? paymentAmount : 0;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public LocalDateTime getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(LocalDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public boolean getIsRestrictedForMore18() {
		return isRestrictedForMore18;
	}

	public void setIsRestrictedForMore18(boolean isRestrictedForMore18) {
		this.isRestrictedForMore18 = isRestrictedForMore18;
	}

	public boolean getDemandsPayment() {
		return demandsPayment;
	}

	public void setDemandsPayment(boolean demandsPayment) {
		this.demandsPayment = demandsPayment;
	}

	public boolean getDemandsItems() {
		return demandsItems;
	}

	public void setDemandsItems(boolean demandsItems) {
		this.demandsItems = demandsItems;
	}

	public User getUserOrganizer() {
		return userOrganizer;
	}

	public void setUserOrganizer(User userOrganizer) {
		this.userOrganizer = userOrganizer;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	public boolean checkIfTheParticipantHasEnoughAge(User user) {
		if (user.getUserAge() < 18 && this.getIsRestrictedForMore18() == true) {
			return false;
		}
		return true;
	}

	public boolean addParticipantInTheEvent(User user) {
		if (!checkIfTheParticipantHasEnoughAge(user)) {
			System.out.println("User " + user.getName() + " cannot participate in this event because it's for +18 years");
			return false;
		}
		if (usersOnTheEvent.contains(user)) {
			throw new DomainException("User" + user.getName() + " is already participating in this event.");
		}

		usersOnTheEvent.add(user);
		System.out.println("User " + user.getName() + " is now participating in the event!");
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Name: ");
		sb.append(eventName + " // ");
		sb.append("Place: ");
		sb.append(eventPlace + " // ");
		sb.append("Date: ");
		sb.append(eventDateTime + " // " + "\n");

		return sb.toString();
	}

}