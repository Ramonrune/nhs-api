package com.healthsystem.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.healthsystem.database.DatabaseConnectionFactory;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.DiagnosisModel;
import com.healthsystem.model.ErrorLogModel;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.NurseWorksOnHealthInstitutionModel;
import com.healthsystem.model.PhysicianWorksOnHealthInstitutionModel;
import com.healthsystem.model.UserModel;
import com.healthsystem.util.Country;
import com.healthsystem.util.JwtUtil;
import com.healthsystem.util.Pair;
import com.healthsystem.util.RabbitMQConnectionManager;

@Resource
public class HealthInstitutionDAO {

	private Connection connection;
	private ErrorLogDAO errorLogDAO = new ErrorLogDAO();
	private AuditTrailDAO auditTrailDAO = new AuditTrailDAO();
	private HttpHeaders httpHeaders;

	public HealthInstitutionDAO() {
		this.connection = DatabaseConnectionFactory.getConnection();
	}

	public boolean add(HealthInstitutionModel healthInstitution) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("INSERT_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo(healthInstitution.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO HEALTH_INSTITUTION " + "(" + "id_health_institution, " + "identity_code," + "name,"
				+ "postal_code," + "country," + "state," + "city," + "street," + "neighborhood," + "number," + "photo, telephone,"
				+ "latitute," + "longitute)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?, ?);";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			System.out.println(healthInstitution.getIdentityCode());

			System.out.println(healthInstitution.getPostalCode());
			System.out.println(healthInstitution.getCountry());

			stm.setString(1, healthInstitution.getIdHealthInstitution());
			stm.setString(2, healthInstitution.getIdentityCode());
			stm.setString(3, healthInstitution.getName());
			stm.setString(4, healthInstitution.getPostalCode());
			stm.setString(5, healthInstitution.getCountry());
			stm.setString(6, healthInstitution.getState());
			stm.setString(7, healthInstitution.getCity());
			stm.setString(8, healthInstitution.getStreet());
			stm.setString(9, healthInstitution.getNeighborhood());
			stm.setString(10, healthInstitution.getNumber());
			stm.setString(11, healthInstitution.getPhoto());
			stm.setString(12, healthInstitution.getTelephone());
			
			

			String urlParam = URLEncoder.encode(Normalizer
					.normalize(healthInstitution.getStreet() + "," + healthInstitution.getNeighborhood() + ", " + healthInstitution.getNumber() + healthInstitution.getCity() + ","
							+ healthInstitution.getState() + "," + healthInstitution.getPostalCode() + ","
							+ Country.getName(healthInstitution.getCountry()), Normalizer.Form.NFD)
					.replaceAll("[^\\p{ASCII}]", ""), "UTF-8");
			
			
			Pair<Double, Double> pair = coordinate(urlParam);

			if (pair.lat == 0 && pair.lng == 0) {
				return false;
			}
			stm.setDouble(13, pair.lat);
			stm.setDouble(14, pair.lng);
			

			stm.execute();
			stm.close();
			
			
			return true;
		} catch (SQLException | UnsupportedEncodingException | IllegalStateException e) {
			
			
			setError(e, healthInstitution.toString());
			return false;
		}
	}

	public boolean update(HealthInstitutionModel healthInstitution) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("UPDATE_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo(healthInstitution.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "UPDATE HEALTH_INSTITUTION SET " + "identity_code = ?," + "name = ?," + "postal_code = ?,"
				+ "country = ?," + "state = ?," + "city = ?," + "street = ?," + "neighborhood = ?," + "number = ?,"
				+ "photo = ?, telephone = ?, " + "latitute = ?," + "longitute = ?"
				+ " WHERE id_health_institution = ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, healthInstitution.getIdentityCode());
			stm.setString(2, healthInstitution.getName());
			stm.setString(3, healthInstitution.getPostalCode());
			stm.setString(4, healthInstitution.getCountry());
			stm.setString(5, healthInstitution.getState());
			stm.setString(6, healthInstitution.getCity());
			stm.setString(7, healthInstitution.getStreet());
			stm.setString(8, healthInstitution.getNeighborhood());
			stm.setString(9, healthInstitution.getNumber());
			stm.setString(10, healthInstitution.getPhoto());
			stm.setString(11, healthInstitution.getTelephone());

			String urlParam = URLEncoder.encode(Normalizer
					.normalize(healthInstitution.getStreet() + "," + healthInstitution.getNeighborhood() + ", "
							+ healthInstitution.getNumber() + healthInstitution.getCity() + ","
							+ healthInstitution.getState() + "," + healthInstitution.getPostalCode() + ","
							+ Country.getName(healthInstitution.getCountry()), Normalizer.Form.NFD)
					.replaceAll("[^\\p{ASCII}]", ""), "UTF-8");

			Pair<Double, Double> pair = coordinate(urlParam);

			if (pair.lat == 0 && pair.lng == 0) {
				return false;
			}

			stm.setDouble(12, pair.lat);
			stm.setDouble(13, pair.lng);
			stm.setString(14, healthInstitution.getIdHealthInstitution());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException | UnsupportedEncodingException e) {
			setError(e, healthInstitution.toString());

			e.printStackTrace();
			return false;
		}
	}

	public List<HealthInstitutionModel> getHealthInstitutions(double lat, double lng, int km) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("LIST_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("lat " + lat + " lng " + lng + " km " + km);
		setAuditTrail(auditTrailModel);
		
		try {
			List<HealthInstitutionModel> healthInstitutionList = new ArrayList<HealthInstitutionModel>();
			PreparedStatement stmt = this.connection.prepareStatement("SELECT_HEALTH_INSTITUTIONS_BY_COORDINATE ?,?,?");

			stmt.setDouble(1, lat);
			stmt.setDouble(2, lng);
			stmt.setInt(3, km);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				System.out.println(rs.getDouble("latitute") + " ------------------------------");

				HealthInstitutionModel healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(
						rs.getString("id_health_institution"), rs.getString("name"))
								.identityCode(rs.getString("identity_code")).postalCode(rs.getString("postal_code"))
								.country(rs.getString("country")).state(rs.getString("state"))
								.city(rs.getString("city")).street(rs.getString("street"))
								.neighborhood(rs.getString("neighborhood")).number(rs.getString("number"))
								.photo(rs.getString("photo")).telephone(rs.getString("telephone"))
								.latitute((rs.getDouble("latitute"))).longitute((rs.getDouble("longitute"))).build();

				healthInstitutionList.add(healthInstitutionModel);
			}
			rs.close();
			stmt.close();
			

			return healthInstitutionList;
		} catch (SQLException e) {
			setError(e, String.valueOf("Lat: " + lat + " Lng" + lng + " km " + km));

			throw new RuntimeException(e);
		}
	}

	public List<HealthInstitutionModel> getHealthInstitutions(int start, int end, String country, String search) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("LIST_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("start " + start + " end " + end + " country " + country + " search " + search);
		setAuditTrail(auditTrailModel);
	 
		try {
			List<HealthInstitutionModel> healthInstitutionList = new ArrayList<HealthInstitutionModel>();

			PreparedStatement stmt = null;
			if (country == null && search == null) {
				stmt = this.connection
						.prepareStatement("SELECT  id_health_institution, latitute, longitute, name,identity_code, country,state,city   "
								+ "FROM    ( SELECT  ROW_NUMBER() OVER ( ORDER BY country) AS RowNum, * "
								+ " FROM      HEALTH_INSTITUTION ) AS RowConstrainedResult " + " WHERE   RowNum >= ? "
								+ " AND RowNum <= ? " + "ORDER BY RowNum;");
				stmt.setInt(1, start);
				stmt.setInt(2, end);
			}

			if (country != null && search == null) {
				stmt = this.connection
						.prepareStatement("SELECT  id_health_institution, latitute, longitute, name,identity_code, country,state,city   "
								+ "FROM    ( SELECT  ROW_NUMBER() OVER ( ORDER BY country) AS RowNum, * "
								+ " FROM      HEALTH_INSTITUTION WHERE country = ? ) AS RowConstrainedResult "
								+ " WHERE   RowNum >= ? " + " AND RowNum <= ? " + "ORDER BY RowNum;");
				stmt.setString(1, country);
				stmt.setInt(2, start);
				stmt.setInt(3, end);
			}
			if (country != null && search != null) {
				stmt = this.connection
						.prepareStatement("SELECT  id_health_institution, latitute, longitute, name,identity_code, country,state,city   "
								+ "FROM    ( SELECT  ROW_NUMBER() OVER ( ORDER BY country) AS RowNum, * "
								+ " FROM      HEALTH_INSTITUTION WHERE country = ? AND "
								+ "(name LIKE ? OR identity_code LIKE ?)) AS RowConstrainedResult "
								+ " WHERE   RowNum >= ? " + " AND RowNum <= ? " + "ORDER BY RowNum;");
				stmt.setString(1, country);
				stmt.setString(2, "%" + search + "%");
				stmt.setString(3, "%" + search + "%");
				stmt.setInt(4, start);
				stmt.setInt(5, end);
			}
			if (country == null && search != null) {
				stmt = this.connection
						.prepareStatement("SELECT  id_health_institution, latitute, longitute, name,identity_code, country,state,city   "
								+ "FROM    ( SELECT  ROW_NUMBER() OVER ( ORDER BY country) AS RowNum, * "
								+ " FROM      HEALTH_INSTITUTION WHERE "
								+ "(name LIKE ? OR identity_code LIKE ?)) AS RowConstrainedResult "
								+ " WHERE   RowNum >= ? " + " AND RowNum <= ? " + "ORDER BY RowNum;");
				stmt.setString(1, "%" + search + "%");
				stmt.setString(2, "%" + search + "%");
				stmt.setInt(3, start);
				stmt.setInt(4, end);
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				HealthInstitutionModel healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(
						rs.getString("id_health_institution"), rs.getString("name"))
								.identityCode(rs.getString("identity_code")).country(rs.getString("country"))
								.state(rs.getString("state")).city(rs.getString("city")).latitute(rs.getDouble("latitute")).longitute(rs.getDouble("longitute")).build();

				healthInstitutionList.add(healthInstitutionModel);
			}
			rs.close();
			stmt.close();
			return healthInstitutionList;
		} catch (SQLException e) {
			setError(e, "Start " + start + " End" + end + " Country " + country + " Search " + search);

			throw new RuntimeException(e);
		}
	}

	private Pair<Double, Double> coordinate(String formatVal) {

		System.out.println("aquiiiiii");
		System.out.println(formatVal);
		Client newClient = ClientBuilder.newClient();
		Invocation invoke = newClient
				.target("https://maps.googleapis.com/maps/api/geocode/json?address=" + formatVal
						+ "&key=AIzaSyBvd3Lh7LOeEM7fM1chIR2C5GA2DKjC6_E")
				.request(MediaType.APPLICATION_JSON).buildGet();
		Pair<Double, Double> pair = new Pair<Double, Double>(0.0, 0.0);
		Response response = invoke.invoke();

		if (response.getStatus() == 200) {
			String jsonStr = response.readEntity(String.class);

			System.out.println(jsonStr);
			JSONObject jsonObject = new JSONObject(jsonStr);

			if (jsonObject.getString("status").equals("OK")) {

				JSONArray result = jsonObject.getJSONArray("results");
				JSONObject obj = result.getJSONObject(0);
				JSONObject geometry = obj.getJSONObject("geometry");
				JSONObject location = geometry.getJSONObject("location");

				pair.lat = location.getDouble("lat");
				pair.lng = location.getDouble("lng");

			}

		}

		return pair;
	}

	public boolean delete(String idHealthInstitution) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("DELETE_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("idHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);
		

		String sql = "delete from HEALTH_INSTITUTION where id_health_institution = ?";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idHealthInstitution);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "Delete " + idHealthInstitution);

			e.printStackTrace();
			return false;
		}
	}

	public HealthInstitutionModel getHealthInstitution(String idHealthInstitution) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("GET_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("idHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);
		
		
		try {

			PreparedStatement stmt = null;
			stmt = this.connection
					.prepareStatement("SELECT  * FROM HEALTH_INSTITUTION WHERE id_health_institution = ?");

			stmt.setString(1, idHealthInstitution);

			ResultSet rs = stmt.executeQuery();

			HealthInstitutionModel healthInstitutionModel = null;
			while (rs.next()) {

				healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(
						rs.getString("id_health_institution"), rs.getString("name"))
								.identityCode(rs.getString("identity_code")).postalCode(rs.getString("postal_code"))
								.country(rs.getString("country")).state(rs.getString("state"))
								.city(rs.getString("city")).street(rs.getString("street"))
								.neighborhood(rs.getString("neighborhood")).number(rs.getString("number"))
								.photo(rs.getString("photo")).telephone(rs.getString("telephone")).build();

			}
			rs.close();
			stmt.close();
			return healthInstitutionModel;
		} catch (SQLException e) {
			setError(e, "Get " + idHealthInstitution);

			throw new RuntimeException(e);
		}
	}

	public boolean containsBind(String idUser, String idHealthInstitution) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("CONTAINS_BIND_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("Id user" + idUser + " IdHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);
		
		String sql = "select count(*) from USER_AND_HEALTH_INSTITUTION where id_user = ? AND id_health_institution = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			System.out.println(stm.toString());

			stm.setString(1, idUser);
			stm.setString(2, idHealthInstitution);

			ResultSet rs = stm.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				return true;
			}

			return false;

		} catch (SQLException e) {
			setError(e, "Id user" + idUser + " IdHealthInstitution " + idHealthInstitution);

			return false;
		}
	}

	public boolean addBind(String idHealthInstitution, String idUser, String status) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("BIND_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("Id user" + idUser + " IdHealthInstitution " + idHealthInstitution + " status " + status);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "INSERT INTO USER_AND_HEALTH_INSTITUTION VALUES (?, ?, ?, ?);";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, UUID.randomUUID().toString());
			stm.setString(2, idUser);
			stm.setString(3, idHealthInstitution);
			stm.setString(4, status);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "Id health institution " + idHealthInstitution + " Id user" + idUser + " Status " + status);

			e.printStackTrace();
			return false;
		}
	}

	public boolean updateBind(String idHealthInstitution, String idUser, String status) {
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("UPDATE_BIND_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("Id user" + idUser + " IdHealthInstitution " + idHealthInstitution + " status " + status);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "UPDATE USER_AND_HEALTH_INSTITUTION SET status = ? WHERE id_health_institution = ? AND id_user = ?";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, status);
			stm.setString(2, idHealthInstitution);
			stm.setString(3, idUser);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "Id health institution " + idHealthInstitution + " Id user" + idUser + " Status " + status);

			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteBind(String idHealthInstitution, String idUser) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("DELETE_BIND_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("Id user" + idUser + " IdHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);
		
		
		String sql = "delete from USER_AND_HEALTH_INSTITUTION where id_health_institution = ? AND id_user = ?";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, idHealthInstitution);
			stm.setString(2, idUser);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "Id health institution " + idHealthInstitution + " idUser" + idUser);

			e.printStackTrace();
			return false;
		}
	}

	public List<UserModel> getBindedUsers(String idHealthInstitution, String status) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("LIST_BINDED_USERS_FROM_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("status " + status + " IdHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);
		
		
		try {
			List<UserModel> userList = new ArrayList<UserModel>();
			PreparedStatement stmt = this.connection.prepareStatement(
					"SELECT USER_HEALTH.id_user, USER_HEALTH.login, USER_HEALTH.name, USER_HEALTH.photo, USER_HEALTH.identity_document, USER_HEALTH.country, USER_HEALTH.state, USER_HEALTH.city, USER_HEALTH.type_of_user "
							+ " FROM USER_AND_HEALTH_INSTITUTION INNER JOIN HEALTH_INSTITUTION ON USER_AND_HEALTH_INSTITUTION.id_health_institution = HEALTH_INSTITUTION.id_health_institution "
							+ " INNER JOIN USER_HEALTH ON USER_AND_HEALTH_INSTITUTION.id_user = USER_HEALTH.id_user "
							+ " WHERE USER_AND_HEALTH_INSTITUTION.id_health_institution = ? AND status = ?");

			stmt.setString(1, idHealthInstitution);
			stmt.setString(2, status);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				UserModel userModel = new UserModel.UserModelBuilder(rs.getString("login"), "")
						.id(rs.getString("id_user")).name(rs.getString("name")).photo(rs.getString("photo"))
						.identityDocument(rs.getString("identity_document")).country(rs.getString("country"))
						.state(rs.getString("state")).city(rs.getString("city"))
						.typeOfUser(rs.getString("type_of_user")).build();

				userList.add(userModel);
			}
			rs.close();
			stmt.close();
			return userList;
		} catch (SQLException e) {
			setError(e, "Id Health Institution " + idHealthInstitution + " Status " + status);

			throw new RuntimeException(e);
		}
	}

	public List<PhysicianWorksOnHealthInstitutionModel> getPhysicians(String idHealthInstitution, String language) {
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("LIST_PHYSICIANS_FROM_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("idHealthInstitution " + idHealthInstitution + " language " + language);
		setAuditTrail(auditTrailModel);
		
		try {
			List<PhysicianWorksOnHealthInstitutionModel> physicianList = new ArrayList<PhysicianWorksOnHealthInstitutionModel>();
			PreparedStatement stmt = this.connection.prepareStatement(
					"SELECT P.id_physician, U.id_user, U.name, U.city, U.country, U.city, U.state, P.practice_document, U.photo from PHYSICIAN AS P INNER JOIN USER_HEALTH AS U "
							+ "ON P.id_user = U.id_user " + "INNER JOIN USER_AND_HEALTH_INSTITUTION AS U_A_H_I "
							+ "ON U_A_H_I.id_user = U.id_user " + "WHERE U_A_H_I.id_health_institution = ? "
							+ "AND U_A_H_I.status = ?;");

			stmt.setString(1, idHealthInstitution);
			stmt.setString(2, "1");

			ResultSet rs = stmt.executeQuery();
			PhysicianDAO physicianDAO = new PhysicianDAO();

			while (rs.next()) {
				PhysicianWorksOnHealthInstitutionModel physicianWorksOnHealthInstitutionModel = new PhysicianWorksOnHealthInstitutionModel();
				physicianWorksOnHealthInstitutionModel.setCity(rs.getString("city"));
				physicianWorksOnHealthInstitutionModel.setCountry(rs.getString("country"));
				physicianWorksOnHealthInstitutionModel.setIdPhysician(rs.getString("id_physician"));
				physicianWorksOnHealthInstitutionModel.setIdUser(rs.getString("id_user"));
				physicianWorksOnHealthInstitutionModel.setName(rs.getString("name"));
				physicianWorksOnHealthInstitutionModel.setState(rs.getString("state"));
				physicianWorksOnHealthInstitutionModel.setPhoto(rs.getString("photo"));
				physicianWorksOnHealthInstitutionModel.setPracticeDocument(rs.getString("practice_document"));
				physicianWorksOnHealthInstitutionModel.setSpecializationList(
				physicianDAO.listPhysicianSpecializationFullData(rs.getString("id_physician"), language));
				physicianList.add(physicianWorksOnHealthInstitutionModel);
			}
			rs.close();
			stmt.close();
			return physicianList;
		} catch (SQLException e) {
			setError(e, "Id health institution " + idHealthInstitution + " Language" + language);
			throw new RuntimeException(e);
		}
	}
	
	
	public List<NurseWorksOnHealthInstitutionModel> getNurses(String idHealthInstitution) {
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("LIST_NURSES_FROM_HEALTH_INSTITUTION");
		auditTrailModel.setAdditionalInfo("idHealthInstitution " + idHealthInstitution);
		setAuditTrail(auditTrailModel);
		
		try {
			List<NurseWorksOnHealthInstitutionModel> nurseList = new ArrayList<NurseWorksOnHealthInstitutionModel>();
			PreparedStatement stmt = this.connection.prepareStatement(
					"SELECT N.id_nurse, N.nurse_type, U.id_user, U.name, U.city, U.country, U.city, U.state, N.nurse_code, N.nurse_type, U.photo from NURSE AS N INNER JOIN USER_HEALTH AS U "
							+ "ON N.id_user = U.id_user " + "INNER JOIN USER_AND_HEALTH_INSTITUTION AS U_A_H_I "
							+ "ON U_A_H_I.id_user = U.id_user " + "WHERE U_A_H_I.id_health_institution = ? "
							+ "AND U_A_H_I.status = ?;");
			 
			stmt.setString(1, idHealthInstitution);
			stmt.setString(2, "1");

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				
				NurseWorksOnHealthInstitutionModel nurseWorksOnHealthInstitutionModel = new NurseWorksOnHealthInstitutionModel();
				
				nurseWorksOnHealthInstitutionModel.setCity(rs.getString("city"));
				nurseWorksOnHealthInstitutionModel.setCountry(rs.getString("country"));
				nurseWorksOnHealthInstitutionModel.setIdNurse(rs.getString("id_nurse"));
				nurseWorksOnHealthInstitutionModel.setIdUser(rs.getString("id_user"));
				nurseWorksOnHealthInstitutionModel.setName(rs.getString("name"));
				nurseWorksOnHealthInstitutionModel.setState(rs.getString("state"));
				nurseWorksOnHealthInstitutionModel.setPhoto(rs.getString("photo"));
				nurseWorksOnHealthInstitutionModel.setNurseCode(rs.getString("nurse_code"));
				nurseWorksOnHealthInstitutionModel.setNurseType(rs.getString("nurse_type"));

				nurseList.add(nurseWorksOnHealthInstitutionModel);
			}
			rs.close();
			stmt.close();
			return nurseList;
		} catch (SQLException e) {
			setError(e, "Id health institution " + idHealthInstitution);
			throw new RuntimeException(e);
		}
	}
	
	public boolean addToWaitList(String idHealthInstitution, String idDiagnosis) {
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("HEALTH_INSTITUTION");
		auditTrailModel.setEventName("ADD_TO_WAIT_LIST");
		auditTrailModel.setAdditionalInfo("idHealthInstitution " + idHealthInstitution + " idDiagnosis " + idDiagnosis);
		setAuditTrail(auditTrailModel);
		

		try {
			
			DiagnosisModel diagnosis = new PatientDAO().getDiagnosis(idDiagnosis);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(diagnosis);
			
			System.out.println(json  +" ------");
			RabbitMQConnectionManager rabbitMQConnectionManager = new RabbitMQConnectionManager(idHealthInstitution);
			rabbitMQConnectionManager.publish(idDiagnosis, json);
			
			return true;
		} catch (Exception e) {
			//setError(e, "idHealthInstitution " + idHealthInstitution + " idDiagnosis " + idDiagnosis);
			e.printStackTrace();
			return false;
		
		}
	
		
	}

	
	public boolean getWaitList(String idHealthInstitution) {
		/*	AuditTrailModel auditTrailModel = new AuditTrailModel();
			auditTrailModel.setCategory("HEALTH_INSTITUTION");
			auditTrailModel.setEventName("ADD_TO_WAIT_LIST");
			auditTrailModel.setAdditionalInfo("idHealthInstitution " + idHealthInstitution + " idDiagnosis " + idDiagnosis);
			setAuditTrail(auditTrailModel);
			*/

			try {
				RabbitMQConnectionManager rabbitMQConnectionManager = new RabbitMQConnectionManager(idHealthInstitution);
				rabbitMQConnectionManager.consume();
				
				return true;
			} catch (Exception e) {
				//setError(e, "idHealthInstitution " + idHealthInstitution);
				e.printStackTrace();
				return false;
			
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