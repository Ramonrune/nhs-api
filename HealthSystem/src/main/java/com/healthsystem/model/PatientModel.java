package com.healthsystem.model;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class PatientModel {

	
	
	@NotBlank(message = "idPatient cannot be null")
	private String idPatient;
	
	@Size(message="bloodType is 2 max characters", max = 2)
	private String bloodType;
	
	@Size(message="color is 2 max characters", max = 2)
	private String color;
	
	@Size(message="fatherName is 100 max characters", max = 100)
	private String fatherName;
	
	@Size(message="motherName is 100 max characters", max = 100)
	private String motherName;
	
	private double weight;
	private double height;
	
	
	@NotBlank(message = "status cannot be null")
	private String status;
	
	
	@NotBlank(message = "idUser cannot be null")
	private String idUser;
	
	
	
	
	
	public String getIdPatient() {
		return idPatient;
	}
	public void setIdPatient(String idPatient) {
		this.idPatient = idPatient;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "PatientModel [idPatient=" + idPatient + ", bloodType=" + bloodType + ", color=" + color
				+ ", fatherName=" + fatherName + ", motherName=" + motherName + ", weight=" + weight + ", height="
				+ height + ", status=" + status + ", idUser=" + idUser + "]";
	}
	
	
	
	
	
}
