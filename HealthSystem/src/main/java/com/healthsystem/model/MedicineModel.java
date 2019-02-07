package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class MedicineModel {

	@NotBlank(message = "idMedicine cannot be null")
	private String idMedicine;
	
	@NotBlank(message = "name cannot be null")
	private String name;
	
	@NotBlank(message = "language cannot be null")
	private String language;
	
	@NotBlank(message = "status cannot be null")
	private String status;
	
	@NotBlank(message = "country cannot be null")
	private String country;
	
	public String getIdMedicine() {
		return idMedicine;
	}
	public void setIdMedicine(String idMedicine) {
		this.idMedicine = idMedicine;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	@Override
	public String toString() {
		return "MedicineModel [idMedicine=" + idMedicine + ", name=" + name + ", language=" + language + ", status="
				+ status + ", country=" + country + "]";
	}
	
	
	
	
}
