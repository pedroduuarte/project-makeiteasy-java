package model.entities;

import java.time.LocalDate;

import java.time.Period;

public class User {

	private String name;
	private String cpf;
	private String email;
	private LocalDate birthDate;
	private String phoneNumber;
	private String address;

	public User(String name, String cpf, String email, LocalDate birthDate, String phoneNumber, String address) {
		this.name = name;
		this.cpf = cpf;
		this.email = email;
		this.birthDate = birthDate;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getUserAge() {
		LocalDate currentDate = LocalDate.now();
		int age = Period.between(birthDate, currentDate).getYears();
		return age;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Name: ");
		sb.append(name + "\n");
		sb.append("Email: ");
		sb.append(email + "\n");
		sb.append("Telefone: ");
		sb.append(phoneNumber + "\n");

		return sb.toString();
	}
}