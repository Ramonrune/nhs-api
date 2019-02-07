package com.healthsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;

import com.healthsystem.database.DatabaseConnectionFactory;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.DiagnosisHistoryModel;
import com.healthsystem.model.ErrorLogModel;
import com.healthsystem.model.ExamHistoryModel;
import com.healthsystem.model.GraphModel;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.PhysicianAttendanceModel;
import com.healthsystem.model.PhysicianModel;
import com.healthsystem.model.PhysicianWorksOnHealthInstitutionModel;
import com.healthsystem.model.SpecializationModel;
import com.healthsystem.model.UserModel;
import com.healthsystem.util.JwtUtil;

@Resource
public class PhysicianDAO {

	private Connection connection;

	private ErrorLogDAO errorLogDAO = new ErrorLogDAO();
	private AuditTrailDAO auditTrailDAO = new AuditTrailDAO();
	private HttpHeaders httpHeaders;

	private List<PhysicianWorksOnHealthInstitutionModel> physicians;

	public PhysicianDAO() {
		this.connection = DatabaseConnectionFactory.getConnection();
	}

	public boolean add(PhysicianModel physicianModel) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("INSERT_PHYSICIAN");
		auditTrailModel.setAdditionalInfo(physicianModel.toString());
		setAuditTrail(auditTrailModel);

		String sql = "INSERT INTO PHYSICIAN VALUES (?, ?, ?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, physicianModel.getIdPhysician());
			stm.setString(2, physicianModel.getIdUser());
			stm.setString(3, physicianModel.getPracticeDocument());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, physicianModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean update(PhysicianModel physicianModel) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("UPDATE_PHYSICIAN");
		auditTrailModel.setAdditionalInfo(physicianModel.toString());
		setAuditTrail(auditTrailModel);

		String sql = "UPDATE PHYSICIAN SET practice_document = ? WHERE id_physician = ? AND id_user = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, physicianModel.getPracticeDocument());
			stm.setString(2, physicianModel.getIdPhysician());
			stm.setString(3, physicianModel.getIdUser());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, physicianModel.toString());

			e.printStackTrace();
			return false;
		}
	}

	public PhysicianModel getPhysician(String idUser) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("GET_PHYSICIAN");
		auditTrailModel.setAdditionalInfo("idUser " + idUser);
		setAuditTrail(auditTrailModel);

		PhysicianModel physicianModel = null;

		String sql = "SELECT PHYSICIAN.id_physician, PHYSICIAN.practice_document "
				+ " from PHYSICIAN INNER JOIN USER_HEALTH ON " + " PHYSICIAN.id_user = USER_HEALTH.id_user "
				+ " WHERE PHYSICIAN.id_user = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idUser);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				physicianModel = new PhysicianModel();
				physicianModel.setIdPhysician(rs.getString("id_physician"));
				physicianModel.setPracticeDocument(rs.getString("practice_document"));

			}
			rs.close();
			stm.close();

		} catch (SQLException e) {

			setError(e, "idUser " + idUser);

			e.printStackTrace();
		}

		return physicianModel;
	}
	
	public UserModel getUser(String idPhysician) {

		/*AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("GET_PHYSICIAN_DATA");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician);
		setAuditTrail(auditTrailModel);
*/
		UserModel user = null;

		String sql = "SELECT * FROM PHYSICIAN INNER JOIN USER_HEALTH ON USER_HEALTH.id_user = PHYSICIAN.id_user WHERE PHYSICIAN.id_physician = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPhysician);

			ResultSet rs = stm.executeQuery();


			while (rs.next()) {

				user = new UserModel.UserModelBuilder(rs.getString("login"), rs.getString("password"))
						.id(rs.getString("id_user")).name(rs.getString("name")).typeOfUser(rs.getString("type_of_user"))
						.bornDate(rs.getString("born_date")).gender(rs.getString("gender"))
						.postalCode(rs.getString("postal_code")).country(rs.getString("country"))
						.state(rs.getString("state")).city(rs.getString("city")).street(rs.getString("street"))
						.neighborhood(rs.getString("neighborhood")).number(rs.getString("number"))
						.photo(rs.getString("photo")).identityDocument((rs.getString("identity_document")))
						.telephone(rs.getString("telephone")).build();

			}
			
			rs.close();
			stm.close();

		} catch (SQLException e) {

			//setError(e, "idPhysician " + idPhysician);

			e.printStackTrace();
		}

		return user;
	}

	public List<SpecializationModel> listPhysicianSpecialization(String idPhysician) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_PHYSICIAN_SPECIALIZATIONS");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician);
		setAuditTrail(auditTrailModel);

		List<SpecializationModel> listSpecialization = new ArrayList<>();

		String sql = "SELECT " + " PHYSICIAN_HAS_SPECIALIZATION.id_specialization  "
				+ " FROM PHYSICIAN_HAS_SPECIALIZATION "
				+ " INNER JOIN PHYSICIAN ON PHYSICIAN.id_physician =  PHYSICIAN_HAS_SPECIALIZATION.id_physician  "
				+ " INNER JOIN PHYSICIAN_SPECIALIZATION ON PHYSICIAN_SPECIALIZATION.id_specialization = PHYSICIAN_HAS_SPECIALIZATION.id_specialization  "
				+ "  WHERE PHYSICIAN_HAS_SPECIALIZATION.id_physician = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPhysician);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				SpecializationModel specializationModel = new SpecializationModel();
				specializationModel.setIdSpecialization(rs.getString("id_specialization"));

				listSpecialization.add(specializationModel);
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			setError(e, "idPhysician " + idPhysician);

			e.printStackTrace();
		}
		return listSpecialization;

	}

	public List<SpecializationModel> listPhysicianSpecializationFullData(String idPhysician, String language) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_PHYSICIAN_SPECIALIZATIONS_FULL_DATA");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician + " language " + language);
		setAuditTrail(auditTrailModel);

		List<SpecializationModel> listSpecialization = new ArrayList<>();

		String sql = "SELECT P_S.id_specialization, P_S.name_en, P_S.name_pt, P_S.country FROM PHYSICIAN AS P "
				+ " INNER JOIN PHYSICIAN_HAS_SPECIALIZATION AS P_H_S  " + " ON P.id_physician = P_H_S.id_physician  "
				+ " INNER JOIN PHYSICIAN_SPECIALIZATION AS P_S  "
				+ " ON P_H_S.id_specialization = P_S.id_specialization  " + " WHERE P.id_physician = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPhysician);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				SpecializationModel specializationModel = new SpecializationModel();
				specializationModel.setIdSpecialization(rs.getString("id_specialization"));
				specializationModel.setCountry(rs.getString("country"));

				if (language.equals("pt")) {
					specializationModel.setName(rs.getString("name_pt"));
				}

				if (language.equals("en")) {
					specializationModel.setName(rs.getString("name_en"));
				}

				listSpecialization.add(specializationModel);
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {

			setError(e, "idPhysician " + idPhysician + " language " + language);

			e.printStackTrace();
		}
		return listSpecialization;

	}

	public List<SpecializationModel> listSpecialization(String country, String language) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_SPECIALIZATIONS");
		auditTrailModel.setAdditionalInfo("country " + country + " language " + language);
		setAuditTrail(auditTrailModel);

		List<SpecializationModel> listSpecialization = new ArrayList<>();

		String sql = "SELECT * from PHYSICIAN_SPECIALIZATION WHERE country = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, country);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				SpecializationModel specializationModel = new SpecializationModel();
				specializationModel.setIdSpecialization(rs.getString("id_specialization"));
				if (language.equals("PT")) {
					specializationModel.setName(rs.getString("name_pt"));
				}
				if (language.equals("EN")) {
					specializationModel.setName(rs.getString("name_en"));
				}

				listSpecialization.add(specializationModel);
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			setError(e, "country " + country + " language " + language);

			e.printStackTrace();
		}
		return listSpecialization;

	}

	public boolean bindSpecialization(String idPhysician, String idSpecialization) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("BIND_SPECIALIZATION");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician + " idSpecialization " + idSpecialization);
		setAuditTrail(auditTrailModel);

		String sql = "INSERT INTO PHYSICIAN_HAS_SPECIALIZATION VALUES (?, ?, ?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, UUID.randomUUID().toString());
			stm.setString(2, idPhysician);
			stm.setString(3, idSpecialization);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPhysician " + idPhysician + " idSpecialization " + idSpecialization);

			e.printStackTrace();
			return false;
		}
	}
	

	public boolean registerPhysicianAttendance(String idPhysicianAttendance, String dateAttendance, String idPatient,
			String idPhysician, String idHealthInstitution) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("REGISTER_PHYSICIAN_ATTENDANCE");
		auditTrailModel.setAdditionalInfo("idPhysicianAttendance " + idPhysicianAttendance + " dateAttendance " + dateAttendance + " idPhysician " + idPhysician + " idPatient " + idPatient  + " idHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);
		 
		String sql = "INSERT INTO PHYSICIAN_ATTENDANCE VALUES (?, ?, ?, ?, ?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPhysicianAttendance);
			stm.setString(2, dateAttendance);
			stm.setString(3, idPatient);
			stm.setString(4, idPhysician);
			stm.setString(5, idHealthInstitution);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPhysicianAttendance " + idPhysicianAttendance + " dateAttendance " + dateAttendance + " idPhysician " + idPhysician + " idPatient " + idPatient  + " idHealthInstitution " + idHealthInstitution);

			e.printStackTrace();
			return false;
		}
		
	}

	public boolean unbindSpecialization(String idPhysician, String idSpecialization) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("DELETE_BIND_SPECIALIZATION");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician + " idSpecialization " + idSpecialization);
		setAuditTrail(auditTrailModel);

		String sql = "DELETE FROM PHYSICIAN_HAS_SPECIALIZATION WHERE id_physician = ? AND id_specialization = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPhysician);
			stm.setString(2, idSpecialization);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "idPhysician " + idPhysician + " idSpecialization " + idSpecialization);

			e.printStackTrace();
			return false;
		}
	}

	public List<DiagnosisHistoryModel> listDiagnosisHistory(String idPhysician, String idHealthInstitution) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_DIAGNOSIS_HISTORY");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician + " idHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);

		List<DiagnosisHistoryModel> listDiagnosis = new ArrayList<>();

		String sql = "SELECT TOP 10 D.date_diagnosis, D.id_patient, D.anotation, D.id_physician, U.photo, U.name, H.id_health_institution FROM DIAGNOSIS AS D "
				+ " INNER JOIN PATIENT AS P ON P.id_patient = D.id_patient "
				+ " INNER JOIN USER_HEALTH AS U ON U.id_user = P.id_user "
				+ " INNER JOIN HEALTH_INSTITUTION AS H ON H.id_health_institution = D.id_health_institution "
				+ " INNER JOIN PHYSICIAN AS PH ON PH.id_physician = D.id_physician "
				+ " WHERE D.id_physician = ? AND H.id_health_institution = ? " + " ORDER BY D.date_diagnosis DESC";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPhysician);
			stm.setString(2, idHealthInstitution);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				DiagnosisHistoryModel diagnosisHistoryModel = new DiagnosisHistoryModel();
				diagnosisHistoryModel.setDateDiagnosis(rs.getString("date_diagnosis"));
				diagnosisHistoryModel.setName(rs.getString("name"));
				diagnosisHistoryModel.setPhoto(rs.getString("photo"));
				listDiagnosis.add(diagnosisHistoryModel);
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			setError(e, "idPhysician " + idPhysician + " idHealthInstitution " + idHealthInstitution);

			e.printStackTrace();
		}
		return listDiagnosis;

	}

	public List<ExamHistoryModel> listExamHistory(String idPhysician, String idHealthInstitution) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_EXAMS_HISTORY");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician + " idHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);

		List<ExamHistoryModel> listExams = new ArrayList<>();

		String sql = "SELECT TOP 10 E.date_exam, E.id_patient, E.anotation, E.id_physician, U.photo, U.name, H.id_health_institution FROM EXAM AS E "
				+ "INNER JOIN PATIENT AS P ON P.id_patient = E.id_patient "
				+ "INNER JOIN USER_HEALTH AS U ON U.id_user = P.id_user "
				+ "INNER JOIN HEALTH_INSTITUTION AS H ON H.id_health_institution = E.id_health_institution "
				+ "INNER JOIN PHYSICIAN AS PH ON PH.id_physician = E.id_physician "
				+ "WHERE E.id_physician = ? AND H.id_health_institution = ? " + "ORDER BY E.date_exam DESC;";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idPhysician);
			stm.setString(2, idHealthInstitution);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ExamHistoryModel examHistoryModel = new ExamHistoryModel();
				examHistoryModel.setDateExam(rs.getString("date_exam"));
				examHistoryModel.setName(rs.getString("name"));
				examHistoryModel.setPhoto(rs.getString("photo"));
				listExams.add(examHistoryModel);
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			setError(e, "idPhysician " + idPhysician + " idHealthInstitution " + idHealthInstitution);

			e.printStackTrace();
		}
		return listExams;

	}

	public List<PhysicianWorksOnHealthInstitutionModel> listPhysicianInNearArea(double lat, double lng, int km,
			String language, List<String> specializationList) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_PHYSICIAN_IN_NEAR_AREA");
		auditTrailModel.setAdditionalInfo("lat " + lat + " lng " + lng + " km " + km + " language " + language);
		setAuditTrail(auditTrailModel);

		List<SpecializationModel> specializationModelList = new ArrayList<>();

		for (String str : specializationList) {
			SpecializationModel specializationModel = new SpecializationModel();
			specializationModel.setIdSpecialization(str);
			specializationModelList.add(specializationModel);
		}

		try {
			List<PhysicianWorksOnHealthInstitutionModel> physicianList = new ArrayList<PhysicianWorksOnHealthInstitutionModel>();
			PreparedStatement stmt = this.connection.prepareStatement("SELECT_HEALTH_INSTITUTIONS_BY_COORDINATE ?,?,?");

			stmt.setDouble(1, lat);
			stmt.setDouble(2, lng);
			stmt.setInt(3, km);

			ResultSet rs = stmt.executeQuery();

			HealthInstitutionDAO healthInstitutionDAO = new HealthInstitutionDAO();

			while (rs.next()) {

				List<PhysicianWorksOnHealthInstitutionModel> physiciansTempList = healthInstitutionDAO
						.getPhysicians(rs.getString("id_health_institution"), language);

				for (PhysicianWorksOnHealthInstitutionModel physician : physiciansTempList) {

					if(specializationModelList.size() == 0){
						if (!physicianList.contains(physician)) {
							physicianList.add(physician);
						}

					}
					else{
						if (!physicianList.contains(physician)) {

							boolean found = false;
							List<SpecializationModel> listSpecTemp = physician.getSpecializationList();
							for (SpecializationModel specializationModel : listSpecTemp) {
								if (specializationModelList.contains(specializationModel)) {
									found = true;
								}
							}

							if (found) {
								physicianList.add(physician);
							}
						}
					}
			

				}

			}
			rs.close();
			stmt.close();

			return physicianList;
		} catch (SQLException e) {
			setError(e, String.valueOf("Lat: " + lat + " Lng" + lng + " km " + km + " language " + language));

			throw new RuntimeException(e);
		}
	}
	
	
	public List<PhysicianAttendanceModel> listPhysicianAttendance(String idPhysician, String idHealthInstitution, String date) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("PHYSICIAN");
		auditTrailModel.setEventName("LIST_PHYSICIAN_ATTENDANCE");
		auditTrailModel.setAdditionalInfo("idPhysician " + idPhysician + " idHealthInstitution " + " date " + date);
		setAuditTrail(auditTrailModel);

		List<PhysicianAttendanceModel> listPhysicianAttendance = new ArrayList<>();

		String sql = "select P_A.id_physician_attendance, P_A.id_patient, P.id_user, U_H.name, U_H.photo, FORMAT(P_A.date_attendance, 'dd/MM/yyyy HH:mm:ss') as date_attendance, P_A.id_health_institution, P_A.id_physician from PHYSICIAN_ATTENDANCE AS P_A " +
" INNER JOIN PATIENT AS P ON P_A.id_patient = P.id_patient "  +
" INNER JOIN USER_HEALTH AS U_H ON P.id_user = U_H.id_user "+ 
" WHERE P_A.id_health_institution = ? " + 
" AND P_A.id_physician = ? " +  
" AND CONVERT(DATE, P_A.date_attendance) = ?"
+ " ORDER BY date_attendance DESC;";
		
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idHealthInstitution);
			stm.setString(2, idPhysician);
			stm.setString(3, date);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
  
				PhysicianAttendanceModel physicianAttendanceModel = new PhysicianAttendanceModel();
				physicianAttendanceModel.setDateAttendance(rs.getString("date_attendance"));
				physicianAttendanceModel.setName(rs.getString("name"));
				physicianAttendanceModel.setIdPatient(rs.getString("id_patient"));
				physicianAttendanceModel.setIdPhysician(rs.getString("id_physician"));
				physicianAttendanceModel.setPhoto(rs.getString("photo"));
				physicianAttendanceModel.setIdPhysicianAttendance(rs.getString("id_physician_attendance"));
				physicianAttendanceModel.setIdHealthInstitution(rs.getString("id_health_institution"));
				physicianAttendanceModel.setIdUser(rs.getString("id_user"));

				listPhysicianAttendance.add(physicianAttendanceModel);
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			setError(e, "idPhysician " + idPhysician + " idHealthInstitution " + " date " + date);

			e.printStackTrace();
		}
		return listPhysicianAttendance;

	}
	
	
	public List<GraphModel> listPhysicianAttendancesGraph(String idPhysician, String idHealthInstitution){

			List<GraphModel> listGraph = new ArrayList<>();

			String sql = " "+
	"SELECT CONVERT(DATE,  CONVERT(datetime, date_attendance)) AS postedDate, COUNT(*) as quantity " + 
" FROM PHYSICIAN_ATTENDANCE WHERE   " + 
" CONVERT(DATE, date_attendance) >= ? AND  CONVERT(DATE, date_attendance) <= ?  " + 
" AND PHYSICIAN_ATTENDANCE.id_physician = ? AND PHYSICIAN_ATTENDANCE.id_health_institution = ? " + 
" GROUP BY CONVERT(DATE,  CONVERT(datetime, date_attendance))";
			try {
				PreparedStatement stm = connection.prepareStatement(sql);

				Date today = new Date();

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(today);

				calendar.add(Calendar.MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.DATE, -1);

				Date lastDayOfMonth = calendar.getTime();

				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				Calendar firstDay = Calendar.getInstance();
				firstDay.set(Calendar.DAY_OF_MONTH, 1);

				Date firstDayOfMonth = firstDay.getTime();

				stm.setString(1, sdf.format(firstDayOfMonth));
				stm.setString(2, sdf.format(lastDayOfMonth));
				stm.setString(3, idPhysician);
				stm.setString(4, idHealthInstitution);

				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					GraphModel graphModel = new GraphModel();
					graphModel.setDate(rs.getString("postedDate"));
					graphModel.setQuantity(rs.getInt("quantity"));
					  
					System.out.println(rs.getString("postedDate") + " ---- " + rs.getInt("quantity"));
					listGraph.add(graphModel);
				}

				Calendar start = Calendar.getInstance();
				start.setTime(firstDayOfMonth);
				Calendar end = Calendar.getInstance();
				end.setTime(lastDayOfMonth);

				while (!start.after(end)) {
					Date targetDay = start.getTime();

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

					 
					GraphModel graphModel = new GraphModel();
					graphModel.setDate(df.format(targetDay)); 
					if (!listGraph.contains(graphModel)) {
						GraphModel errorGraphModel = new GraphModel();
						errorGraphModel.setDate(df.format(targetDay));
						errorGraphModel.setQuantity(0);

						listGraph.add(errorGraphModel);
					}
					
					  

					start.add(Calendar.DATE, 1);
				}

				Collections.sort(listGraph, new Comparator<GraphModel>() {
					public int compare(GraphModel s1, GraphModel s2) {
						return s1.getDate().compareTo(s2.getDate());
					}
				});

				rs.close();
				stm.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			return listGraph;

		
		
	}

	private void setError(Exception e, String additionalInfo) {
		ErrorLogModel errorLogModel = new ErrorLogModel();
		errorLogModel.setClassName(e.getStackTrace()[0].getClassName());
		errorLogModel.setLineNumber(e.getStackTrace()[0].getLineNumber());
		errorLogModel.setMessage(e.getMessage());
		errorLogModel.setMethodName(e.getStackTrace()[0].getMethodName());
		errorLogModel.setNameOfFile(e.getStackTrace()[0].getFileName());
		errorLogModel.setAdditionalInfo(additionalInfo);
		if (httpHeaders != null) {
			errorLogModel.setUserId(JwtUtil.getUserId(httpHeaders.getHeaderString("Authorization")));

		}
		errorLogDAO.add(errorLogModel);

	}

	private void setAuditTrail(AuditTrailModel auditTrailModel) {

		if (httpHeaders != null) {
			auditTrailModel.setIdUser(JwtUtil.getUserId(httpHeaders.getHeaderString("Authorization")));

		}
		auditTrailDAO.add(auditTrailModel);

	}

	public void setHttpHeaders(HttpHeaders httpHeaders) {
		this.httpHeaders = httpHeaders;
	}


}
