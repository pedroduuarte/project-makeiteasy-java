package model.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import model.exceptions.DomainException;

public class Payment implements SystemTax {

	private double value;
	private LocalDateTime instantOfPayment;
	private String typeOfPayment;
	private String status;

	private static final List<String> validPaymentsMethods = Arrays.asList("Credit Card", "Debit Card", "PIX");

	public Payment(double value, LocalDateTime instantOfPayment, String typeOfPayment) {
		this.value = value;
		this.instantOfPayment = instantOfPayment;
		this.typeOfPayment = typeOfPayment;
		this.status = "Pending";
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public LocalDateTime getInstantOfPayment() {
		return instantOfPayment;
	}

	public void setInstantOfPayment(LocalDateTime instantOfPayment) {
		this.instantOfPayment = instantOfPayment;
	}

	public String getTypeOfPayment() {
		return typeOfPayment;
	}

	public void setTypeOfPayment(String typeOfPayment) {
		this.typeOfPayment = typeOfPayment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isValidPaymentMethod() throws DomainException {
		if (validPaymentsMethods.contains(this.typeOfPayment)) {
			throw new DomainException("Invalid payment method: " + this.typeOfPayment);
		}
		return true;
	}

	@Override
	public double calculateTax(double amount) {
		return amount * 0.05;
	}

	public double calculateFinalValue() {
		return this.value + calculateTax(value);
	}

}