package com.sroyc.humane.data.model;

import java.time.LocalDateTime;

public interface AuditMarker extends CreationAuditAttributes {

	/**
	 * 
	 */

	public LocalDateTime getModifiedOn();

	public void setModifiedOn(LocalDateTime updatedOn);

	public String getModifiedBy();

	public void setModifiedBy(String modifiedBy);

}
