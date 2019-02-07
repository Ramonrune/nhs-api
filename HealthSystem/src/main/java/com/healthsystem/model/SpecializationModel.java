package com.healthsystem.model;

public class SpecializationModel {

	
	private String idSpecialization;
	private String name;
	private String country;
	
	public String getIdSpecialization() {
		return idSpecialization;
	}
	public void setIdSpecialization(String idSpecialization) {
		this.idSpecialization = idSpecialization;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSpecialization == null) ? 0 : idSpecialization.hashCode());
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
		SpecializationModel other = (SpecializationModel) obj;
		if (idSpecialization == null) {
			if (other.idSpecialization != null)
				return false;
		} else if (!idSpecialization.equals(other.idSpecialization))
			return false;
		return true;
	}
	
	
	
	
	
	
}
