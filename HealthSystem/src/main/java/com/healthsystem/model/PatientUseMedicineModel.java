package com.healthsystem.model;

import org.hibernate.validator.constraints.NotBlank;

public class PatientUseMedicineModel {

	@NotBlank(message = "idPatientUseMedicine cannot be null")
	private String idPatientUseMedicine;
	
	@NotBlank(message = "idPatient cannot be null")
	private String idPatient;
	
	@NotBlank(message = "idMedicine cannot be null")
	private String idMedicine;
	
	public String getIdPatientUseMedicine() {
		return idPatientUseMedicine;
	}
	public void setIdPatientUseMedicine(String idPatientUseMedicine) {
		this.idPatientUseMedicine = idPatientUseMedicine;
	}
	public String getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(String idPatient) {
		this.idPatient = idPatient;
	}
	public String getIdMedicine() {
		return idMedicine;
	}
	public void setIdMedicine(String idMedicine) {
		this.idMedicine = idMedicine;
	}
	@Override
	public String toString() {
		return "PatientUseMedicineModel [idPatientUseMedicine=" + idPatientUseMedicine + ", idPatient=" + idPatient
				+ ", idMedicine=" + idMedicine + "]";
	}
	
	
	
}
