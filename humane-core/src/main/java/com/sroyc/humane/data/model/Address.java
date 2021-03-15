package com.sroyc.humane.data.model;

import java.io.Serializable;

public interface Address extends Serializable {

	public String getId();

	public void setId(String id);

	public String getTag();

	public void setTag(String tag);

	public String getLine1();

	public void setLine1(String line1);

	public String getLine2();

	public void setLine2(String line2);

	public String getLandmark();

	public void setLandmark(String landmark);

	public String getCodeOrPin();

	public void setCodeOrPin(String codeOrPin);

	public String getCity();

	public void setCity(String city);

	public String getState();

	public void setState(String state);

	public String getCountry();

	public void setCountry(String country);

	public boolean isPrimary();

	public void setPrimary(boolean primary);

}
