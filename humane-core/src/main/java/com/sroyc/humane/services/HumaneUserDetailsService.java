package com.sroyc.humane.services;

import java.util.Map;

import com.sroyc.humane.exception.HumaneEntityViolationException;
import com.sroyc.humane.view.models.HumaneUserDetails;
import com.sroyc.humane.view.models.HumaneUserMasterVO;

public interface HumaneUserDetailsService {

	/** Load user by email or user name */
	public HumaneUserDetails loadUser(String emailOrUsername);

	/**
	 * Fetch or create user. Create user only if automatic user creation setting is
	 * enabled
	 * 
	 * @return {@linkplain HumaneUserMasterVO}
	 * @throws HumaneEntityViolationException
	 */
	public HumaneUserDetails loadUser(Map<String, String> attributes) throws HumaneEntityViolationException;

}
