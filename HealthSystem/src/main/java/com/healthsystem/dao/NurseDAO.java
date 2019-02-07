package com.healthsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;

import com.healthsystem.database.DatabaseConnectionFactory;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.ErrorLogModel;
import com.healthsystem.model.NurseModel;
import com.healthsystem.model.NurseProcedureModel;
import com.healthsystem.model.NurseProcessModel;
import com.healthsystem.model.SpecializationModel;
import com.healthsystem.util.JwtUtil;

@Resource
public class NurseDAO {

	private Connection connection;
	private ErrorLogDAO errorLogDAO = new ErrorLogDAO();
	private AuditTrailDAO auditTrailDAO = new AuditTrailDAO();
	private HttpHeaders httpHeaders;

	public NurseDAO() {
		this.connection = DatabaseConnectionFactory.getConnection();
	}

	public boolean add(NurseModel nurseModel) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("INSERT_NURSE");
		auditTrailModel.setAdditionalInfo(nurseModel.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO NURSE VALUES (?, ?, ?, ?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, nurseModel.getIdNurse());
			stm.setString(2, nurseModel.getIdUser());
			stm.setString(3, nurseModel.getNurseCode());
			stm.setString(4, nurseModel.getNurseType());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, nurseModel.toString());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(NurseModel nurseModel) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("UPDATE_NURSE");
		auditTrailModel.setAdditionalInfo(nurseModel.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "UPDATE NURSE SET nurse_code = ?, nurse_type = ? WHERE id_nurse = ? AND id_user = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, nurseModel.getNurseCode());
			stm.setString(2, nurseModel.getNurseType());
			stm.setString(3, nurseModel.getIdNurse());
			stm.setString(4, nurseModel.getIdUser());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, nurseModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public NurseModel getNurse(String idUser) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("GET_NURSE");
		auditTrailModel.setAdditionalInfo("idUser " + idUser);
		setAuditTrail(auditTrailModel);
		
		NurseModel nurseModel = null;

		String sql = "SELECT id_nurse, nurse_code, nurse_type, id_user "
				+ " from NURSE WHERE id_user = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idUser);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				nurseModel = new NurseModel();
				nurseModel.setIdNurse(rs.getString("id_nurse"));
				nurseModel.setIdUser(rs.getString("id_user"));
				nurseModel.setNurseCode(rs.getString("nurse_code"));
				nurseModel.setNurseType(rs.getString("nurse_type"));

			}
			rs.close();
			stm.close();
			
			
		} catch (SQLException e) {
			setError(e, "Id user " + idUser);

			e.printStackTrace();
		}
		
		return nurseModel;
	}

	public List<SpecializationModel> listNurseSpecialization(String idNurse) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("LIST_NURSE_SPECIALIZATIONS");
		auditTrailModel.setAdditionalInfo("idNurse " + idNurse);
		setAuditTrail(auditTrailModel);
		

		List<SpecializationModel> listSpecialization = new ArrayList<>();

		String sql = "SELECT " + " NURSE_HAS_SPECIALIZATION.id_specialization  "
				+ " FROM NURSE_HAS_SPECIALIZATION "
				+ " INNER JOIN NURSE ON NURSE.id_nurse =  NURSE_HAS_SPECIALIZATION.id_nurse  "
				+ " INNER JOIN NURSE_SPECIALIZATION ON NURSE_SPECIALIZATION.id_specialization = NURSE_HAS_SPECIALIZATION.id_specialization  "
				+ "  WHERE NURSE_HAS_SPECIALIZATION.id_nurse = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idNurse);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				SpecializationModel specializationModel = new SpecializationModel();
				specializationModel.setIdSpecialization(rs.getString("id_specialization"));

				listSpecialization.add(specializationModel);
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			
			setError(e, "Id nurse " + idNurse);

			e.printStackTrace();
		}
		return listSpecialization;

	}

	public List<SpecializationModel> listSpecialization(String country, String language) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("LIST_SPECIALIZATIONS");
		auditTrailModel.setAdditionalInfo("country " + country + " language " + language);
		setAuditTrail(auditTrailModel);

		List<SpecializationModel> listSpecialization = new ArrayList<>();

		String sql = "SELECT * from NURSE_SPECIALIZATION WHERE country = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, country);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				SpecializationModel specializationModel = new SpecializationModel();
				specializationModel.setIdSpecialization(rs.getString("id_specialization"));
				
				if(language.equals("PT")){
				specializationModel.setName(rs.getString("name_pt"));
				
				}
				if(language.equals("EN")){
					specializationModel.setName(rs.getString("name_en"));

				}
				listSpecialization.add(specializationModel);
				
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			
			setError(e, "Country " + country + " Language " + language);

			e.printStackTrace();
		}
		return listSpecialization;

	}

	public boolean bindSpecialization(String idNurse, String idSpecialization) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("BIND_SPECIALIZATION");
		auditTrailModel.setAdditionalInfo("idNurse " + idNurse + " idSpecialization " + idSpecialization);
		setAuditTrail(auditTrailModel); 
		
		String sql = "INSERT INTO NURSE_HAS_SPECIALIZATION VALUES (?, ?, ?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, UUID.randomUUID().toString());
			stm.setString(2, idNurse);
			stm.setString(3, idSpecialization);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			
			setError(e, "Id nurse " + idNurse + " Id specialization " + idSpecialization);

			e.printStackTrace();
			return false;
		}
	}

	public boolean unbindSpecialization(String idNurse, String idSpecialization) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("DELETE_BIND_SPECIALIZATION");
		auditTrailModel.setAdditionalInfo("idNurse " + idNurse + " idSpecialization " + idSpecialization);
		setAuditTrail(auditTrailModel); 
		
		
		String sql = "DELETE FROM NURSE_HAS_SPECIALIZATION WHERE id_nurse = ? AND id_specialization = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idNurse);
			stm.setString(2, idSpecialization);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			
			setError(e, "Id nurse " + idNurse + " Id specialization " + idSpecialization);

			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	public boolean addProcedure(NurseProcedureModel nurseProcedureModel) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("INSERT_NURSE_PROCEDURE");
		auditTrailModel.setAdditionalInfo(nurseProcedureModel.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO DIAGNOSIS_PROCEDURE VALUES (?,?,?,?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, nurseProcedureModel.getIdDiagnosisProcedure());
			stm.setString(2, nurseProcedureModel.getDateProcedure());
			stm.setString(3, nurseProcedureModel.getAnotation());
			stm.setString(4, nurseProcedureModel.getStatus());
			stm.setString(5, nurseProcedureModel.getIdDiagnosis());
			stm.setString(6, nurseProcedureModel.getIdNurse());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, nurseProcedureModel.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	

	public boolean updateNurseProcedure(String idDiagnosisProcedure, String status, String anotation) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("UPDATE_NURSE_PROCEDURE");
		auditTrailModel.setAdditionalInfo("idDiagnosisProcedure " + idDiagnosisProcedure + " status " + status + " anotation " + anotation);
		setAuditTrail(auditTrailModel);
		
		String sql = "UPDATE DIAGNOSIS_PROCEDURE set status = ?, anotation = ? WHERE id_diagnosis_procedure = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, status);
			stm.setString(2, anotation);
			stm.setString(3, idDiagnosisProcedure);
			
			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idDiagnosisProcedure " + idDiagnosisProcedure + " status " + status + " anotation " + anotation);
			e.printStackTrace();
			return false;
		}
	}


	public List<NurseProcessModel> listAttendance(String idHealthInstitution, String idNurse, String status) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("NURSE");
		auditTrailModel.setEventName("LIST_NURSE_PROCEDURE");
		auditTrailModel.setAdditionalInfo("idHealthInstitution " + idHealthInstitution + " idNurse " + idNurse + " status " + status);
		setAuditTrail(auditTrailModel);

		List<NurseProcessModel> listNurseProcess = new ArrayList<>();

		String sql = "SELECT d_p.id_diagnosis, d_p.anotation as procedure_anotation, d_p.id_nurse, d_p.status, d_p.date_procedure, d_p.id_diagnosis_procedure, " + 
" d.date_diagnosis, d.anotation, u.name as patient_name, u.photo as patient_photo, " + 
" h.name as institution_name, u_h.name as physician_name, u_h.photo as physician_photo, " + 
" u_h_n.name as nurse_name, u_h_n.photo as nurse_photo, h.id_health_institution " + 
" FROM DIAGNOSIS_PROCEDURE AS d_p INNER JOIN diagnosis as d ON d_p.id_diagnosis = d.id_diagnosis  " + 
" INNER JOIN PATIENT AS p ON p.id_patient = d.id_patient " + 
" INNER JOIN USER_HEALTH as u ON u.id_user = p.id_user " +
" INNER JOIN HEALTH_INSTITUTION as h ON h.id_health_institution = d.id_health_institution " +
" INNER JOIN PHYSICIAN as ph ON ph.id_physician = d.id_physician " +
" INNER JOIN USER_HEALTH as u_h ON u_h.id_user = ph.id_user " +
" INNER JOIN NURSE AS n ON d_p.id_nurse = n.id_nurse " +
" INNER JOIN USER_HEALTH as u_h_n ON u_h_n.id_user = n.id_user " +
" WHERE h.id_health_institution = ? AND n.id_nurse = ? AND d_p.status = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, idHealthInstitution);
			stm.setString(2, idNurse);
			stm.setString(3, status);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				NurseProcessModel nurseProcessModel = new NurseProcessModel();
				nurseProcessModel.setIdDiagnosis(rs.getString("id_diagnosis"));
				nurseProcessModel.setAnotationProcedure(rs.getString("procedure_anotation"));
				nurseProcessModel.setIdNurse(rs.getString("id_nurse"));
				nurseProcessModel.setStatus(rs.getString("status"));
				nurseProcessModel.setDateProcedure(rs.getString("date_procedure"));
				nurseProcessModel.setIdDiagnosisProcedure(rs.getString("id_diagnosis_procedure"));
				nurseProcessModel.setDateDiagnosis(rs.getString("date_diagnosis"));
				nurseProcessModel.setAnotationDiagnosis(rs.getString("anotation"));
				nurseProcessModel.setPatientName(rs.getString("patient_name"));
				nurseProcessModel.setPatientPhoto(rs.getString("patient_photo"));
				nurseProcessModel.setHealthInstitutionName(rs.getString("institution_name"));
				nurseProcessModel.setPhysicianName(rs.getString("physician_name"));
				nurseProcessModel.setPhysicianPhoto(rs.getString("physician_photo"));
				nurseProcessModel.setNurseName(rs.getString("nurse_name"));
				nurseProcessModel.setNursePhoto(rs.getString("nurse_photo"));
				nurseProcessModel.setIdHealthInstitution(rs.getString("id_health_institution"));

				listNurseProcess.add(nurseProcessModel);
				
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			
			setError(e, "idHealthInstitution " + idHealthInstitution + " idNurse " + idNurse + " status " + status);

			e.printStackTrace();
		}
		return listNurseProcess;

	}

	public void setHttpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	private void setError(Exception e, String additionalInfo){
		ErrorLogModel errorLogModel = new ErrorLogModel();
		errorLogModel.setClassName(e.getStackTrace()[0].getClassName());
		errorLogModel.setLineNumber(e.getStackTrace()[0].getLineNumber());
		errorLogModel.setMessage(e.getMessage());
		errorLogModel.setMethodName(e.getStackTrace()[0].getMethodName());
		errorLogModel.setNameOfFile(e.getStackTrace()[0].getFileName());
		errorLogModel.setAdditionalInfo(additionalInfo);
		if(httpHeaders != null){
			errorLogModel.setUserId(JwtUtil.getUserId(httpHeaders.getHeaderString("Authorization")));
			
		}
		errorLogDAO.add(errorLogModel);
		
	}
	
	private void setAuditTrail(AuditTrailModel auditTrailModel){
		
		if(httpHeaders != null){
			auditTrailModel.setIdUser(JwtUtil.getUserId(httpHeaders.getHeaderString("Authorization")));
			
		}
		auditTrailDAO.add(auditTrailModel);
		
	}

}
