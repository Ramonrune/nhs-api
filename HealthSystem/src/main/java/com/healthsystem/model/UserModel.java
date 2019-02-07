package com.healthsystem.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UserModel {

	@NotBlank(message = "id cannot be null")
	private String id;
	
	@NotBlank(message = "login cannot be null")
	@Size(message="login is 100 max characters", max = 100)
	private String login;
	
	@NotBlank(message = "password cannot be null")
	@Size(message="password is 64 max characters", max = 64)
	private String password;
	
	@Size(message="name is 100 max characters", max = 100)
	private String name;
	
	@NotBlank(message = "typeOfUser cannot be null")
	@Size(message="typeOfUser is 2 max characters", max = 2)
	private String typeOfUser;
	
	@Size(message = "bornDate is 10 max characters", max=10)
	private String bornDate;
	
	@Size(message="gender is 1 max characters", max = 1)
	private String gender;
	
	@Size(message="postalCode is 8 max characters", max = 8)
	private String postalCode;

	@NotBlank(message = "country cannot be null")
	@Size(message="country is 3 max characters", max = 3)
	private String country;

	@Size(message="state is 100 max characters", max = 100)
	private String state;
	
	@Size(message="city is 100 max characters", max = 100)
	private String city;
	
	@Size(message="street is 100 max characters", max = 100)
	private String street;
	
	@Size(message="neighborhood is 100 max characters", max = 100)
	private String neighborhood;
	
	@Size(message="number is 10 max characters", max = 10)
	private String number;
	
	@Size(message="photo is 100 max characters", max = 100)
	private String photo;
	
	@Size(message="identityDocument is 36 max characters", max = 36)
	@NotBlank(message = "identityDocument cannot be null")
	private String identityDocument;
	
	@Size(message="telephone is 20 max characters", max = 20)
	private String telephone;

	public static class UserModelBuilder {
		private String id;
		private String login;
		private String password;
		private String name;
		private String typeOfUser;
		private String   bornDate;
		private String gender;
		private String postalCode;
		private String country;
		private String state;
		private String city;
		private String street;
		private String neighborhood;
		private String number;
		private String photo;
		private String identityDocument;
		private String telephone;

		
		public UserModelBuilder(String password){
			this.login = "not-needed";
			this.password = password;
		}
		
		
		
		public UserModelBuilder(String login, String password){
			this.login = login;
			this.password = password;
		}
		
		public UserModelBuilder name(String name){
			this.name = name;
			return this;
		}
		
		public UserModelBuilder id(String id){
			this.id = id;
			return this;
		}
		
		public UserModelBuilder typeOfUser(String typeOfUser){
			this.typeOfUser = typeOfUser;
			return this;
		}
		
		public UserModelBuilder bornDate(String bornDate){
			this.bornDate = bornDate;
			return this;
		}
		
		public UserModelBuilder gender(String gender){
			this.gender = gender;
			return this;
		}
		
		public UserModelBuilder postalCode(String postalCode){
			this.postalCode = postalCode;
			return this;
		}
		
		public UserModelBuilder country(String country){
			this.country = country;
			return this;
		}
		
		public UserModelBuilder state(String state){
			this.state = state;
			return this;
		}
		
		public UserModelBuilder city(String city){
			this.city = city;
			return this;
		}
		
		
		public UserModelBuilder street(String street){
			this.street = street;
			return this;
		}
		
		public UserModelBuilder neighborhood(String neighborhood){
			this.neighborhood = neighborhood;
			return this;
		}
		

		public UserModelBuilder number(String number){
			this.number = number;
			return this;
		}
		
		public UserModelBuilder photo(String photo){
			this.photo = photo;
			return this;
		}
		
		public UserModelBuilder identityDocument(String identityDocument){
			this.identityDocument = identityDocument;
			return this;
		}
		
		
		public UserModelBuilder telephone(String telephone){
			this.telephone = telephone;
			return this;
		}
		
		
		public UserModel build(){
			return new UserModel(this);
		}
		
		
		
	}

	private UserModel(UserModelBuilder builder){
		this.id = builder.id;
		this.login = builder.login;
		this.password = builder.password;
		this.name = builder.name;
		this.typeOfUser = builder.typeOfUser;
		this.bornDate = builder.bornDate;
		this.gender = builder.gender;
		this.postalCode = builder.postalCode;
		this.country = builder.country;
		this.state = builder.state;
		this.city = builder.city;
		this.street = builder.street;
		this.neighborhood = builder.neighborhood;
		this.number = builder.number;
		this.photo = builder.photo;
		this.identityDocument = builder.identityDocument;
		this.telephone = builder.telephone;
		
	}

	public String getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getTypeOfUser() {
		return typeOfUser;
	}

	public String getBornDate() {
		return bornDate;
	}

	public String getGender() {
		return gender;
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

	public String getIdentityDocument() {
		return identityDocument;
	}

	public void setIdentityDocument(String identityDocument) {
		this.identityDocument = identityDocument;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	
	
	public String getTelephone() {
		return telephone;
	}

	@Override
	public String toString() {
		return "UserModel [id=" + id + ", login=" + login + ", password=" + password + ", name=" + name
				+ ", typeOfUser=" + typeOfUser + ", bornDate=" + bornDate + ", gender=" + gender + ", postalCode="
				+ postalCode + ", country=" + country + ", state=" + state + ", city=" + city + ", street=" + street
				+ ", neighborhood=" + neighborhood + ", number=" + number + ", photo=" + photo + ", identityDocument="
				+ identityDocument + ", telephone=" + telephone + "]";
	}

	
	
	
	
}
