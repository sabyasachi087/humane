package com.sroyc.humane.exception;

public class HumaneEntityViolationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -194011927650627315L;

	public HumaneEntityViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public HumaneEntityViolationException(String message) {
		super(message);
	}

	public HumaneEntityViolationException(Throwable cause) {
		super(cause);
	}

}
