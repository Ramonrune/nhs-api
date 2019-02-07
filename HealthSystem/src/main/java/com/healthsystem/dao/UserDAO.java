package com.healthsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.HttpHeaders;

import com.healthsystem.database.DatabaseConnectionFactory;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.ErrorLogModel;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.UserModel;
import com.healthsystem.util.JwtUtil;
import com.healthsystem.util.SecretCodeGenerator;

@Resource
public class UserDAO {

	private Connection connection;
	private ErrorLogDAO errorLogDAO = new ErrorLogDAO();
	private AuditTrailDAO auditTrailDAO = new AuditTrailDAO();
	private HttpHeaders httpHeaders;

	public UserDAO() {
		this.connection = DatabaseConnectionFactory.getConnection();
	}

	public boolean add(UserModel user) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("INSERT_USER");
		auditTrailModel.setAdditionalInfo(user.toString());
		setAuditTrail(auditTrailModel);
		
		String sql = "INSERT INTO USER_HEALTH (id_user, login,password,name,type_of_user,born_date,gender,postal_code,country,state,city,street,neighborhood,number,photo,identity_document, telephone, secret_code)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			System.out.println(user);

			stm.setString(1, user.getId());
			stm.setString(2, user.getLogin());
			stm.setString(3, user.getPassword());
			stm.setString(4, user.getName());
			stm.setString(5, user.getTypeOfUser());
			if (user.getBornDate().isEmpty()) {
				stm.setNull(6, Types.DATE);
			} else {
				stm.setString(6, user.getBornDate());

			}
			stm.setString(7, user.getGender());
			stm.setString(8, user.getPostalCode());
			stm.setString(9, user.getCountry());
			stm.setString(10, user.getState());
			stm.setString(11, user.getCity());
			stm.setString(12, user.getStreet());
			stm.setString(13, user.getNeighborhood());
			stm.setString(14, user.getNumber());
			stm.setString(15, user.getPhoto());
			stm.setString(16, user.getIdentityDocument());
			stm.setString(17, user.getTelephone());
			String generateSecretCode = SecretCodeGenerator.generateSecretCode();
			stm.setString(18, generateSecretCode);

			sendSecretCode(user.getLogin(), generateSecretCode, user.getCountry());

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			
			setError(e, user.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean updateLogin(String login, String id) {

		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("UPDATE_LOGIN");
		auditTrailModel.setAdditionalInfo("login " + login + " id " + id);
		setAuditTrail(auditTrailModel);
		
		String sql = "UPDATE USER_HEALTH SET login = ? WHERE id_user = ?;";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, login);
			stm.setString(2, id);

			stm.execute();
			stm.close();
			return true;

		} catch (Exception e) {
			
			setError(e, "login " + login + " id " + id);

			e.printStackTrace();
			return false;
		}
	}

	public boolean update(UserModel user) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("UPDATE_USER");
		auditTrailModel.setAdditionalInfo(user.toString());
		setAuditTrail(auditTrailModel);

		try {
			String sql = "";

			sql = "UPDATE USER_HEALTH " + "SET " + "password = ?," + "name = ?, " + "born_date = ?," + "gender = ?,"
					+ "postal_code = ?," + "" + " state = ?," + "city = ?," + "street = ?," + "neighborhood = ?,"
					+ "number = ?," + "photo =?," + "identity_document = ?, telephone= ?, country= ? "
					+ "WHERE id_user = ?;";

			if (user.getPassword() == null) {

				sql = "UPDATE USER_HEALTH " + "SET " + "name = ?, " + "born_date = ?," + "gender = ?,"
						+ "postal_code = ?," + "" + " state = ?," + "city = ?," + "street = ?," + "neighborhood = ?,"
						+ "number = ?," + "photo =?," + "identity_document = ?, telephone= ?, country = ? "
						+ "WHERE id_user = ?;";
			}

			if (user.getIdentityDocument() == null) {

				sql = "UPDATE USER_HEALTH " + "SET " + "password = ?," + "name = ?, " + "born_date = ?," + "gender = ?,"
						+ "postal_code = ?," + "" + " state = ?," + "city = ?," + "street = ?," + "neighborhood = ?,"
						+ "number = ?," + "photo =?," + " telephone= ?, country=? " + "WHERE id_user = ?;";

			}

			if (user.getPassword() == null && user.getIdentityDocument() == null) {

				sql = "UPDATE USER_HEALTH " + "SET " + "name = ?, " + "born_date = ?," + "gender = ?,"
						+ "postal_code = ?," + "" + " state = ?," + "city = ?," + "street = ?," + "neighborhood = ?,"
						+ "number = ?," + "photo =?," + " telephone= ?, country=? " + "WHERE id_user = ?;";
			}

			PreparedStatement stm = connection.prepareStatement(sql);
			if (user.getPassword() == null && user.getIdentityDocument() != null) {

				stm.setString(1, user.getName());
				stm.setString(2, user.getBornDate());
				stm.setString(3, user.getGender());
				stm.setString(4, user.getPostalCode());
				stm.setString(5, user.getState());
				stm.setString(6, user.getCity());
				stm.setString(7, user.getStreet());
				stm.setString(8, user.getNeighborhood());
				stm.setString(9, user.getNumber());
				stm.setString(10, user.getPhoto());
				stm.setString(11, user.getIdentityDocument());
				stm.setString(12, user.getTelephone());
				stm.setString(13, user.getCountry());
				stm.setString(14, user.getId());
			}

			if (user.getPassword() != null && user.getIdentityDocument() == null) {

				stm.setString(1, user.getPassword());
				stm.setString(2, user.getName());
				stm.setString(3, user.getBornDate());
				stm.setString(4, user.getGender());
				stm.setString(5, user.getPostalCode());
				stm.setString(6, user.getState());
				stm.setString(7, user.getCity());
				stm.setString(8, user.getStreet());
				stm.setString(9, user.getNeighborhood());
				stm.setString(10, user.getNumber());
				stm.setString(11, user.getPhoto());
				stm.setString(12, user.getTelephone());
				stm.setString(13, user.getCountry());
				stm.setString(14, user.getId());
			}

			if (user.getPassword() == null && user.getIdentityDocument() == null) {

				stm.setString(1, user.getName());
				stm.setString(2, user.getBornDate());
				stm.setString(3, user.getGender());
				stm.setString(4, user.getPostalCode());
				stm.setString(5, user.getState());
				stm.setString(6, user.getCity());
				stm.setString(7, user.getStreet());
				stm.setString(8, user.getNeighborhood());
				stm.setString(9, user.getNumber());
				stm.setString(10, user.getPhoto());
				stm.setString(11, user.getTelephone());
				stm.setString(12, user.getCountry());
				stm.setString(13, user.getId());
			}

			if (user.getPassword() != null && user.getIdentityDocument() != null) {
				stm.setString(1, user.getPassword());
				stm.setString(2, user.getName());
				stm.setString(3, user.getBornDate());
				stm.setString(4, user.getGender());
				stm.setString(5, user.getPostalCode());
				stm.setString(6, user.getState());
				stm.setString(7, user.getCity());
				stm.setString(8, user.getStreet());
				stm.setString(9, user.getNeighborhood());
				stm.setString(10, user.getNumber());
				stm.setString(11, user.getPhoto());
				stm.setString(12, user.getIdentityDocument());
				stm.setString(13, user.getTelephone());
				stm.setString(14, user.getCountry());
				stm.setString(15, user.getId());
			}

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, user.toString());

			e.printStackTrace();
			return false;
		}
	}

	public boolean updateUserStatus(String login, String typeOfUser) {

		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("UPDATE_USER_STATUS");
		auditTrailModel.setAdditionalInfo("login " + login + " typeOfUser " + typeOfUser);
		setAuditTrail(auditTrailModel);
		
		try {
			String sql = "";

			sql = "UPDATE USER_HEALTH " + "SET " + "type_of_user = ? WHERE login = ?;";

			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, typeOfUser);
			stm.setString(2, login);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "login " + login + " typeOfUser " + typeOfUser);

			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateUserPhoto(String userId, String photo) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("UPDATE_USER_PHOTO");
		auditTrailModel.setAdditionalInfo("userId " + userId + " photo " + photo);
		setAuditTrail(auditTrailModel);
		
		try {
			String sql = "";

			sql = "UPDATE USER_HEALTH SET photo = ? WHERE id_user = ?;";

			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, photo);
			stm.setString(2, userId);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "userId " + userId + " photo " + photo);

			e.printStackTrace();
			return false;
		}
	}


	public boolean delete(String userId) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("DELETE_USER");
		auditTrailModel.setAdditionalInfo("userId " + userId);
		setAuditTrail(auditTrailModel);
		

		String sql = "delete from USER_HEALTH where id_user = ?";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, userId);

			stm.execute();
			stm.close();
			return true;
		} catch (SQLException e) {
			setError(e, "userId " + userId);

			e.printStackTrace();
			return false;
		}
	}

	public UserModel getUser(String userId) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("GET_USER");
		auditTrailModel.setAdditionalInfo("userId " + userId);
		setAuditTrail(auditTrailModel);
		
		
		try {
			PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM USER_HEALTH WHERE id_user = ?");

			stmt.setString(1, userId);

			ResultSet rs = stmt.executeQuery();

			UserModel user = null;

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
			stmt.close();

			return user;
		} catch (SQLException e) {
			setError(e, "userId " + userId);

			throw new RuntimeException(e);
		}
	}
	
	public UserModel getUserBySecretCode(String secretCode) {
		
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("GET_USER_BY_SECRET_CODE");
		auditTrailModel.setAdditionalInfo("secretCode " + secretCode);
		setAuditTrail(auditTrailModel);
		
		
		try {
			PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM USER_HEALTH WHERE secret_code = ?");

			stmt.setString(1, secretCode);

			ResultSet rs = stmt.executeQuery();

			UserModel user = null;

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
			stmt.close();

			return user;
		} catch (SQLException e) {
			setError(e, "secretCode " + secretCode);

			throw new RuntimeException(e);
		}
	}


	public UserModel getUserByLogin(String login) {
		

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("GET_USER_BY_LOGIN");
		auditTrailModel.setAdditionalInfo("login " + login);
		setAuditTrail(auditTrailModel);
		
		
		try {
			PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM USER_HEALTH WHERE login = ?");

			stmt.setString(1, login);

			ResultSet rs = stmt.executeQuery();

			UserModel user = null;

			while (rs.next()) {

				user = new UserModel.UserModelBuilder(rs.getString("login"), "").id(rs.getString("id_user"))
						.name(rs.getString("name")).typeOfUser(rs.getString("type_of_user"))
						.bornDate(rs.getString("born_date")).gender(rs.getString("gender"))
						.postalCode(rs.getString("postal_code")).country(rs.getString("country"))
						.state(rs.getString("state")).city(rs.getString("city")).street(rs.getString("street"))
						.neighborhood(rs.getString("neighborhood")).number(rs.getString("number"))
						.photo(rs.getString("photo")).identityDocument((rs.getString("identity_document")))
						.telephone(rs.getString("telephone")).build();

			}
			rs.close();
			stmt.close();

			return user;
		} catch (SQLException e) {
			
			setError(e, "login " + login);

			throw new RuntimeException(e);
		}
	}

	public boolean checkIfUserExist(String login) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("CHECK_IF_USER_EXIST");
		auditTrailModel.setAdditionalInfo("login " + login);
		setAuditTrail(auditTrailModel);
		

		String sql = "select count(*) from USER_HEALTH where login = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			System.out.println(stm.toString());

			stm.setString(1, login);

			ResultSet rs = stm.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				return true;
			}

			return false;

		} catch (SQLException e) {
			setError(e, "login " + login);

			return false;
		}
	}

	public boolean checkIfDocumentsExist(String document, String country, String typeOfUser) {
		  
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("CHECK_IF_DOCUMENT_EXIST");
		auditTrailModel.setAdditionalInfo("document " + document + " country " + country + " typeOfUser " + typeOfUser);
		setAuditTrail(auditTrailModel);
	
		
		System.out.println(document + " ----- " + country + " ---- " + typeOfUser);
		String sql = "select count(*) from USER_HEALTH where identity_document = ? AND country = ? AND type_of_user = ?";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			System.out.println(stm.toString());

			System.out.println();

			stm.setString(1, document);
			stm.setString(2, country);
			stm.setString(3, typeOfUser);

			ResultSet rs = stm.executeQuery();
			rs.next();

			if (rs.getInt(1) > 0) {
				return true;
			}

			return false;

		} catch (SQLException e) {
			setError(e, "document " + document + " country " + country + " typeOfUser " + typeOfUser);

			return false;
		}
	}

	public boolean authenticate(String login, String password, StringBuilder token, StringBuilder typeOfUser,
			StringBuilder name, StringBuilder secretCode) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("AUTHENTICATE");
		auditTrailModel.setAdditionalInfo("login " + login + " password  " + password + " token " + token.toString() + " typeOfUser " + typeOfUser.toString() + " name " + name);
		setAuditTrail(auditTrailModel);


		String sql = "select id_user, type_of_user, name,secret_code  from USER_HEALTH where login = ? and password = ?";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, login);
			stm.setString(2, password);

			ResultSet rs = stm.executeQuery();
			rs.next();

			System.out.println(rs.getString("id_user"));

			
			if (rs.getString("id_user") != null) {

				token.append(rs.getString("id_user"));
				typeOfUser.append(rs.getString("type_of_user"));
				name.append(rs.getString("name"));
				secretCode.append(rs.getString("secret_code"));

				stm.close();
				return true;
			} else {
				stm.close();
				return false;

			}

			
		} catch (SQLException e) {
			setError(e, "login " + login + " password " + password + " token " + token.toString() + " typeOfUser " + typeOfUser.toString() + " name " + name.toString());

			return false;
		}
	}

	public List<HealthInstitutionModel> getHealthInstitutionBind(String idUser, String status) {
		
		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("GET_HEALTH_INSTITUTION_BIND");
		auditTrailModel.setAdditionalInfo("idUser " + idUser + " status " + status);
		setAuditTrail(auditTrailModel);
		
		try {
			List<HealthInstitutionModel> healthInstitutionList = new ArrayList<HealthInstitutionModel>();
			PreparedStatement stmt = this.connection.prepareStatement(
					"SELECT HEALTH_INSTITUTION.id_health_institution, HEALTH_INSTITUTION.name, HEALTH_INSTITUTION.photo, HEALTH_INSTITUTION.identity_code, HEALTH_INSTITUTION.country, HEALTH_INSTITUTION.state, HEALTH_INSTITUTION.city, HEALTH_INSTITUTION.number, HEALTH_INSTITUTION.street, HEALTH_INSTITUTION.neighborhood, HEALTH_INSTITUTION.telephone "
							+ " FROM USER_AND_HEALTH_INSTITUTION INNER JOIN HEALTH_INSTITUTION ON USER_AND_HEALTH_INSTITUTION.id_health_institution = HEALTH_INSTITUTION.id_health_institution "
							+ " INNER JOIN USER_HEALTH ON USER_AND_HEALTH_INSTITUTION.id_user = USER_HEALTH.id_user "
							+ " WHERE USER_AND_HEALTH_INSTITUTION.id_user = ? AND status = ?");

			stmt.setString(1, idUser);
			stmt.setString(2, status);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				HealthInstitutionModel healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(
						rs.getString("id_health_institution"), rs.getString("name")).photo(rs.getString("photo"))
								.identityCode(rs.getString("identity_code")).country(rs.getString("country"))
								.state(rs.getString("state")).city(rs.getString("city")).number(rs.getString("number")).neighborhood(rs.getString("neighborhood")).telephone(rs.getString("telephone")).street(rs.getString("street")).build();
				
				healthInstitutionList.add(healthInstitutionModel);
			}
			rs.close();
			stmt.close();
			return healthInstitutionList;
		} catch (SQLException e) {
			setError(e, "idUser " + idUser + " status " + status);

			throw new RuntimeException(e);
		}
	}

	public boolean sendMailPassword(String email, String pass, String country) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("SEND_MAIL_PASSWORD");
		auditTrailModel.setAdditionalInfo("email " + email + " pass " + pass + " country " + country);
		setAuditTrail(auditTrailModel);

		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.**ssl.enable", "true");
		props.setProperty("mail.smtp.**ssl.required", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("nfcareone@gmail.com", "nfc_care_tcc");
			}
		});
		try {
			// Creating a Message object to set the email content
			MimeMessage msg = new MimeMessage(session);
			// Storing the comma seperated values to email addresses
			String to = email;
			/*
			 * Parsing the String with defualt delimiter as a comma by marking
			 * the boolean as true and storing the email addresses in an array
			 * of InternetAddress objects
			 */
			InternetAddress[] address = InternetAddress.parse(to, true);
			// Setting the recepients from the address variable
			msg.setRecipients(Message.RecipientType.TO, address);
			String timeStamp = new SimpleDateFormat("yyyymmdd_hh-mm-ss").format(new Date());
			if (country.equals("BRA")) {
				msg.setSubject("Senha de acesso ao sistema");
			} else {
				msg.setSubject("System password");

			}
			msg.setSentDate(new Date());
			if (country.equals("BRA")) {
				msg.setText("Sua senha de acesso ao sistema é: " + pass);

			} else {
				msg.setText("Your system password is: " + pass);

			}

			msg.setHeader("XPriority", "1");
			Transport.send(msg);

			return true;
		} catch (MessagingException ex) {
			setError(ex, "email " + email + " pass " + pass + " country " + country);

			ex.printStackTrace();
			return false;
		}
	}

	public boolean sendSecretCode(String email, String secretCode, String country) {

		AuditTrailModel auditTrailModel = new AuditTrailModel();
		auditTrailModel.setCategory("USER");
		auditTrailModel.setEventName("SEND_MAIL_PASSWORD");
		auditTrailModel.setAdditionalInfo("email " + email + " secretCode " + secretCode + " country " + country);
		setAuditTrail(auditTrailModel);


		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.**ssl.enable", "true");
		props.setProperty("mail.smtp.**ssl.required", "true");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("nfcareone@gmail.com", "nfc_care_tcc");
			}
		});
		try {
			// Creating a Message object to set the email content
			MimeMessage msg = new MimeMessage(session);
			// Storing the comma seperated values to email addresses
			String to = email;
			/*
			 * Parsing the String with defualt delimiter as a comma by marking
			 * the boolean as true and storing the email addresses in an array
			 * of InternetAddress objects
			 */
			InternetAddress[] address = InternetAddress.parse(to, true);
			// Setting the recepients from the address variable
			msg.setRecipients(Message.RecipientType.TO, address);
			String timeStamp = new SimpleDateFormat("yyyymmdd_hh-mm-ss").format(new Date());
			if (country.equals("BRA")) {
				msg.setSubject("Código secreto de uso");
			} else {
				msg.setSubject("Secret code");
			}
			msg.setSentDate(new Date());
			if (country.equals("BRA")) {

				msg.setText("Olá!, seu código secreto de uso é  " + secretCode
						+ " e você pode usa-lo como alternativa quando não tiver um equipamento NFC em suas mãos!");

			}
			else{
				msg.setText("Hello!, your secret code is " + secretCode
						+ " and you can use it as an alternative when you do not have NFC equipment in your hands!");

			}
			msg.setHeader("XPriority", "1");
			Transport.send(msg);

			return true;
		} catch (MessagingException ex) {
			setError(ex, "email " + email + " secretCode " + secretCode + " country " + country);

			ex.printStackTrace();
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