package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DiagnosisHistoryModel {

	private String name;
	private String dateDiagnosis;
	private String photo;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateDiagnosis() {
		return dateDiagnosis;
	}
	public void setDateDiagnosis(String dateDiagnosis) {
		this.dateDiagnosis = dateDiagnosis;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	
}
