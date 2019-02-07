package com.healthsystem.model;

import java.util.UUID;

public class AuditTrailModel {

	private String idAuditTrail = UUID.randomUUID().toString();
	private String eventDate;
	private String category;
	private String eventName;
	private String additionalInfo;
	private String idUser;
	private String name;
	private String login;
	private String photo;
	
	
	public String getIdAuditTrail() {
		return idAuditTrail;
	}
	public void setIdAuditTrail(String idAuditTrail) {
		this.idAuditTrail = idAuditTrail;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
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
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	
	
}
