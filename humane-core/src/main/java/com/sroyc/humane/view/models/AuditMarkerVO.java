package com.sroyc.humane.view.models;

import java.time.LocalDateTime;

import com.sroyc.humane.data.model.AuditMarker;

public abstract class AuditMarkerVO extends CreationAuditAttributesVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -914059342146228438L;

	private LocalDateTime updatedOn;
	private String modifiedBy;

	public AuditMarkerVO() {
		super();
	}

	public AuditMarkerVO(AuditMarker marker) {
		super(marker);
		this.updatedOn = marker.getModifiedOn();
		this.modifiedBy = marker.getModifiedBy();
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
