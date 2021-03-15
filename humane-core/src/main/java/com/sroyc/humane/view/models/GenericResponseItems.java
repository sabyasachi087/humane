package com.sroyc.humane.view.models;

import java.io.Serializable;
import java.util.Collection;

public class GenericResponseItems<E extends Collection<?>> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2436956014500918004L;

	private E items;

	public E getItems() {
		return items;
	}

	public void setItems(E items) {
		this.items = items;
	}

}
