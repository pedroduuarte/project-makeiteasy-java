package model.exceptions;

public class InvalidOptionException extends RuntimeException {
	
 static final long serialVersionUID = 1L;
 
	public InvalidOptionException(String msg) {
		super(msg);
	}

}
