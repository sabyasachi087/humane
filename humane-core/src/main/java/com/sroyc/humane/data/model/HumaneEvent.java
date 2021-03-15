package com.sroyc.humane.data.model;

import java.io.Serializable;

public class HumaneEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6094151453664307562L;

	private EventType event;
	private Serializable data;
	private Class<?> dataType;

	public EventType getEvent() {
		return event;
	}

	public void setEvent(EventType event) {
		this.event = event;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public enum EventType {
		USER_CREATED;
	}

	public <T> T cast(Class<T> clazz) {
		return clazz.cast(this.data);
	}

}
