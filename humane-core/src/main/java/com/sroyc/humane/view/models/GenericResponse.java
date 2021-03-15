package com.sroyc.humane.view.models;

import java.io.Serializable;

public class GenericResponse<T extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3757564008542841084L;

	private T data;
	private String message;
	private ResponseStatus status;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public enum ResponseStatus {
		SUCCESS, ERROR;
	}

}
