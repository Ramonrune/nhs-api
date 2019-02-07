package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExamHistoryModel {

	private String name;
	private String dateExam;
	private String photo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateExam() {
		return dateExam;
	}

	public void setDateExam(String dateExam) {
		this.dateExam = dateExam;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
