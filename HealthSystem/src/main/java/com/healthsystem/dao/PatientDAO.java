package com.healthsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;

import com.healthsystem.database.DatabaseConnectionFactory;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.DeficiencyModel;
import com.healthsystem.model.DiagnosisModel;
import com.healthsystem.model.DiagnosisProcedureModel;
import com.healthsystem.model.DiseaseModel;
import com.healthsystem.model.ErrorLogModel;
import com.healthsystem.model.ExamAttachmentModel;
import com.healthsystem.model.ExamModel;
import com.healthsystem.model.HistoryModel;
import com.healthsystem.model.MedicineModel;
import com.healthsystem.model.PatientHasDeficiencyModel;
import com.healthsystem.model.PatientHasDiseaseModel;
import com.healthsystem.model.PatientModel;
import com.healthsystem.model.PatientTagModel;
import com.healthsystem.model.PatientUseMedicineModel;
import com.healthsystem.model.PhysicianWorksOnHealthInstitutionModel;
import com.healthsystem.model.SpecializationModel;
import com.healthsystem.util.JwtUtil;
import com.healthsystem.util.Translate;

@Resource
public class PatientDAO {

	private Connection connection;
	private ErrorLogDAO errorLogDAO = new ErrorLogDAO();
	private AuditTrailDAO auditTrailDAO = new AuditTrailDAO();
	private HttpHeaders httpHeaders;


	public PatientDAO() {
		this.connection = DatabaseConnectionFactory.getConnection();
	}

	public boolean add(PatientModel patientModel) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("INSERT_PATIENT");
		auditTrailModel.setAdditionalInfo(patientModel.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO PATIENT VALUES (?,?,?,?,?,?,?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, patientModel.getIdPatient());
			stm.setString(2, patientModel.getBloodType());
			stm.setString(3, patientModel.getColor());
			stm.setString(4, patientModel.getFatherName());
			stm.setString(5, patientModel.getMotherName());
			stm.setDouble(6, patientModel.getWeight());
			stm.setDouble(7, patientModel.getHeight());
			stm.setString(8, patientModel.getIdUser());
			stm.setString(9, patientModel.getStatus());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, patientModel.toString());
			e.printStackTrace();
			return false;
		}
	}

	public boolean checkIfUserExist(String idUser) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("CHECK_IF_USER_EXIST");
		auditTrailModel.setAdditionalInfo("idUser " + idUser);
		setAuditTrail(auditTrailModel);

		String sql = "select count(*) from PATIENT where id_user = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			System.out.println(stm.toString());

			stm.setString(1, idUser);

			ResultSet rs = stm.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				return true;
			}

			return false;

		} catch (SQLException e) {
			setError(e, "Id user " + idUser);

			return false;
		}
	}

	public boolean update(PatientModel patientModel) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("UPDATE_PATIENT");
		auditTrailModel.setAdditionalInfo(patientModel.toString());
		setAuditTrail(auditTrailModel);
		
		
		String sql = "UPDATE PATIENT SET blood_type = ?, color = ?, father_name = ?, "
				+ " mother_name = ?, weight = ?, height = ?, status = ? WHERE id_patient = ? and id_user = ?";
		try {

			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, patientModel.getBloodType());
			stm.setString(2, patientModel.getColor());
			stm.setString(3, patientModel.getFatherName());
			stm.setString(4, patientModel.getMotherName());
			stm.setDouble(5, patientModel.getWeight());
			stm.setDouble(6, patientModel.getHeight());
			stm.setString(7, patientModel.getStatus());
			stm.setString(8, patientModel.getIdPatient());
			stm.setString(9, patientModel.getIdUser());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, patientModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public PatientModel getPatient(String idUser) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("GET_PATIENT");
		auditTrailModel.setAdditionalInfo("idUser " + idUser);
		setAuditTrail(auditTrailModel);
		
		
		PatientModel patientModel = null;

		String sql = "SELECT * from PATIENT WHERE id_user = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idUser);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				patientModel = new PatientModel();
				patientModel.setIdPatient(rs.getString("id_patient"));
				patientModel.setBloodType(rs.getString("blood_type"));
				patientModel.setColor(rs.getString("color"));
				patientModel.setFatherName(rs.getString("father_name"));
				patientModel.setMotherName(rs.getString("mother_name"));
				patientModel.setWeight(rs.getDouble("weight"));
				patientModel.setHeight(rs.getDouble("height"));
				patientModel.setIdUser(idUser);
				patientModel.setStatus(rs.getString("status"));

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "Id user " + idUser);

			e.printStackTrace();
		}

		return patientModel;
	}

	public boolean registerTag(String idPatientTag, String macCode, String idPatient) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("INSERT_TAG");
		auditTrailModel.setAdditionalInfo("idPatientTag " + idPatientTag + " macCode " + macCode + " idPatient" + idPatient);
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO PATIENT_HAS_TAG VALUES (?,?,?, ?, ?);";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatientTag);
			stm.setString(2, macCode);
			stm.setString(3, idPatient);
			stm.setNull(4, Types.VARCHAR);
			stm.setNull(5, Types.VARCHAR);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			
			setError(e, "idPatientTag " + idPatientTag + " MacCode" + macCode + " idPatient" + idPatient);

			e.printStackTrace();
			return false;
		}
	}

	public boolean updateTag(String idPatientTag, String name, String tagType) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("UPDATE_TAG");
		auditTrailModel.setAdditionalInfo("idPatientTag " + idPatientTag + " name " + name + " tagType" + tagType);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "UPDATE PATIENT_HAS_TAG set name = ?, tag_type = ? WHERE id_patient_has_tag = ?;";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, name);
			stm.setString(2, tagType);
			stm.setString(3, idPatientTag);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPatientTag " + idPatientTag + " Name" + name + " tagType" + tagType);

			e.printStackTrace();
			return false;
		}
	}

	public boolean unregisterTag(String idPatientHasTag) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("DELETE_TAG");
		auditTrailModel.setAdditionalInfo("idPatientHasTag " + idPatientHasTag);
		setAuditTrail(auditTrailModel);
		
		String sql = "DELETE FROM PATIENT_HAS_TAG WHERE id_patient_has_tag = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatientHasTag);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPatientHasTag " + idPatientHasTag);

			e.printStackTrace();
			return false;
		}
	}

	public boolean tagExist(String macCode) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("TAG_EXIST");
		auditTrailModel.setAdditionalInfo("macCode " + macCode);
		setAuditTrail(auditTrailModel);
		
		String sql = "SELECT COUNT(*) FROM PATIENT_HAS_TAG WHERE mac_code = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, macCode);

			ResultSet rs = stm.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				return true;
			}

			return false;

		} catch (SQLException e) {
			setError(e, "macCode " + macCode);

			e.printStackTrace();
			return false;
		}
	}

	public List<PatientTagModel> listPatientTag(String idPatient) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_TAG");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient);
		setAuditTrail(auditTrailModel);
		

		List<PatientTagModel> list = new ArrayList<>();
		String sql = "SELECT PATIENT_HAS_TAG.id_patient_has_tag, PATIENT_HAS_TAG.mac_code, PATIENT_HAS_TAG.tag_type, PATIENT_HAS_TAG.name FROM PATIENT_HAS_TAG WHERE id_patient = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatient);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				PatientTagModel patientTagModel = new PatientTagModel();
				patientTagModel.setIdPatientTag(rs.getString("id_patient_has_tag"));
				patientTagModel.setMacCode(rs.getString("mac_code"));
				patientTagModel.setIdPatient(idPatient);
				patientTagModel.setName(rs.getString("name"));
				patientTagModel.setTagType(rs.getString("tag_type"));
				list.add(patientTagModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient);

			e.printStackTrace();
		}

		return list;
	}

	public PatientModel getPatientByTagCode(String macCode) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("GET_PATIENT_BY_TAG");
		auditTrailModel.setAdditionalInfo("macCode " + macCode);
		setAuditTrail(auditTrailModel);
		
		
		PatientModel patientModel = null;
		macCode = macCode.trim();
		System.out.println(macCode);

		String sql = "SELECT PATIENT.blood_type, PATIENT.color, " + " PATIENT.father_name, " + " PATIENT.mother_name, "
				+ " PATIENT.height, " + " PATIENT.weight, " + " PATIENT.id_user, " + " PATIENT.id_patient, "
				+ " PATIENT.status FROM PATIENT_HAS_TAG INNER JOIN  "
				+ " PATIENT ON  PATIENT.id_patient = PATIENT_HAS_TAG.id_patient "
				+ " WHERE PATIENT_HAS_TAG.mac_code = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, macCode);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				patientModel = new PatientModel();

				patientModel.setIdPatient(rs.getString("id_patient"));
				patientModel.setBloodType(rs.getString("blood_type"));
				patientModel.setColor(rs.getString("color"));
				patientModel.setFatherName(rs.getString("father_name"));
				patientModel.setMotherName(rs.getString("mother_name"));
				patientModel.setWeight(rs.getDouble("weight"));
				patientModel.setHeight(rs.getDouble("height"));
				patientModel.setIdUser(rs.getString("id_user"));
				patientModel.setStatus(rs.getString("status"));

			}

			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "macCode " + macCode);

			e.printStackTrace();
		}

		return patientModel;
	}

	public boolean addDiagnosis(DiagnosisModel diagnosisModel) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("INSERT_DIAGNOSIS");
		auditTrailModel.setAdditionalInfo(diagnosisModel.toString());
		setAuditTrail(auditTrailModel);
		
		
		String sql = "INSERT INTO DIAGNOSIS VALUES (?,?,?,?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, diagnosisModel.getIdDiagnosis());
			stm.setString(2, diagnosisModel.getDateDiagnosis());
			stm.setString(3, diagnosisModel.getAnnotation());
			stm.setString(4, diagnosisModel.getIdPatient());
			stm.setString(5, diagnosisModel.getIdPhysician());
			stm.setString(6, diagnosisModel.getIdHealthInstitution());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, diagnosisModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean updateDiagnosis(String idDiagnosis, String anotation) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("UPDATE_DIAGNOSIS");
		auditTrailModel.setAdditionalInfo("idDiagnosis " + idDiagnosis + " anotation " + anotation);
		setAuditTrail(auditTrailModel);
		
		String sql = "UPDATE DIAGNOSIS set anotation = ? WHERE id_diagnosis = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, anotation);
			stm.setString(2, idDiagnosis);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idDiagnosis " + idDiagnosis + " anotation " + anotation);

			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteDiagnosis(String idDiagnosis) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("DELETE_DIAGNOSIS");
		auditTrailModel.setAdditionalInfo("idDiagnosis " + idDiagnosis);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "DELETE FROM DIAGNOSIS WHERE id_diagnosis = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idDiagnosis);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idDiagnosis " + idDiagnosis);

			e.printStackTrace();
			return false;
		}
	}

	public List<DiagnosisModel> listPatientDiagnosis(String idPatient) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_DIAGNOSIS");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient);
		setAuditTrail(auditTrailModel);
		
		
		List<DiagnosisModel> list = new ArrayList<>();
		String sql = "SELECT D.id_diagnosis, D.date_diagnosis, D.anotation, D.id_patient, D.id_physician, D.id_health_institution, "
				+ "H.name, H.photo as hphoto, H.latitute, H.longitute, P.practice_document, U.name as pname, U.country as pcountry, U.photo as pphoto "
				+ " FROM DIAGNOSIS AS D " + "INNER JOIN HEALTH_INSTITUTION AS H ON  "
				+ "D.id_health_institution = H.id_health_institution " + "INNER JOIN PHYSICIAN AS P ON  "
				+ "P.id_physician = D.id_physician "
				+ "INNER JOIN (select  name, id_physician, country, photo from PHYSICIAN INNER JOIN USER_HEALTH "
				+ "ON USER_HEALTH.id_user = PHYSICIAN.id_user) AS U ON " + "P.id_physician = U.id_physician "
				+ "WHERE D.id_patient = ? ORDER BY date_diagnosis DESC;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatient);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				DiagnosisModel diagnosisModel = new DiagnosisModel();

				diagnosisModel.setIdDiagnosis(rs.getString("id_diagnosis"));
				diagnosisModel.setDateDiagnosis(rs.getString("date_diagnosis"));
				diagnosisModel.setAnnotation(rs.getString("anotation"));
				diagnosisModel.setIdPatient(rs.getString("id_patient"));
				diagnosisModel.setIdPhysician(rs.getString("id_physician"));
				diagnosisModel.setIdHealthInstitution(rs.getString("id_health_institution"));
				diagnosisModel.setPhysicianPracticeNumber(rs.getString("practice_document"));
				diagnosisModel.setPhysicianName(rs.getString("pname"));
				diagnosisModel.setHealthInstitutionName(rs.getString("name"));
				diagnosisModel.setPhysicianCountry(rs.getString("pcountry"));
				diagnosisModel.setPhysicianPhoto(rs.getString("pphoto"));
				diagnosisModel.setHealthInstitutionLatitute(rs.getDouble("latitute"));
				diagnosisModel.setHealthInstitutionLongitute(rs.getDouble("longitute"));
				diagnosisModel.setHealthInstitutionPhoto(rs.getString("hphoto"));
				list.add(diagnosisModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient);

			e.printStackTrace();
		}

		return list;
	}
	
	
	public DiagnosisModel getDiagnosis(String idDiagnosis) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("GET_DIAGNOSIS");
		auditTrailModel.setAdditionalInfo("idDiagnosis " + idDiagnosis);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "select d.id_diagnosis, d.date_diagnosis, d.id_patient, d.id_physician, d.anotation, u.name as patient_name, u.photo from diagnosis as d "  +
" INNER JOIN PHYSICIAN AS p ON p.id_physician = d.id_physician " +
" INNER JOIN PATIENT AS pa ON pa.id_patient = d.id_patient "  +
" INNER JOIN USER_HEALTH as u ON u.id_user = pa.id_user " + 
" WHERE d.id_diagnosis = ? ORDER BY d.date_diagnosis DESC; ";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idDiagnosis);
			 
			ResultSet rs = stm.executeQuery();
			DiagnosisModel diagnosisModel = new DiagnosisModel();

			while (rs.next()) {


				diagnosisModel.setIdDiagnosis(rs.getString("id_diagnosis"));
				diagnosisModel.setDateDiagnosis(rs.getString("date_diagnosis"));
				diagnosisModel.setAnnotation(rs.getString("anotation"));
				diagnosisModel.setIdPatient(rs.getString("id_patient"));
				diagnosisModel.setIdPhysician(rs.getString("id_physician"));
				diagnosisModel.setPatientName(rs.getString("patient_name"));
				diagnosisModel.setPatientPhoto(rs.getString("photo"));

			}
			rs.close();
			stm.close();
			
			return diagnosisModel;

		} catch (SQLException e) {
			setError(e, "idDiagnosis " + idDiagnosis);

			e.printStackTrace();
		}

		return null;
	}
	
	public DiagnosisProcedureModel getDiagnosisProcedures(String idDiagnosis) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("GET_DIAGNOSIS_PROCEDURES");
		auditTrailModel.setAdditionalInfo("idDiagnosis " + idDiagnosis);
		setAuditTrail(auditTrailModel);
		
		
		String sql = 
" SELECT D_P.anotation, U.name, U.photo FROM DIAGNOSIS_PROCEDURE AS D_P INNER JOIN DIAGNOSIS AS D on D.id_diagnosis = D_P.id_diagnosis "  +
" INNER JOIN NURSE AS N ON N.id_nurse = D_P.id_nurse " + 
" INNER JOIN USER_HEALTH AS U on U.id_user = N.id_user " + 
" WHERE D_P.id_diagnosis = ? ";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idDiagnosis);
			 
			ResultSet rs = stm.executeQuery();
			DiagnosisProcedureModel diagnosisProcedureModel = new DiagnosisProcedureModel();

			while (rs.next()) {


				diagnosisProcedureModel.setAnotation(rs.getString("anotation"));
				diagnosisProcedureModel.setNurseName(rs.getString("name"));
				diagnosisProcedureModel.setNursePhoto(rs.getString("photo"));
			

			}
			rs.close();
			stm.close();
			
			return diagnosisProcedureModel;

		} catch (SQLException e) {
			setError(e, "idDiagnosis " + idDiagnosis);

			e.printStackTrace();
		}

		return null;
	}

	public List<DiseaseModel> listDiseases() {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_DISEASES");
		auditTrailModel.setAdditionalInfo("");
		setAuditTrail(auditTrailModel);

		List<DiseaseModel> list = new ArrayList<>();
		String sql = "SELECT * FROM DISEASE";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				DiseaseModel diseaseModel = new DiseaseModel();

				diseaseModel.setIdDisease(rs.getString("id_disease"));
				diseaseModel.setNameEn(rs.getString("name_en"));
				diseaseModel.setNamePt(rs.getString("name_pt"));

				list.add(diseaseModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "");

			e.printStackTrace();
		}

		return list;
	}

	public boolean bindDisease(PatientHasDiseaseModel patientHasDiseaseModel) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("BIND_DISEASE");
		auditTrailModel.setAdditionalInfo(patientHasDiseaseModel.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO PATIENT_HAS_DISEASE VALUES (?,?,?, ?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, patientHasDiseaseModel.getIdPatientHasDisease());
			stm.setString(2, patientHasDiseaseModel.getIdPatient());
			stm.setString(3, patientHasDiseaseModel.getIdDisease());
			stm.setString(4, patientHasDiseaseModel.getAnotation());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, patientHasDiseaseModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean updateBindDisease(String idPatientHasDisease, String anotation) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("UPDATE_BIND_DISEASE");
		auditTrailModel.setAdditionalInfo("idPatientHasDisease " + idPatientHasDisease + " anotation " + anotation);
		setAuditTrail(auditTrailModel);
		
		String sql = "UPDATE PATIENT_HAS_DISEASE SET anotations = ? WHERE id_patient_has_disease = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, anotation);
			stm.setString(2, idPatientHasDisease);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPatientHasDisease " + idPatientHasDisease + " anotation " + anotation);

			e.printStackTrace();
			return false;
		}
	}

	public boolean unbindDisease(String idPatientHasDisease) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("DELETE_BIND_DISEASE");
		auditTrailModel.setAdditionalInfo("idPatientHasDisease " + idPatientHasDisease);
		setAuditTrail(auditTrailModel);
		
		String sql = "DELETE FROM PATIENT_HAS_DISEASE WHERE id_patient_has_disease = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatientHasDisease);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPatientHasDisease " + idPatientHasDisease);

			e.printStackTrace();
			return false;
		}
	}

	public List<PatientHasDiseaseModel> listPatientDiseases(String idPatient) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_DISEASES");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient);
		setAuditTrail(auditTrailModel);
		
		
		List<PatientHasDiseaseModel> list = new ArrayList<>();
		String sql = " SELECT P.id_patient_has_disease, P.id_disease, P.anotations,D.name_en, D.name_pt FROM PATIENT_HAS_DISEASE AS P INNER JOIN DISEASE  AS D ON P.id_disease = D.id_disease "
				+ " WHERE p.id_patient = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatient);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				PatientHasDiseaseModel patientHasDiseaseModel = new PatientHasDiseaseModel();
				patientHasDiseaseModel.setAnotation(rs.getString("anotations"));
				patientHasDiseaseModel.setIdDisease(rs.getString("id_disease"));
				patientHasDiseaseModel.setIdPatient(idPatient);
				patientHasDiseaseModel.setIdPatientHasDisease(rs.getString("id_patient_has_disease"));
				list.add(patientHasDiseaseModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient);

			e.printStackTrace();
		}

		return list;
	}

	public boolean addExam(ExamModel examModel) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("INSERT_EXAM");
		auditTrailModel.setAdditionalInfo(examModel.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO EXAM VALUES (?,?,?,?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, examModel.getIdExam());
			stm.setString(2, examModel.getDateExam());
			stm.setString(3, examModel.getAnotation());
			stm.setString(4, examModel.getIdPatient());
			stm.setString(5, examModel.getIdPhysician());
			stm.setString(6, examModel.getIdHealthInstitution());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, examModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean updateExam(String idExam, String anotation) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("INSERT_EXAM");
		auditTrailModel.setAdditionalInfo("idExam " + idExam + " anotation " + anotation);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "UPDATE EXAM SET anotation = ? WHERE id_exam = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, anotation);
			stm.setString(2, idExam);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idExam " + idExam + " anotation" + anotation);

			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteExam(String idExam) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("DELETE_EXAM");
		auditTrailModel.setAdditionalInfo("idExam " + idExam);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "DELETE FROM EXAM WHERE id_exam = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idExam);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			
			setError(e, "idExam " + idExam);

			e.printStackTrace();
			return false;
		}
	}

	public List<ExamModel> listPatientExams(String idPatient) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_PATIENT_EXAMS");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient);
		setAuditTrail(auditTrailModel);

		List<ExamModel> list = new ArrayList<>();
		String sql = "SELECT E.id_exam, E.date_exam, E.anotation, E.id_patient, E.id_physician, E.id_health_institution, "
				+ "H.name, H.photo as hphoto, H.latitute, H.longitute, P.practice_document, U.name as pname, U.country as pcountry, U.photo as pphoto "
				+ " FROM EXAM AS E " + "INNER JOIN HEALTH_INSTITUTION AS H ON  "
				+ "E.id_health_institution = H.id_health_institution " + "INNER JOIN PHYSICIAN AS P ON  "
				+ "P.id_physician = E.id_physician "
				+ "INNER JOIN (select  name, id_physician, country, photo from PHYSICIAN INNER JOIN USER_HEALTH "
				+ "ON USER_HEALTH.id_user = PHYSICIAN.id_user) AS U ON " + "P.id_physician = U.id_physician "
				+ "WHERE E.id_patient = ? ORDER BY date_exam DESC;";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatient);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ExamModel examModel = new ExamModel();
				examModel.setIdExam(rs.getString("id_exam"));
				examModel.setAnotation(rs.getString("anotation"));
				examModel.setDateExam(rs.getString("date_exam"));
				examModel.setIdPatient(rs.getString("id_patient"));
				examModel.setIdPhysician(rs.getString("id_physician"));
				examModel.setIdHealthInstitution(rs.getString("id_health_institution"));

				examModel.setPhysicianPracticeNumber(rs.getString("practice_document"));
				examModel.setPhysicianName(rs.getString("pname"));
				examModel.setHealthInstitutionName(rs.getString("name"));
				examModel.setPhysicianCountry(rs.getString("pcountry"));
				examModel.setPhysicianPhoto(rs.getString("pphoto"));
				examModel.setHealthInstitutionLatitute(rs.getDouble("latitute"));
				examModel.setHealthInstitutionLongitute(rs.getDouble("longitute"));
				examModel.setHealthInstitutionPhoto(rs.getString("hphoto"));
				examModel.setAttachmentSize(listExamAttachments(rs.getString("id_exam")).size());
				list.add(examModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient);

			e.printStackTrace();
		}

		return list;
	}

	public boolean addAttachment(ExamAttachmentModel attachmentModel) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("INSERT_EXAM_ATTACHMENT");
		auditTrailModel.setAdditionalInfo(attachmentModel.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO EXAM_ATTACHMENT VALUES (?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, attachmentModel.getIdExamAttachment());
			stm.setString(2, attachmentModel.getAttachmentName());
			stm.setString(3, attachmentModel.getIdExam());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, attachmentModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteAttachment(String idExamAttachment) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("DELETE_EXAM_ATTACHMENT");
		auditTrailModel.setAdditionalInfo("idExamAttachment " + idExamAttachment);
		setAuditTrail(auditTrailModel);
		
		String sql = "DELETE FROM EXAM_ATTACHMENT WHERE id_exam_attachment = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idExamAttachment);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idExamAttachment " + idExamAttachment);

			e.printStackTrace();
			return false;
		}
	}

	public List<ExamAttachmentModel> listExamAttachments(String idExam) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_EXAM_ATTACHMENTS");
		auditTrailModel.setAdditionalInfo("idExam " + idExam);
		setAuditTrail(auditTrailModel);
		
		List<ExamAttachmentModel> list = new ArrayList<>();
		String sql = "SELECT * FROM EXAM_ATTACHMENT WHERE id_exam = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idExam);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ExamAttachmentModel examAttachmentModel = new ExamAttachmentModel();

				examAttachmentModel.setIdExam(rs.getString("id_exam"));
				examAttachmentModel.setAttachmentName(rs.getString("attachment_name"));
				examAttachmentModel.setIdExamAttachment(rs.getString("id_exam_attachment"));

				list.add(examAttachmentModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "idExam " + idExam);

			e.printStackTrace();
		}

		return list;
	}

	public ExamAttachmentModel getExamAttachment(String idExamAttachment) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("GET_EXAM_ATTACHMENT");
		auditTrailModel.setAdditionalInfo("idExamAttachment " + idExamAttachment);
		setAuditTrail(auditTrailModel);

		String sql = "SELECT * FROM EXAM_ATTACHMENT WHERE id_exam_attachment = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idExamAttachment);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ExamAttachmentModel examAttachmentModel = new ExamAttachmentModel();

				examAttachmentModel.setIdExam(rs.getString("id_exam"));
				examAttachmentModel.setAttachmentName(rs.getString("attachment_name"));
				examAttachmentModel.setIdExamAttachment(rs.getString("id_exam_attachment"));

				return examAttachmentModel;

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "idExamAttachment " + idExamAttachment);

			e.printStackTrace();
		}

		return null;
	}

	public List<DeficiencyModel> listDeficiences(String country) {

		List<DeficiencyModel> list = new ArrayList<>();
		String sql = "SELECT * FROM DEFICIENCY";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				DeficiencyModel deficiencyModel = new DeficiencyModel();
				deficiencyModel.setIdDeficiency(rs.getString("id_deficiency"));
				if (country.equals("BRA")) {
					deficiencyModel.setName(rs.getString("name_pt"));
				}

				if (country.equals("FS")) {
					deficiencyModel.setName(rs.getString("name_en"));

				}
				list.add(deficiencyModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			
			setError(e, "country " + country);

			e.printStackTrace();
		}

		return list;
	}

	public boolean bindDeficiency(PatientHasDeficiencyModel patientHasDeficiencyModel) {
		String sql = "INSERT INTO PATIENT_HAS_DEFICIENCY VALUES (?,?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, patientHasDeficiencyModel.getIdPatientHasDeficiency());
			stm.setString(2, patientHasDeficiencyModel.getIdPatient());
			stm.setString(3, patientHasDeficiencyModel.getIdDeficiency());
			stm.setString(4, patientHasDeficiencyModel.getAnotation());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateBindDeficiency(String idPatientHasDeficiency, String anotation) {
		String sql = "UPDATE PATIENT_HAS_DEFICIENCY SET anotation = ? WHERE id_patient_has_deficiency = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, anotation);
			stm.setString(2, idPatientHasDeficiency);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean unbindDeficiency(String idPatientHasDeficiency) {
		String sql = "DELETE FROM PATIENT_HAS_DEFICIENCY WHERE id_patient_has_deficiency = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatientHasDeficiency);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<PatientHasDeficiencyModel> listPatientDeficiencies(String idPatient) {

		List<PatientHasDeficiencyModel> list = new ArrayList<>();
		String sql = " SELECT * FROM PATIENT_HAS_DEFICIENCY " + " WHERE id_patient = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatient);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				PatientHasDeficiencyModel patientHasDiseaseModel = new PatientHasDeficiencyModel();
				patientHasDiseaseModel.setAnotation(rs.getString("anotation"));
				patientHasDiseaseModel.setIdDeficiency(rs.getString("id_deficiency"));
				patientHasDiseaseModel.setIdPatient(idPatient);
				patientHasDiseaseModel.setIdPatientHasDeficiency(rs.getString("id_patient_has_deficiency"));
				list.add(patientHasDiseaseModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<MedicineModel> listMedicines(String search, String country, String language, String status) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_MEDICINES");
		auditTrailModel.setAdditionalInfo("search " + search + " country " + country + " language " +language + " status " + status);
		setAuditTrail(auditTrailModel);
		
		List<MedicineModel> listMedicines = new ArrayList<>();
		String sql = "SELECT TOP 10 * from MEDICINE WHERE name LIKE ? AND country = ? AND status = ? ORDER BY name DESC";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, "%" + search + "%");
			stm.setString(2, country);
			if (status == null) {
				status = "1";
			}
			stm.setString(3, status);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				MedicineModel medicineModel = new MedicineModel();
				medicineModel.setIdMedicine(rs.getString("id_medicine"));
				medicineModel.setName(rs.getString("name"));
				medicineModel.setLanguage(rs.getString("language"));
				medicineModel.setStatus(rs.getString("status"));
				medicineModel.setCountry(rs.getString("country"));

				listMedicines.add(medicineModel);

			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			setError(e, "search " + search + " country " + country + " language " +language + " status " + status);

			e.printStackTrace();
		}

		List<MedicineModel> translationList = new ArrayList<>();

		for (MedicineModel medicineModel : listMedicines) {
			if (!medicineModel.getLanguage().equals(language)) {
				translationList.add(medicineModel);
			}
		}

		StringBuilder builder = new StringBuilder();
		builder.append("{");

		for (MedicineModel medicineModel : translationList) {

			builder.append("'q' : '" + medicineModel.getName() + "',");

		}
		builder.append("'target' : '" + language + "'");
		builder.append("}");

		List<String> translateList = Translate.translate(builder.toString());

		System.out.println(translationList.size() + " ---- " + translateList.size());
		for (int i = 0; i < translationList.size(); i++) {
			translationList.get(i).setName(translateList.get(i));
		}

		for (int i = 0; i < listMedicines.size(); i++) {

			for (int j = 0; j < translateList.size(); j++) {
				if (listMedicines.get(i).getIdMedicine().equals(translationList.get(j).getIdMedicine())) {
					listMedicines.get(i).setName(translationList.get(j).getName());
				}
			}
		}

		return listMedicines;

	}

	public boolean addMedicine(MedicineModel medicineModel) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("INSERT_MEDICINE");
		auditTrailModel.setAdditionalInfo(medicineModel.toString());
		setAuditTrail(auditTrailModel);
		
		
		String sql = "INSERT INTO MEDICINE VALUES (?,?,?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, medicineModel.getIdMedicine());
			stm.setString(2, medicineModel.getName());
			stm.setString(3, medicineModel.getLanguage());
			stm.setString(4, medicineModel.getCountry());
			stm.setString(5, medicineModel.getStatus());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, medicineModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean bindMedicine(PatientUseMedicineModel patientUseMedicineModel) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("BIND_MEDICINE");
		auditTrailModel.setAdditionalInfo(patientUseMedicineModel.toString());
		setAuditTrail(auditTrailModel);
		
		
		String sql = "INSERT INTO PATIENT_USE_MEDICINE VALUES (?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, patientUseMedicineModel.getIdPatientUseMedicine());
			stm.setString(2, patientUseMedicineModel.getIdPatient());
			stm.setString(3, patientUseMedicineModel.getIdMedicine());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, patientUseMedicineModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean unbindMedicine(String idPatient) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("DELETE_BIND_MEDICINE");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient);
		setAuditTrail(auditTrailModel);
		
		String sql = "DELETE FROM PATIENT_USE_MEDICINE WHERE id_patient = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatient);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient);

			e.printStackTrace();
			return false;
		}
	}

	public List<MedicineModel> listPatientMedicines(String idPatient, String language) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_MEDICINES");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient + " language " + language);
		setAuditTrail(auditTrailModel);
		
		List<MedicineModel> listMedicines = new ArrayList<>();
		String sql = "SELECT PATIENT_USE_MEDICINE.id_medicine, MEDICINE.id_medicine, MEDICINE.name, MEDICINE.language, MEDICINE.country, MEDICINE.status FROM PATIENT_USE_MEDICINE INNER JOIN MEDICINE ON "
				+ "MEDICINE.id_medicine = PATIENT_USE_MEDICINE.id_medicine WHERE id_patient = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, idPatient);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				MedicineModel medicineModel = new MedicineModel();
				medicineModel.setIdMedicine(rs.getString("id_medicine"));
				medicineModel.setName(rs.getString("name"));
				medicineModel.setLanguage(rs.getString("language"));
				medicineModel.setStatus(rs.getString("status"));
				medicineModel.setCountry(rs.getString("country"));

				listMedicines.add(medicineModel);

			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient + " language " + language);

			e.printStackTrace();
		}

		List<MedicineModel> translationList = new ArrayList<>();

		for (MedicineModel medicineModel : listMedicines) {
			if (!medicineModel.getLanguage().equals(language)) {
				translationList.add(medicineModel);
			}
		}

		StringBuilder builder = new StringBuilder();
		builder.append("{");

		for (MedicineModel medicineModel : translationList) {

			builder.append("'q' : '" + medicineModel.getName() + "',");

		}
		builder.append("'target' : '" + language + "'");
		builder.append("}");

		List<String> translateList = Translate.translate(builder.toString());

		System.out.println(translationList.size() + " ---- " + translateList.size());
		for (int i = 0; i < translationList.size(); i++) {
			translationList.get(i).setName(translateList.get(i));
		}

		for (int i = 0; i < listMedicines.size(); i++) {

			for (int j = 0; j < translateList.size(); j++) {
				if (listMedicines.get(i).getIdMedicine().equals(translationList.get(j).getIdMedicine())) {
					listMedicines.get(i).setName(translationList.get(j).getName());
				}
			}
		}

		return listMedicines;
	}
	
	public List<HistoryModel> listPatientHistory(String idPatient) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PATIENT");
		auditTrailModel.setEventName("LIST_HISTORY");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient);
		setAuditTrail(auditTrailModel);
		
		
		List<HistoryModel> list = new ArrayList<>();
		String sql = "select E.id_exam AS id, e.id_patient, E.anotation, E.date_exam as date, E.id_physician, E.id_health_institution, U.name as physician_name, U.photo AS physician_photo, H.name as health_institution_name, H.latitute, H.longitute, H.photo as health_institution_photo, 1 AS type "+
  " FROM EXAM as E "+
  " INNER JOIN HEALTH_INSTITUTION AS H ON h.id_health_institution = E.id_health_institution "+
  " INNER JOIN PHYSICIAN AS P ON P.id_physician = E.id_physician "+
  " INNER JOIN (select  name, id_physician, country, photo from PHYSICIAN INNER JOIN USER_HEALTH  "+
  " ON USER_HEALTH.id_user = PHYSICIAN.id_user) AS U ON P.id_physician = U.id_physician "
  + " WHERE id_patient = ? "+
  " UNION ALL  "+
  " select D.id_diagnosis, D.id_patient, D.anotation, D.date_diagnosis, D.id_physician, D.id_health_institution , U.name, U.photo, H.name, H.latitute, H.longitute, H.photo, 2 AS type "+
  " FROM DIAGNOSIS  as D "+
  " INNER JOIN HEALTH_INSTITUTION AS H ON h.id_health_institution = d.id_health_institution "+
  " INNER JOIN PHYSICIAN AS P ON P.id_physician = d.id_physician "+
  " INNER JOIN (select  name, id_physician, country, photo from PHYSICIAN INNER JOIN USER_HEALTH  "+
  " ON USER_HEALTH.id_user = PHYSICIAN.id_user) AS U ON P.id_physician = U.id_physician "+
  " WHERE id_patient = ? "+
  " ORDER BY date DESC";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPatient);
			stm.setString(2, idPatient);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				HistoryModel historyModel = new HistoryModel();

				historyModel.setId(rs.getString("id"));
				historyModel.setType(rs.getInt("type"));
				historyModel.setIdPatient(rs.getString("id_patient"));
				historyModel.setIdPhysician(rs.getString("id_physician"));
				historyModel.setIdHealthInstitution(rs.getString("id_health_institution"));
				historyModel.setAnotation(rs.getString("anotation"));
				historyModel.setDate(rs.getString("date"));
				
				historyModel.setPhysicianName(rs.getString("physician_name"));
				historyModel.setHealthInstitutionName(rs.getString("health_institution_name"));
				
				historyModel.setPhysicianPhoto(rs.getString("physician_photo"));
				historyModel.setLatitute(rs.getDouble("latitute"));
				historyModel.setLongitute(rs.getDouble("longitute"));
				historyModel.setHealthInstitutionPhoto(rs.getString("health_institution_photo"));
				
				
				list.add(historyModel);

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient);

			e.printStackTrace();
		}

		return list;
	}
	
	
	
	public List<PhysicianWorksOnHealthInstitutionModel> listKnownPhysicians(String idPatient, String language) {
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_PHYSICIAN_ALREADY_KNOWN");
		auditTrailModel.setAdditionalInfo("idPatient " + idPatient + " language " + language);
		setAuditTrail(auditTrailModel);
		
		try {
			List<PhysicianWorksOnHealthInstitutionModel> physicianList = new ArrayList<PhysicianWorksOnHealthInstitutionModel>();
			PreparedStatement stmt = this.connection.prepareStatement(

" SELECT DISTINCT DIAGNOSIS.id_physician, DIAGNOSIS.id_patient AS id_patient,U.name, U.photo, PHYSICIAN.practice_document, U.id_user, U.city as city, U.state as state, U.country as country   FROM DIAGNOSIS " +
" INNER JOIN PHYSICIAN ON PHYSICIAN.id_physician = DIAGNOSIS.id_physician " + 
" INNER JOIN (select  name, id_physician, country, photo, city, state, USER_HEALTH.id_user from PHYSICIAN INNER JOIN USER_HEALTH " +   
" ON USER_HEALTH.id_user = PHYSICIAN.id_user) AS U ON PHYSICIAN.id_physician = U.id_physician " + 
" WHERE id_patient = ? " + 
" UNION  " +
" SELECT DISTINCT EXAM.id_physician, EXAM.id_patient, U.name, U.photo, PHYSICIAN.practice_document, U.id_user, U.city as city, U.state as state, U.country as country FROM EXAM " + 
" INNER JOIN PHYSICIAN ON PHYSICIAN.id_physician = EXAM.id_physician " + 
" INNER JOIN (select  name, id_physician, country, photo, city, state, USER_HEALTH.id_user from PHYSICIAN INNER JOIN USER_HEALTH "  +  
" ON USER_HEALTH.id_user = PHYSICIAN.id_user) AS U ON PHYSICIAN.id_physician = U.id_physician " +
" WHERE id_patient = ?;"); 
			
			

			stmt.setString(1, idPatient);
			stmt.setString(2, idPatient);

			ResultSet rs = stmt.executeQuery();
			PhysicianDAO physicianDAO = new PhysicianDAO();

			while (rs.next()) {
				PhysicianWorksOnHealthInstitutionModel physicianWorksOnHealthInstitutionModel = new PhysicianWorksOnHealthInstitutionModel();
				physicianWorksOnHealthInstitutionModel.setIdPhysician(rs.getString("id_physician"));
				physicianWorksOnHealthInstitutionModel.setIdUser(rs.getString("id_user"));
				physicianWorksOnHealthInstitutionModel.setName(rs.getString("name"));
				physicianWorksOnHealthInstitutionModel.setPhoto(rs.getString("photo"));
				physicianWorksOnHealthInstitutionModel.setPracticeDocument(rs.getString("practice_document"));
				physicianWorksOnHealthInstitutionModel.setCity(rs.getString("city"));
				physicianWorksOnHealthInstitutionModel.setCountry(rs.getString("country"));
				physicianWorksOnHealthInstitutionModel.setState(rs.getString("state"));
				physicianWorksOnHealthInstitutionModel.setSpecializationList(
				physicianDAO.listPhysicianSpecializationFullData(rs.getString("id_physician"), language));
				physicianList.add(physicianWorksOnHealthInstitutionModel);
			}
			rs.close();
			stmt.close();
			return physicianList;
		} catch (SQLException e) {
			setError(e, "idPatient " + idPatient + " language " + language);
			throw new RuntimeException(e);
		}
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

	public void setHttpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}
}
