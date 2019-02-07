package com.healthsystem.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class PhysicianModel {
	
	@NotBlank(message = "id physiciancannot be null")
	private String idPhysician;
	
	@NotBlank(message = "id user cannot be null")
	private String idUser;
	
	@NotBlank(message = "practice document cannot be null")
	@Size(message="practice document is 20 max characters", max = 20)
	private String practiceDocument;
	
	public String getIdPhysician() {
		return idPhysician;
	}
	public void setIdPhysician(String idPhysician) {
		this.idPhysician = idPhysician;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getPracticeDocument() {
		return practiceDocument;
	}
	public void setPracticeDocument(String practiceDocument) {
		this.practiceDocument = practiceDocument;
	}
	@Override
	public String toString() {
		return "PhysicianModel [idPhysician=" + idPhysician + ", idUser=" + idUser + ", practiceDocument="
				+ practiceDocument + "]";
	}
	
	
	
	
}
