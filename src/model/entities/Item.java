package model.entities;

public class Item {

	private String name;
	private int quantity;
	private User designatedUser;
	private Event event;

	public Item(String name, int quantity, User designatedUser, Event event) {
		this.name = name;
		this.quantity = quantity;
		this.designatedUser = designatedUser;
		this.event = event;
	}
	
	public Item(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public User getDesignatedUser() {
		return designatedUser;
	}

	public void setDesignatedUser(User designatedUser) {
		this.designatedUser = designatedUser;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}