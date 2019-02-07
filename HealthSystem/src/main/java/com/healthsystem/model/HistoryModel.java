package com.healthsystem.model;

public class HistoryModel {
	private String id;
	private int type;
	private String idPatient;
	private String anotation;
	private String date;
	private String idPhysician;
	private String idHealthInstitution;
	private String physicianName;
	private String physicianPhoto;
	private String healthInstitutionName;
	private String healthInstitutionPhoto;
	private double latitute;
	private double longitute;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(String idPatient) {
		this.idPatient = idPatient;
	}
	public String getAnotation() {
		return anotation;
	}
	public void setAnotation(String anotation) {
		this.anotation = anotation;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public String getPhysicianName() {
		return physicianName;
	}
	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}
	public String getPhysicianPhoto() {
		return physicianPhoto;
	}
	public void setPhysicianPhoto(String physicianPhoto) {
		this.physicianPhoto = physicianPhoto;
	}
	public String getHealthInstitutionName() {
		return healthInstitutionName;
	}
	public void setHealthInstitutionName(String healthInstitutionName) {
		this.healthInstitutionName = healthInstitutionName;
	}
	public String getHealthInstitutionPhoto() {
		return healthInstitutionPhoto;
	}
	public void setHealthInstitutionPhoto(String healthInstitutionPhoto) {
		this.healthInstitutionPhoto = healthInstitutionPhoto;
	}
	public double getLatitute() {
		return latitute;
	}
	public void setLatitute(double latitute) {
		this.latitute = latitute;
	}
	public double getLongitute() {
		return longitute;
	}
	public void setLongitute(double longitute) {
		this.longitute = longitute;
	}

}
