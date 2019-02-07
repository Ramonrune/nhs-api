package com.healthsystem.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class NurseProcedureModel {
	
	@NotBlank(message = "idDiagnosisProcedure cannot be null")
	private String idDiagnosisProcedure;
	
	@NotBlank(message = "idDiagnosisProcedure cannot be null")
	private String dateProcedure;
	private String anotation;
	
	@NotBlank(message = "idDiagnosis cannot be null")
	private String idDiagnosis;
	
	@NotBlank(message = "idNurse cannot be null")
	private String idNurse;
	
	@NotBlank(message = "status cannot be null")
	private String status;
	
	
	public String getIdDiagnosisProcedure() {
		return idDiagnosisProcedure;
	}
	public void setIdDiagnosisProcedure(String idDiagnosisProcedure) {
		this.idDiagnosisProcedure = idDiagnosisProcedure;
	}
	public String getDateProcedure() {
		return dateProcedure;
	}
	public void setDateProcedure(String dateProcedure) {
		this.dateProcedure = dateProcedure;
	}
	public String getAnotation() {
		return anotation;
	}
	public void setAnotation(String anotation) {
		this.anotation = anotation;
	}
	public String getIdDiagnosis() {
		return idDiagnosis;
	}
	public void setIdDiagnosis(String idDiagnosis) {
		this.idDiagnosis = idDiagnosis;
	}
	public String getIdNurse() {
		return idNurse;
	}
	public void setIdNurse(String idNurse) {
		this.idNurse = idNurse;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "NurseProcedureModel [idDiagnosisProcedure=" + idDiagnosisProcedure + ", dateProcedure=" + dateProcedure
				+ ", anotation=" + anotation + ", idDiagnosis=" + idDiagnosis + ", idNurse=" + idNurse + ", status="
				+ status + "]";
	}
	
	
	
	
	
}
