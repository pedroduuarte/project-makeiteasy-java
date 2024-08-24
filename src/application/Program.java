package application;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import model.entities.User;
import model.exceptions.DomainException;
import model.exceptions.NotLoggedException;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Locale.setDefault(Locale.US);

		User loggedUser = null;

		System.out.println("Welcome to MakeItEasy System!");
		System.out.println("TYPE WHAT YOU WISH TO DO");

		while (true) {
			try {
				UI.showMenu();
				System.out.print("Type your option: ");
				int option = sc.nextInt();
				if (option < 1 || option > 5) {
					throw new InputMismatchException("Invalid input! Please inout a number between 1 and 5!");
				}
				sc.nextLine();

				switch (option) {
				case 1: {
					loggedUser = UI.login(sc);
					break;
				}

				case 3: {
					UI.createAnEvent(sc, loggedUser);
					break;
				}
				case 4: {
					UI.JoinAnEvent(loggedUser, sc);
					break;
				}

				case 5: {
					System.out.println("Exiting...");
					sc.close();
					return;
				}
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input! Please input a number between 1 and 5!");
				sc.nextLine();
			}
		}
	}

}