package model.exceptions;

public class CpfException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CpfException(String msg) {
		super(msg);
	}
}
