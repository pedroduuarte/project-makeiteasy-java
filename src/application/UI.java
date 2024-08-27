package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import model.entities.Event;
import model.entities.Payment;
import model.entities.User;
import model.exceptions.DomainException;
import model.exceptions.NotLoggedException;

public class UI {

	private static List<User> usersList = new ArrayList<>();
	private static List<Event> eventsCreated = new ArrayList<>();

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

	public static void showMenu() {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		System.out.println("=============== MENU ===============");
		System.out.println("| 1. Sign in with an account       |");
		System.out.println("| 2. Create an account             |");
		System.out.println("| 3. Create a new event            |");
		System.out.println("| 4. Join an event                 |");
		System.out.println("| 5. Check all your created events |");
		System.out.println("| 6. Exit                          |");
		System.out.println("====================================");
		System.out.println(ANSI_RESET);
	}
	
	public static void menuEventCreator() {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		System.out.println("=============== EVENT MENU ===============");
		System.out.println("| 1. See all participants                 |");
		System.out.println("| 2. Remove a participant from the event. |");
		System.out.println("| 3. See all payment from the event.      |");
		System.out.println("==========================================");
		System.out.println(ANSI_RESET);
	}
	
	public static User createAnAccount(Scanner sc) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		try {
			System.out.println("We need just some informations to make your account!");
			System.out.print("Name: ");
			String name = sc.nextLine();

			System.out.print("CPF (only numbers): ");
			String cpf = sc.nextLine();
			if (!cpf.matches("\\d{11}")) {
				throw new DomainException("Invalid CPF! Please enter only numbers and exactly 11 digits.");
			}

			System.out.print("E-mail: ");
			String email = sc.nextLine();
			if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
				throw new DomainException("Invalid email format!");
			}

			System.out.print("Birth date (dd/MM/yyyy): ");
			LocalDate birthDate = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			sc.nextLine();

			System.out.print("Phone number: ");
			String phoneNumber = sc.nextLine();

			System.out.print("Address: ");
			String address = sc.nextLine();

			User newUser = new User(name, cpf, email, birthDate, phoneNumber, address);
			usersList.add(newUser);

			System.out.println("Account created successfully! Welcome " + newUser.getName());
			return newUser;

		} catch (InputMismatchException e) {
			System.out.println("Invalid input format. Please try again.");
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date format! Please use dd/MM/yyyy.");
		} catch (DomainException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(ANSI_RESET);
		return null;
	}

	public static User login(Scanner sc) {
		System.out.print("Do you already have an account on our system? (y/n) ");
		String answer = sc.next();

		if (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
			throw new DomainException("Invalid answer! Please enter 'y' or 'n'.");
		}

		if (answer.equalsIgnoreCase("n")) {
			createAnAccount(sc);
		} else {
			if (usersList.isEmpty()) {
				System.out.println("No users found! Please create an account first.");
				return null;
			}

			System.out.print("Type your CPF: ");
			sc.nextLine();
			String searchCpf = sc.nextLine();
			for (User user : usersList) {
				if (user.getCpf().equals(searchCpf)) {
					System.out.println("Welcome " + user.getName());
					return user;
				}
			}
			System.out.println("User not found! Please check your CPF or create a new account.");
		}

		return null;
	}

	public static void createAnEvent(Scanner sc, User user) {
		if (user == null) {
			System.out.println("You need to sign in first.");
			return;
		}
		System.out.println(ANSI_PURPLE_BACKGROUND);
		System.out.println("Let's create your Event!");
		System.out.println("Type your event datas:");
		System.out.print("Name: ");
		String eventName = sc.nextLine();
		System.out.print("Place: ");
		String eventPlace = sc.nextLine();
		System.out.print("Type a short description about this event: ");
		String eventDescription = sc.nextLine();
		LocalDateTime eventDate = null;
		while (eventDate == null) {
			try {
				System.out.print("Event date (dd/MM/yyyy HH:mm): ");
				String eventDateStr = sc.nextLine();
				eventDate = LocalDateTime.parse(eventDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date and hour! Please try again.");
			}
		}

		boolean isRestrictedForMore18 = false;
		while (true) {
			System.out.print("Is this event restricted for +18? (y/n): ");
			String answerRestrictedEvent = sc.next();
			if (!answerRestrictedEvent.equalsIgnoreCase("y") && !answerRestrictedEvent.equalsIgnoreCase("n")) {
				throw new DomainException("Invalid answer! Please enter 'y' or 'n'.");
			} else if (answerRestrictedEvent.equalsIgnoreCase("y")) {
				isRestrictedForMore18 = true;
				break;
			} else if (answerRestrictedEvent.equalsIgnoreCase("n")) {
				isRestrictedForMore18 = false;
				break;
			}
		}

		boolean demandsPayment = false;
		double paymentAmount = 0.0;
		while (true) {
			System.out.print("The event demands payment to participate? (y/n): ");
			String answerDemandsPayment = sc.next();
			if (!answerDemandsPayment.equalsIgnoreCase("y") && !answerDemandsPayment.equalsIgnoreCase("n")) {
				throw new DomainException("Invalid answer! Please enter 'y' or 'n'.");
			} else if (answerDemandsPayment.equalsIgnoreCase("y")) {
				demandsPayment = true;
				System.out.print("Type the payment's value: $ ");
				paymentAmount = sc.nextDouble();
				sc.nextLine();
				break;
			} else if (answerDemandsPayment.equalsIgnoreCase("n")) {
				demandsPayment = false;
				break;
			}
		}

		boolean demandsItems = false;
		while (true) {
			System.out.print("The event demands that participants bring items to participate? (y/n): ");
			String answerDemandsItems = sc.next();
			if (!answerDemandsItems.equalsIgnoreCase("y") && !answerDemandsItems.equalsIgnoreCase("n")) {
				throw new DomainException("Invalid answer! Please enter 'y' or 'n'.");
			} else if (answerDemandsItems.equalsIgnoreCase("y")) {
				demandsItems = true;
				break;
			} else if (answerDemandsItems.equalsIgnoreCase("n")) {
				demandsItems = false;
				break;
			}
		}

		Event newEvent = new Event(eventName, eventPlace, eventDescription, eventDate, isRestrictedForMore18,
				demandsPayment, demandsItems, user, paymentAmount);
		eventsCreated.add(newEvent);
		System.out.println("Event created sussefully!");
		System.out.println(ANSI_RESET);

	}

	public static boolean processPayment(User user, Event event, Scanner sc) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		if (!event.getDemandsPayment()) {
			System.out.println("This event don't requires payment.");
			return true;
		}
		System.out.printf("This event demands a payment value of: $ %.2f%n", event.getPaymentAmount());
		System.out.print("Type the method of payment: ");
		String paymentMethod = sc.nextLine();

		Payment payment = new Payment(event.getPaymentAmount(), LocalDateTime.now(), paymentMethod);

		try {
			payment.isValidPaymentMethod();
			System.out.println("Processing payment...");
			payment.setStatus("Completed");
			System.out.println("A tax of 5% will be increased to the final value.");
			payment.calculateTax(event.getPaymentAmount());
			System.out.printf("Payment made succefully! Value with tax: $ %.2f%n", payment.calculateFinalValue());

			if (payment.getStatus().equals("Completed")) {
				event.addParticipantInTheEvent(user);
				return true;
			}
		} catch (DomainException e) {
			System.out.println(e.getMessage());
			return false;
		}

		return false;
	}

	public static void JoinAnEvent(User user, Scanner sc) {
		if (user == null) {
			throw new NotLoggedException("You need to be logged to join an event.");
		}

		if (eventsCreated.isEmpty()) {
			System.out.println("There is no events created yet.");
			return;
		}

		System.out.println("Please select an event to join:");
		for (int i = 0; i < eventsCreated.size(); i++) {
	        Event event = eventsCreated.get(i);
	        System.out.println((i + 1) + ". " + event);
	    }

		System.out.print("Type de number of the event you want to join: ");
		int choiceChooseEvent = sc.nextInt();
		sc.nextLine();

		if (choiceChooseEvent < 1 || choiceChooseEvent > eventsCreated.size()) {
			throw new DomainException("Invalid choice. Please select a correct number.");
		}

		Event choosenEvent = eventsCreated.get(choiceChooseEvent - 1);
		
		if (!choosenEvent.checkIfTheParticipantHasEnoughAge(user)) {
			throw new DomainException("User " + user.getName() + " cannot participate in this event because it's for +18 years");
		}

		if (choosenEvent.getDemandsPayment()) {
			boolean sucess = processPayment(user, choosenEvent, sc);
			if (!sucess) {
				throw new DomainException("Failed processing your payment!");
			}
		}
		if (choosenEvent.addParticipantInTheEvent(user)) {
			System.out.println("You're participating at this event!");
		}
		System.out.println(ANSI_RESET);

	}
	
	public static void showUserEvents(User user) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		if (user == null) {
			throw new NotLoggedException("You need to be logged to see your events.");
		}
		
		System.out.println("Events created by you:");
		if (user.getCreatedEvents().isEmpty()) {
			System.out.println("You haven't created any events yet.");
		}
		else {
			for (Event createdEvent : user.getCreatedEvents()) {
				System.out.println(createdEvent);
			}
		}
		
		System.out.println("Events you participated:");
		if (user.getParticipatedEvents().isEmpty()) {
			System.out.println("You haven't participated in any events yet.");
		}
		else {
			for (Event participatedEvent : user.getParticipatedEvents()) {
				System.out.println(participatedEvent);
			}
		}
		System.out.println(ANSI_RESET);
	}

}