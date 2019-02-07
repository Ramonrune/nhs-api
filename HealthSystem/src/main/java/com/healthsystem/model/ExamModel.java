package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class ExamModel {

	@NotBlank(message = "idExam cannot be null")
	private String idExam;
	private String dateExam;
	private String anotation;
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
	private int attachmentSize;
	
	
	
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
	public String getIdExam() {
		return idExam;
	}
	public void setIdExam(String idExam) {
		this.idExam = idExam;
	}
	public String getDateExam() {
		return dateExam;
	}
	public void setDateExam(String dateExam) {
		this.dateExam = dateExam;
	}
	public String getAnotation() {
		return anotation;
	}
	public void setAnotation(String anotation) {
		this.anotation = anotation;
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
	public int getAttachmentSize() {
		return attachmentSize;
	}
	public void setAttachmentSize(int attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	@Override
	public String toString() {
		return "ExamModel [idExam=" + idExam + ", dateExam=" + dateExam + ", anotation=" + anotation + ", idPatient="
				+ idPatient + ", idPhysician=" + idPhysician + ", idHealthInstitution=" + idHealthInstitution
				+ ", healthInstitutionName=" + healthInstitutionName + ", physicianName=" + physicianName
				+ ", physicianPracticeNumber=" + physicianPracticeNumber + ", physicianCountry=" + physicianCountry
				+ ", physicianPhoto=" + physicianPhoto + ", healthInstitutionPhoto=" + healthInstitutionPhoto
				+ ", healthInstitutionLatitute=" + healthInstitutionLatitute + ", healthInstitutionLongitute="
				+ healthInstitutionLongitute + ", attachmentSize=" + attachmentSize + "]";
	}
	
	
	
	
}
