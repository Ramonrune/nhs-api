package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class PhysicianAttendanceModel {
	
	@NotBlank(message = "idPhysicianAttendance cannot be null")
	private String idPhysicianAttendance;
	@NotBlank(message = "dateAttendance cannot be null")
	private String dateAttendance;
	@NotBlank(message = "idPatient cannot be null")
	private String idPatient;
	@NotBlank(message = "idPhysician cannot be null")
	private String idPhysician;
	@NotBlank(message = "idHealthInstitution cannot be null")
	private String idHealthInstitution;
	
	private String idUser;
	private String name;
	private String photo;
	
	
	public String getIdPhysicianAttendance() {
		return idPhysicianAttendance;
	}
	public void setIdPhysicianAttendance(String idPhysicianAttendance) {
		this.idPhysicianAttendance = idPhysicianAttendance;
	}
	public String getDateAttendance() {
		return dateAttendance;
	}
	public void setDateAttendance(String dateAttendance) {
		this.dateAttendance = dateAttendance;
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
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	
	
	
	
}
