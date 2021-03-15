package com.sroyc.humane.services;

public interface HumaneUserContext {

	/**
	 * Must return logged in user identifier. It can be user name, email, name or id
	 */
	String loggedInUser();

}
