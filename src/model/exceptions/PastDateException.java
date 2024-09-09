package model.exceptions;

public class PastDateException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PastDateException(String msg) {
		super(msg);
	}

}
