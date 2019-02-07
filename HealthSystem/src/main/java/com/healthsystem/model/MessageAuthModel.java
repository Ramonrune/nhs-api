package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageAuthModel extends MessageModel{

	private String userId;
	private String token;
	private String userType;
	private String userName;
	private String secretCode;
	
	
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserId() {
		return userId;
	}
	public String getToken() {
		return token;
	}
	public String getUserType() {
		return userType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSecretCode() {
		return secretCode;
	}
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
	
	
	
	
	
	
}
