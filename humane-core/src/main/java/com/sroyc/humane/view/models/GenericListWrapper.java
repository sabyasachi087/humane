package com.sroyc.humane.view.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericListWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -453302727874310203L;

	private List<PhoneNumberVO> phones;
	private List<AddressVO> addresses;

	public List<PhoneNumberVO> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneNumberVO> phones) {
		this.phones = phones;
	}

	public List<AddressVO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressVO> addresses) {
		this.addresses = addresses;
	}

}
