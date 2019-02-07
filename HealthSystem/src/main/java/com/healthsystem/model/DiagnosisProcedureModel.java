package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DiagnosisProcedureModel {

	private String nurseName;
	private String anotation;
	private String nursePhoto;
	public String getNurseName() {
		return nurseName;
	}
	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}
	public String getAnotation() {
		return anotation;
	}
	public void setAnotation(String anotation) {
		this.anotation = anotation;
	}
	public String getNursePhoto() {
		return nursePhoto;
	}
	public void setNursePhoto(String nursePhoto) {
		this.nursePhoto = nursePhoto;
	}
	
	
	
}
