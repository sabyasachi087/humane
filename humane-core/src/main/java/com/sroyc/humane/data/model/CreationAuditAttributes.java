package com.sroyc.humane.data.model;

import java.time.LocalDateTime;

public interface CreationAuditAttributes extends HumaneEntity {

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public LocalDateTime getCreatedOn();

	public void setCreatedOn(LocalDateTime createdOn);

}
