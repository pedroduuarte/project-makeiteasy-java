package application;

import java.util.List;
import java.util.Scanner;

import model.entities.Event;
import model.entities.Organizer;
import model.entities.Participant;
import model.entities.Payment;
import model.entities.User;
import model.exceptions.DomainException;
import model.exceptions.InvalidOptionException;

public class MenuOrganizer {
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	
	public static void menuEventOrganzier(Scanner sc, Event event) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		while (true) {
			System.out.println("=============== EVENT MENU ===============");
			System.out.println("| 1. See all participants                 |");
			System.out.println("| 2. Remove a participant from the event  |");
			System.out.println("| 3. See all payments from the event      |");
			System.out.println("| 4. Go back                              |");
			System.out.println("==========================================");
			System.out.println();
			System.out.print("Choose your option: ");
			int option = sc.nextInt();
			sc.nextLine();
			if (option <= 0 || option > 4) {
				throw new InvalidOptionException("Invalid option. Please choose an option between 1 and 4.");
			}
			switch (option) {
			case 1: {
				showAllParticipants(event);
				break;
			}
			case 2: {
				removeAParticipantFromTheEvent(sc, event);
				break;
			}
			case 3: {
				showAllPayments(event);
				break;
			}
			case 4: {
				System.out.println("Going back...");
				return;
			}
			}			
		}
	}

	public static void acessMenuEvent(User user, Scanner sc) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		if (!(user instanceof Organizer)) {
			System.out.println("Please sign with an organizer account first.");
			return;
		}
		Organizer organizer = (Organizer) user;
		if (organizer.getCreatedEvents().isEmpty()) {
			System.out.println("You have not created any events yet.");
			return;
		}
		System.out.println("Choose the event you want to acess:");
		for (int i = 0; i < organizer.getCreatedEvents().size(); i++) {
			Event event = organizer.getCreatedEvents().get(i);
			System.out.println((i + 1) + ". " + event);
		}

		int choiceChoosenEvent = sc.nextInt();
		sc.nextLine();
		if (choiceChoosenEvent < 1 || choiceChoosenEvent > organizer.getCreatedEvents().size()) {
			throw new DomainException("Invalid choice. Please select a correct number.");
		}

		Event choosenEvent = organizer.getCreatedEvents().get(choiceChoosenEvent - 1);
		menuEventOrganzier(sc, choosenEvent);
	}
	
	public static void showAllParticipants(Event event) {
		List<Participant> participants = event.getUsersOnTheEvent();
		if (participants.isEmpty()) {
			System.out.println("There is no participants in the event yet.");
			return;
		}
		for (Participant participant : participants) {
			System.out.println(participant);
			System.out.println("-----------------------");
		}
		System.out.println(ANSI_RESET);
	}
	
	public static void removeAParticipantFromTheEvent(Scanner sc, Event event) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		List<Participant> participants = event.getUsersOnTheEvent();
		if (participants.isEmpty()) {
			System.out.println("There is no participants in the event yet.");
			return;
		}
		System.out.println("Choose a participant to remove:");
		for (int i = 0; i < participants.size(); i++) {
			System.out.println((i+1) + ". " + participants.get(i).getName());
		}
		int choice = sc.nextInt();
		sc.nextLine();
		if (choice < 1 || choice > participants.size()) {
			System.out.println("Invalid choice. Please input a choice between 1 and " + participants.size());
			return;
		}
		
		Participant participantToRemove = participants.get(choice - 1);
		event.getUsersOnTheEvent().remove(participantToRemove);
		System.out.println("Participant " + participantToRemove + " was removed.");	
		System.out.println(ANSI_RESET);
	}
	
	public static void showAllPayments(Event event) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		List<Payment> payments = event.getPayments();
		if (payments.isEmpty()) {
			System.out.println("There is no payments recorded for this event yet.");
			return;
		}
		System.out.println("Payment for event: " + event.getEventName());
		for (Payment p : payments) {
			System.out.println(p);
		}	
		System.out.println(ANSI_RESET);
	}
}
