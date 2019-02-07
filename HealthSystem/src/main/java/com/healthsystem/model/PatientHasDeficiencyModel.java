package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class PatientHasDeficiencyModel {

	@NotBlank(message = "idPatientHasDeficiency cannot be null")
	private String idPatientHasDeficiency;
	
	@NotBlank(message = "idPatient cannot be null")
	private String idPatient;
	
	@NotBlank(message = "idDeficiency cannot be null")
	private String idDeficiency;
	
	private String anotation;
	public String getIdPatientHasDeficiency() {
		return idPatientHasDeficiency;
	}
	public void setIdPatientHasDeficiency(String idPatientHasDeficiency) {
		this.idPatientHasDeficiency = idPatientHasDeficiency;
	}
	public String getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(String idPatient) {
		this.idPatient = idPatient;
	}
	public String getIdDeficiency() {
		return idDeficiency;
	}
	public void setIdDeficiency(String idDeficiency) {
		this.idDeficiency = idDeficiency;
	}
	public String getAnotation() {
		return anotation;
	}
	public void setAnotation(String anotation) {
		this.anotation = anotation;
	}
	
	
	
}
