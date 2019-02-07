package com.healthsystem.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PhysicianWorksOnHealthInstitutionModel {

	private String idUser;
	private String idPhysician;
	private String name;
	private String city;
	private String country;
	private String state;
	private String practiceDocument;
	private String photo;
	private List<SpecializationModel> specializationList = new ArrayList<>();
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getIdPhysician() {
		return idPhysician;
	}
	public void setIdPhysician(String idPhysician) {
		this.idPhysician = idPhysician;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPracticeDocument() {
		return practiceDocument;
	}
	public void setPracticeDocument(String practiceDocument) {
		this.practiceDocument = practiceDocument;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public List<SpecializationModel> getSpecializationList() {
		return specializationList;
	}
	public void setSpecializationList(List<SpecializationModel> specializationList) {
		this.specializationList = specializationList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPhysician == null) ? 0 : idPhysician.hashCode());
		result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhysicianWorksOnHealthInstitutionModel other = (PhysicianWorksOnHealthInstitutionModel) obj;
		if (idPhysician == null) {
			if (other.idPhysician != null)
				return false;
		} else if (!idPhysician.equals(other.idPhysician))
			return false;
		if (idUser == null) {
			if (other.idUser != null)
				return false;
		} else if (!idUser.equals(other.idUser))
			return false;
		return true;
	}
	
	
	
	
	
}
