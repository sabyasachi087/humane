package com.sroyc.humane.view.models;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericPageableResponseItems extends GenericResponseItems<Collection<?>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3497204978724517200L;

	private int page;
	private boolean hasMoreItems;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isHasMoreItems() {
		return hasMoreItems;
	}

	public void setHasMoreItems(boolean hasMoreItems) {
		this.hasMoreItems = hasMoreItems;
	}

}
