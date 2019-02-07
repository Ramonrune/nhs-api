package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DiseaseModel {

	private String idDisease;
	private String nameEn;
	private String namePt;
	
	public String getIdDisease() {
		return idDisease;
	}
	public void setIdDisease(String idDisease) {
		this.idDisease = idDisease;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getNamePt() {
		return namePt;
	}
	public void setNamePt(String namePt) {
		this.namePt = namePt;
	}


	
	
	
}
