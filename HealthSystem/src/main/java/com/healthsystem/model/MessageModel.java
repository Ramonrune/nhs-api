package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageModel {

	private boolean success;
	private int code;
	private String description;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
