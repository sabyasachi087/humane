package com.sroyc.humane.view.models;

import com.sroyc.humane.data.model.Address;

public class AddressVO implements PrimaryRecordAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6646232521221294468L;

	private String id;
	private String tag;
	private String line1;
	private String line2;
	private String landmark;
	private String codeOrPin;
	private String city;
	private String state;
	private String country;
	private boolean primary;

	public AddressVO(Address address) {
		this.id = address.getId();
		this.tag = address.getTag();
		this.line1 = address.getLine1();
		this.line2 = address.getLine2();
		this.landmark = address.getLandmark();
		this.codeOrPin = address.getCodeOrPin();
		this.city = address.getCity();
		this.state = address.getState();
		this.country = address.getCountry();
		this.primary = address.isPrimary();
	}

	public AddressVO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCodeOrPin() {
		return codeOrPin;
	}

	public void setCodeOrPin(String codeOrPin) {
		this.codeOrPin = codeOrPin;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

}
