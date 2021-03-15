package com.sroyc.humane.exception;

public class HumaneValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9141963104538635127L;

	public HumaneValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public HumaneValidationException(String message) {
		super(message);
	}

	public HumaneValidationException(Throwable cause) {
		super(cause);
	}

}
