package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class DiagnosisModel {

	@NotBlank(message = "idDiagnosis cannot be null")
	private String idDiagnosis;
	
	private String dateDiagnosis;
	private String annotation;
	
	
	@NotBlank(message = "idPatient cannot be null")
	private String idPatient;
	
	@NotBlank(message = "idPhysician cannot be null")
	private String idPhysician;
	
	@NotBlank(message = "idHealthInstitution cannot be null")
	private String idHealthInstitution;
	
	private String healthInstitutionName;
	private String physicianName;
	private String physicianPracticeNumber;
	private String physicianCountry;
	private String physicianPhoto;
	private String healthInstitutionPhoto;
	private double healthInstitutionLatitute;
	private double healthInstitutionLongitute;
	
	
	private String patientName;
	private String patientPhoto;
	
	
	public String getIdDiagnosis() {
		return idDiagnosis;
	}
	public void setIdDiagnosis(String idDiagnosis) {
		this.idDiagnosis = idDiagnosis;
	}
	public String getDateDiagnosis() {
		return dateDiagnosis;
	}
	public void setDateDiagnosis(String dateDiagnosis) {
		this.dateDiagnosis = dateDiagnosis;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public String getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(String idPatient) {
		this.idPatient = idPatient;
	}
	public String getIdPhysician() {
		return idPhysician;
	}
	public void setIdPhysician(String idPhysician) {
		this.idPhysician = idPhysician;
	}
	public String getIdHealthInstitution() {
		return idHealthInstitution;
	}
	public void setIdHealthInstitution(String idHealthInstitution) {
		this.idHealthInstitution = idHealthInstitution;
	}
	public String getHealthInstitutionName() {
		return healthInstitutionName;
	}
	public void setHealthInstitutionName(String healthInstitutionName) {
		this.healthInstitutionName = healthInstitutionName;
	}
	public String getPhysicianName() {
		return physicianName;
	}
	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}
	public String getPhysicianPracticeNumber() {
		return physicianPracticeNumber;
	}
	public void setPhysicianPracticeNumber(String physicianPracticeNumber) {
		this.physicianPracticeNumber = physicianPracticeNumber;
	}
	public String getPhysicianCountry() {
		return physicianCountry;
	}
	public void setPhysicianCountry(String physicianCountry) {
		this.physicianCountry = physicianCountry;
	}
	public String getPhysicianPhoto() {
		return physicianPhoto;
	}
	public void setPhysicianPhoto(String physicianPhoto) {
		this.physicianPhoto = physicianPhoto;
	}
	public String getHealthInstitutionPhoto() {
		return healthInstitutionPhoto;
	}
	public void setHealthInstitutionPhoto(String healthInstitutionPhoto) {
		this.healthInstitutionPhoto = healthInstitutionPhoto;
	}
	public double getHealthInstitutionLatitute() {
		return healthInstitutionLatitute;
	}
	public void setHealthInstitutionLatitute(double healthInstitutionLatitute) {
		this.healthInstitutionLatitute = healthInstitutionLatitute;
	}
	public double getHealthInstitutionLongitute() {
		return healthInstitutionLongitute;
	}
	public void setHealthInstitutionLongitute(double healthInstitutionLongitute) {
		this.healthInstitutionLongitute = healthInstitutionLongitute;
	}
	
	
	
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientPhoto() {
		return patientPhoto;
	}
	public void setPatientPhoto(String patientPhoto) {
		this.patientPhoto = patientPhoto;
	}
	@Override
	public String toString() {
		return "DiagnosisModel [idDiagnosis=" + idDiagnosis + ", dateDiagnosis=" + dateDiagnosis + ", annotation="
				+ annotation + ", idPatient=" + idPatient + ", idPhysician=" + idPhysician + ", idHealthInstitution="
				+ idHealthInstitution + ", healthInstitutionName=" + healthInstitutionName + ", physicianName="
				+ physicianName + ", physicianPracticeNumber=" + physicianPracticeNumber + ", physicianCountry="
				+ physicianCountry + ", physicianPhoto=" + physicianPhoto + ", healthInstitutionPhoto="
				+ healthInstitutionPhoto + ", healthInstitutionLatitute=" + healthInstitutionLatitute
				+ ", healthInstitutionLongitute=" + healthInstitutionLongitute + "]";
	}
	
	
	
	
	
	
	
	
	
	
}
