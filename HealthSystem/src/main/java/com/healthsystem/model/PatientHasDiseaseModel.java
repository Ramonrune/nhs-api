package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class PatientHasDiseaseModel {

	
	@NotBlank(message = "idPatientHasDisease cannot be null")
	private String idPatientHasDisease;
	
	@NotBlank(message = "idPatient cannot be null")
	private String idPatient;
	
	@NotBlank(message = "idDisease cannot be null")
	private String idDisease;
	
	
	private String anotation;
	
	
	
	public String getIdPatientHasDisease() {
		return idPatientHasDisease;
	}
	public void setIdPatientHasDisease(String idPatientHasDisease) {
		this.idPatientHasDisease = idPatientHasDisease;
	}
	public String getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(String idPatient) {
		this.idPatient = idPatient;
	}
	public String getIdDisease() {
		return idDisease;
	}
	public void setIdDisease(String idDisease) {
		this.idDisease = idDisease;
	}
	public String getAnotation() {
		return anotation;
	}
	public void setAnotation(String anotation) {
		this.anotation = anotation;
	}
	@Override
	public String toString() {
		return "PatientHasDiseaseModel [idPatientHasDisease=" + idPatientHasDisease + ", idPatient=" + idPatient
				+ ", idDisease=" + idDisease + ", anotation=" + anotation + "]";
	}
	
	
	
}
