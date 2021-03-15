package com.sroyc.humane.view.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.sroyc.humane.data.model.CreationAuditAttributes;

public abstract class CreationAuditAttributesVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3495448173360304820L;

	private String createdBy;
	private LocalDateTime createdOn;

	public CreationAuditAttributesVO() {
		super();
	}

	public CreationAuditAttributesVO(CreationAuditAttributes caa) {
		super();
		this.createdBy = caa.getCreatedBy();
		this.createdOn = caa.getCreatedOn();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

}
