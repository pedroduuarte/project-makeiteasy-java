package model.exceptions;

public class NotLoggedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NotLoggedException(String msg) {
		super(msg);
	}
}