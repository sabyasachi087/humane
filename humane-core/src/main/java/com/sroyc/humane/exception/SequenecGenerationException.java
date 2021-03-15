package com.sroyc.humane.exception;

public class SequenecGenerationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4690260163604525800L;

	public SequenecGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SequenecGenerationException(String message) {
		super(message);
	}

	public SequenecGenerationException(Throwable cause) {
		super(cause);
	}

}
