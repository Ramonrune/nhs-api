package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class ExamAttachmentModel {

	@NotBlank(message = "idExamAttachment cannot be null")
	private String idExamAttachment;
	private String attachmentName;
	@NotBlank(message = "idExam cannot be null")
	private String idExam;
	
	public String getIdExamAttachment() {
		return idExamAttachment;
	}
	public void setIdExamAttachment(String idExamAttachment) {
		this.idExamAttachment = idExamAttachment;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getIdExam() {
		return idExam;
	}
	public void setIdExam(String idExam) {
		this.idExam = idExam;
	}
	@Override
	public String toString() {
		return "ExamAttachmentModel [idExamAttachment=" + idExamAttachment + ", attachmentName=" + attachmentName
				+ ", idExam=" + idExam + "]";
	}
	
	
	
	
}
