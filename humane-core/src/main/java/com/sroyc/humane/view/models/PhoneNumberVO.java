package com.sroyc.humane.view.models;

import com.sroyc.humane.data.model.Phone;

public class PhoneNumberVO implements PrimaryRecordAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1838104749083722991L;

	private String countryCode;
	private String phoneNumber;
	private boolean primary;

	public PhoneNumberVO(Phone phone) {
		super();
		this.countryCode = phone.getCountryCode();
		this.phoneNumber = phone.getPhoneNumber();
		this.primary = phone.isPrimary();
	}

	public PhoneNumberVO() {
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

}
