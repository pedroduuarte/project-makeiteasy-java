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
import model.entities.Item;
import model.entities.Organizer;
import model.entities.Participant;
import model.entities.Payment;
import model.entities.User;
import model.exceptions.CpfException;
import model.exceptions.DomainException;
import model.exceptions.EmailException;
import model.exceptions.InvalidOptionException;
import model.exceptions.NotLoggedException;
import model.exceptions.PastDateException;

public class UI {

	private static List<User> usersList = new ArrayList<>();
	private static List<Event> eventsCreated = new ArrayList<>();

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";

	public static void showMenu() {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		System.out.println("==================== MENU ====================");
		System.out.println("| 1. Sign in with an account                 |");
		System.out.println("| 2. Create an account                       |");
		System.out.println("| 3. Create a new event                      |");
		System.out.println("| 4. Join an event                           |");
		System.out.println("| 5. Acess organizer menu                    |");
		System.out.println("| 6. Show all events you created/participated|");
		System.out.println("| 7. Exit                                    |");
		System.out.println("==============================================");
		System.out.println(ANSI_RESET);
	}

	public static User createAnAccount(Scanner sc) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		User newUser = null;

		System.out.println("We need just some informations to make your account!");
		String name = "";
		while (true) {
			try {
				System.out.print("Name: ");
				name = sc.nextLine();
				if (name.trim().isEmpty()) {
					throw new DomainException("Name cannot be empty. ");
				}
				break;
			} catch (DomainException e) {
				System.out.println(e.getMessage());
			}
		}
		String cpf = "";
		while (true) {
			try {
				System.out.print("CPF (only numbers): ");
				cpf = sc.nextLine();
				if (!cpf.matches("\\d{11}")) {
					throw new CpfException("Invalid CPF! Please enter only numbers and exactly 11 digits.");
				}
				break;
			} catch (CpfException e) {
				System.out.println(e.getMessage());
			}
		}

		String email = "";
		while (true) {
			try {
				System.out.print("E-mail: ");
				email = sc.nextLine();
				if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
					throw new EmailException("Invalid email format!");
				}
				break;
			} catch (EmailException e) {
				System.out.println(e.getMessage());
			}
		}

		LocalDate birthDate = null;
		while (birthDate == null) {
			try {
				System.out.print("Birth date (dd/MM/yyyy): ");
				birthDate = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				sc.nextLine();
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date format! Please enter the date in dd/MM/yyyy format.");
				sc.nextLine();
			}
		}

		String phoneNumber = "";
		while (true) {
			try {
				System.out.print("Phone number: ");
				phoneNumber = sc.nextLine();
				if (phoneNumber.trim().isEmpty()) {
					throw new DomainException("Phone number cannot be empty.");
				}
				break;
			} catch (DomainException e) {
				System.out.println(e.getMessage());
			}
		}
		String address = "";
		while (true) {
			try {
				System.out.print("Address: ");
				address = sc.nextLine();
				if (address.trim().isEmpty()) {
					throw new DomainException("Address cannot be empty.");
				}
				break;
			} catch (DomainException e) {
				System.out.println(e.getMessage());
			}
		}
		String accountType = "";
		while (true) {
			try {
				System.out.println("What type of account do you want to create? Event organizer or Event participant?");
				System.out.print("Type 'o' for ORGANIZER or type 'p' for PARTICIPANT: ");
				accountType = sc.next();
				sc.nextLine();
				if (accountType.equalsIgnoreCase("o")) {
					newUser = new Organizer(name, cpf, email, birthDate, phoneNumber, address);
				} else if (accountType.equalsIgnoreCase("p")) {
					newUser = new Participant(name, cpf, email, birthDate, phoneNumber, address);
				} else {
					throw new InvalidOptionException("Invalid option. Please type 'o' for ORGANIZER or 'p' for PARTICIPANT.");
				}
				break;
			} catch (InvalidOptionException e) {
				System.out.println(e.getMessage());
			}
		}

		usersList.add(newUser);
		System.out.println("Account created successfully! Welcome " + newUser.getName());

		return newUser;
	}

	public static User login(Scanner sc) {
		System.out.print("Do you already have an account on our system? (y/n) ");
		String answer = sc.next();
		sc.nextLine();

		if (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
			throw new InvalidOptionException("Invalid answer! Please enter 'y' or 'n'.");
		}

		if (answer.equalsIgnoreCase("n")) {
			return createAnAccount(sc);
		} else {
			if (usersList.isEmpty()) {
				System.out.println("No users found! Please create an account first.");
				return null;
			}

			System.out.print("Type your CPF: ");
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
		System.out.println(ANSI_PURPLE_BACKGROUND);
		if (user == null) {
			System.out.println("You need to sign in first.");
			return;
		}
		if (!(user instanceof Organizer)) {
			System.out.println("Only organizers can create events. Please sign in with an organizer account.");
			return;
		}

		System.out.println("Let's create your Event!");
		System.out.println("Type your event details:");

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
				if (eventDate.isBefore(LocalDateTime.now())) {
					throw new PastDateException("Event date must be a future date.");
				}
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date and hour format! Please try again.");
			} catch (PastDateException e) {
				System.out.println("Error! " + e.getMessage());
				eventDate = null;
			}
		}

		boolean isRestrictedForMore18 = false;
		while (true) {
			try {
				System.out.print("Is this event restricted for +18? (y/n): ");
				String answerRestrictedEvent = sc.next();
				if (!answerRestrictedEvent.equalsIgnoreCase("y") && !answerRestrictedEvent.equalsIgnoreCase("n")) {
					throw new InvalidOptionException("Invalid answer! Please enter 'y' or 'n'.");
				}
				isRestrictedForMore18 = answerRestrictedEvent.equalsIgnoreCase("y");
				break;
			} catch (InvalidOptionException e) {
				System.out.println("Error! " + e.getMessage());
			}
		}

		boolean demandsPayment = false;
		double paymentAmount = 0.0;
		while (true) {
			try {
				System.out.print("The event demands payment to participate? (y/n): ");
				String answerDemandsPayment = sc.next();
				if (!answerDemandsPayment.equalsIgnoreCase("y") && !answerDemandsPayment.equalsIgnoreCase("n")) {
					throw new InvalidOptionException("Invalid answer! Please enter 'y' or 'n'.");
				}
				demandsPayment = answerDemandsPayment.equalsIgnoreCase("y");
				if (demandsPayment) {
					System.out.print("Type the payment's value: $ ");
					paymentAmount = sc.nextDouble();
					if (paymentAmount < 0) {
						throw new DomainException("Payment value cannot be negative.");
					}
					sc.nextLine();
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Invalid value! Please enter a valid number.");
				sc.nextLine();
			} catch (DomainException e) {
				System.out.println("Error! " + e.getMessage());
			}
		}

		boolean demandsItems = false;
		while (true) {
			try {
				System.out.print("The event demands that participants bring items to participate? (y/n): ");
				String answerDemandsItems = sc.next();
				if (!answerDemandsItems.equalsIgnoreCase("y") && !answerDemandsItems.equalsIgnoreCase("n")) {
					throw new InvalidOptionException("Invalid answer! Please enter 'y' or 'n'.");
				}
				demandsItems = answerDemandsItems.equalsIgnoreCase("y");
				break;
			} catch (InvalidOptionException e) {
				System.out.println("Error! " + e.getMessage());
			}
		}

		Event newEvent = new Event(eventName, eventPlace, eventDescription, eventDate, isRestrictedForMore18,
				demandsPayment, demandsItems, user, paymentAmount);

		if (demandsItems) {
			int totalItems = 0;
			while (true) {
				try {
					System.out.print("How many items are required for the event? ");
					totalItems = sc.nextInt();
					sc.nextLine();
					if (totalItems <= 0) {
						throw new DomainException("The number of items must be bigger than zero.");
					}
					break;
				} catch (InputMismatchException e) {
					System.out.println("Invalid input. Please input a valid number.");
				} catch (DomainException e) {
					System.out.println("Error! " + e.getMessage());
				}
			}

			for (int i = 0; i < totalItems; i++) {
				int itemQuantity = 0;
				String itemName = "";
				while (true) {
					try {
						System.out.print("Item " + (i + 1) + " name: ");
						itemName = sc.nextLine();
						System.out.print("Item " + (i + 1) + " quantity: ");
						itemQuantity = sc.nextInt();
						sc.nextLine();
						if (itemQuantity <= 0) {
							throw new DomainException("Item quantity must be bigger than zero");
						}
						break;
					} catch (InputMismatchException e) {
						System.out.println("Invalid input! Please enter a valid input.");
						sc.nextLine();
					} catch (DomainException e) {
						System.out.println("Error! " + e.getMessage());
					}
				}
				Item item = new Item(itemName, itemQuantity);
				newEvent.addItemsInTheEvent(item);
			}
		}

		Organizer organizer = (Organizer) user;
		organizer.addCreatedEvent(newEvent);
		eventsCreated.add(newEvent);
		System.out.println("Event created successfully!");
		System.out.println(ANSI_RESET);
	}

	public static boolean processPayment(User user, Event event, Scanner sc) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		Participant participant = (Participant) user;
		List<Payment> payments = event.getPayments();
		if (!event.getDemandsPayment()) {
			System.out.println("This event doesn't requires payment.");
			return true;
		}
		System.out.printf("This event demands a payment value of: $ %.2f%n", event.getPaymentAmount());
		System.out.print("Type the method of payment: ");
		String paymentMethod = sc.nextLine();

		Payment payment = new Payment(participant, event.getPaymentAmount(), LocalDateTime.now(), paymentMethod);

		try {
			payment.isValidPaymentMethod();
			System.out.println("Processing payment...");
			payment.setStatus("Completed");
			System.out.println("A tax of 5% will be increased to the final value.");
			payment.calculateTax(event.getPaymentAmount());
			System.out.printf("Payment made succefully! Value with tax: $ %.2f%n", payment.calculateFinalValue());

			if (payment.getStatus().equals("Completed")) {
				event.addParticipantInTheEvent(user);
				payments.add(payment);
				return true;
			} else {
				System.out.println("Payment not completed. You cannot join the event.");
				return false;
			}

		} catch (DomainException e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			System.out.println(ANSI_RESET);
		}
	}

	public static void JoinAnEvent(User user, Scanner sc) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		if (user == null) {
			throw new NotLoggedException("You need to be logged to join an event.");
		}
		if (!(user instanceof Participant)) {
			throw new DomainException("This account it is not a Participant account.");
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
			throw new InvalidOptionException("Invalid choice. Please select a correct number.");
		}

		Event choosenEvent = eventsCreated.get(choiceChooseEvent - 1);

		if (choosenEvent.getUsersOnTheEvent().contains(user)) {
			System.out.println("You are already participating in this event.");
			return;
		}

		List<Event> userEvents = ((Participant) user).getParticipatedEvents();
		for (Event event : userEvents) {
			if (event.getEventDateTime().equals(choosenEvent.getEventDateTime())) {
				System.out.println("You are already participating in another event at the same time.");
				return;
			}
		}

		if (!choosenEvent.checkIfTheParticipantHasEnoughAge(user)) {
			throw new DomainException(
					"User " + user.getName() + " cannot participate in this event because it's for +18 years");
		}

		if (choosenEvent.getDemandsPayment()) {
			boolean sucess = processPayment(user, choosenEvent, sc);
			if (!sucess) {
				throw new DomainException("Failed processing your payment!");
			}
		}
		if (choosenEvent.getDemandsItems()) {
			System.out.println("This event requires you to bring an item to participate.");
			System.out.println("Here is the items the event need: ");
			List<Item> items = choosenEvent.getEventItems();
			if (items.isEmpty()) {
				throw new DomainException("There are no items listed for this event.");
			}

			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				System.out.println((i + 1) + " - Name: " + item.getName() + ", Quantity needed: " + item.getQuantity());
			}
			int choosenItem = -1;
			int quantityChoosenItem = 0;

			while (true) {
				try {
					System.out.print("Choose the number of the item you want to bring: ");
					choosenItem = sc.nextInt();
					sc.nextLine();
					if (choosenItem < 1 || choosenItem > items.size()) {
						throw new DomainException("Invalid item number. Please select a valid number");
					}
					Item selectedItem = items.get(choosenItem - 1);
					System.out.print("Enter the quantity of the choosen item: ");
					quantityChoosenItem = sc.nextInt();
					if (quantityChoosenItem <= 0 || quantityChoosenItem > selectedItem.getQuantity()) {
						throw new DomainException(
								"Invalid quantity. Please enter a number betweetn 1 and " + selectedItem.getQuantity());
					}
					selectedItem.setQuantity(selectedItem.getQuantity() - quantityChoosenItem);
					System.out.println(
							"You have choose to bring " + quantityChoosenItem + " of " + selectedItem.getName());
					break;
				} catch (InputMismatchException e) {
					System.out.println("Invalid input! Please enter a valid input.");
				} catch (DomainException e) {
					System.out.println("Error! " + e.getMessage());
				}
			}
		}
		if (choosenEvent.addParticipantInTheEvent(user)) {
			System.out.println("Sucess!");
		}
		System.out.println(ANSI_RESET);

	}

	public static void showUserEvents(User user) {
		System.out.println(ANSI_PURPLE_BACKGROUND);
		if (user == null) {
			throw new NotLoggedException("You need to be logged to see your events.");
		}

		if (user instanceof Organizer) {
			Organizer organizer = (Organizer) user;
			List<Event> createdEvents = organizer.getCreatedEvents();

			if (createdEvents.isEmpty()) {
				System.out.println("You have not created any events yet.");
			} else {
				System.out.println("=== Events created by you: ===");
				for (Event event : createdEvents) {
					System.out.println(event);
				}
			}
		} else if (user instanceof Participant) {
			Participant participant = (Participant) user;
			List<Event> participatedEvents = participant.getParticipatedEvents();

			if (participatedEvents.isEmpty()) {
				System.out.println("You have not participated any events yet.");
			} else {
				System.out.println("Events participated by you:");
				for (Event event : participatedEvents) {
					System.out.println(event);
				}
			}
		} else {
			System.out.println("Invalid user type.");
		}
		System.out.println(ANSI_RESET);
	}
}