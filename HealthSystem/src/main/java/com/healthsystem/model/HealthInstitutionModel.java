package com.healthsystem.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class HealthInstitutionModel {
	
	@NotBlank(message = "id cannot be null")
	private String idHealthInstitution;
	
	@NotBlank(message = "identityCode cannot be null")
	@Size(message="identityCode is 20 max characters", max = 20)
	private String identityCode;
	
	@NotBlank(message = "name cannot be null")
	@Size(message="name is 100 max characters", max = 100)
	private String name;

	@Size(message="postalCode is 8 max characters", max = 8)
	private String postalCode;

	@NotBlank(message = "country cannot be null")
	@Size(message="country is 3 max characters", max = 3)
	private String country;

	@Size(message="state is 100 max characters", max = 100)
	@NotBlank(message = "state cannot be null")
	private String state;
	
	@Size(message="city is 100 max characters", max = 100)
	@NotBlank(message = "city cannot be null")
	private String city;
	
	@Size(message="street is 100 max characters", max = 100)
	@NotBlank(message = "street cannot be null")
	private String street;
	
	@Size(message="neighborhood is 100 max characters", max = 100)
	@NotBlank(message = "neighborhood cannot be null")
	private String neighborhood;
	
	@Size(message="number is 10 max characters", max = 10)
	@NotBlank(message = "number cannot be null")
	private String number;
	
	@Size(message="photo is 100 max characters", max = 100)
	private String photo;
	
	@Size(message="telephone is 20 max characters", max = 20)
	private String telephone;
	
	private double latitute;
	private double longitute;

	
	public static class HealthInstitutionModelBuilder {

		private String idHealthInstitution;
		private String identityCode;
		private String name;
		private String postalCode;
		private String country;
		private String state;
		private String city;
		private String street;
		private String neighborhood;
		private String number;
		private String photo;
		private String telephone;
		private double latitute;
		private double longitute;

		
		public HealthInstitutionModelBuilder(String idHealthInstitution, String name){
			this.idHealthInstitution = idHealthInstitution;
			this.name = name;
			
		}
		
		public HealthInstitutionModelBuilder identityCode(String identityCode){
			this.identityCode = identityCode;
			return this;
		}
		
		public HealthInstitutionModelBuilder postalCode(String postalCode){
			this.postalCode = postalCode;
			return this;
		}
		
		public HealthInstitutionModelBuilder country(String country){
			this.country = country;
			return this;
		}
		

		public HealthInstitutionModelBuilder state(String state){
			this.state = state;
			return this;
		}
		
		
		public HealthInstitutionModelBuilder city(String city){
			this.city = city;
			return this;
		}
		
		public HealthInstitutionModelBuilder street(String street){
			this.street = street;
			return this;
		}
		
		public HealthInstitutionModelBuilder neighborhood(String neighborhood){
			this.neighborhood = neighborhood;
			return this;
		}
		

		public HealthInstitutionModelBuilder number(String number){
			this.number = number;
			return this;
		}
		
		public HealthInstitutionModelBuilder photo(String photo){
			this.photo = photo;
			return this;
		}
		
		public HealthInstitutionModelBuilder telephone(String telephone){
			this.telephone = telephone;
			return this;
		}
		
		public HealthInstitutionModelBuilder latitute(double latitute){
			this.latitute = latitute;
			return this;
		}
		
		public HealthInstitutionModelBuilder longitute(double longitute){
			this.longitute = longitute;
			return this;
		}
	
	
		
		public HealthInstitutionModel build(){
			return new HealthInstitutionModel(this);
		}
		
	}

	public HealthInstitutionModel(HealthInstitutionModelBuilder builder) {
		this.idHealthInstitution = builder.idHealthInstitution;
		this.identityCode =  builder.identityCode;
		this.name =  builder.name;
		this.postalCode =  builder.postalCode;
		this.country = builder.country;
		this.state =  builder.state;
		this.city =  builder.city;
		this.street =  builder.street;
		this.neighborhood =  builder.neighborhood;
		this.number =  builder.number;
		this.photo =  builder.photo;
		this.telephone = builder.telephone;
		this.latitute = builder.latitute;
		this.longitute = builder.longitute;
	}


	public String getIdHealthInstitution() {
		return idHealthInstitution;
	}

	public String getIdentityCode() {
		return identityCode;
	}

	public String getName() {
		return name;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	public String getState() {
		return state;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public String getNumber() {
		return number;
	}

	public String getPhoto() {
		return photo;
	}

	public String getTelephone() {
		return telephone;
	}


	public double getLatitute() {
		return latitute;
	}


	public double getLongitute() {
		return longitute;
	}


	@Override
	public String toString() {
		return "HealthInstitutionModel [idHealthInstitution=" + idHealthInstitution + ", identityCode=" + identityCode
				+ ", name=" + name + ", postalCode=" + postalCode + ", country=" + country + ", state=" + state
				+ ", city=" + city + ", street=" + street + ", neighborhood=" + neighborhood + ", number=" + number
				+ ", photo=" + photo + ", telephone=" + telephone + ", latitute=" + latitute + ", longitute="
				+ longitute + "]";
	}
	 
	
	
	
}
