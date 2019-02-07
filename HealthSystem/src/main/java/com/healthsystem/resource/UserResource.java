package com.healthsystem.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.healthsystem.dao.PatientDAO;
import com.healthsystem.dao.PhysicianDAO;
import com.healthsystem.dao.UserDAO;
import com.healthsystem.filter.JWTTokenNeeded;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.MessageAuthModel;
import com.healthsystem.model.MessageListModel;
import com.healthsystem.model.MessageModel;
import com.healthsystem.model.MessageNewUserModel;
import com.healthsystem.model.PatientModel;
import com.healthsystem.model.UserModel;
import com.healthsystem.util.AzureBlob;
import com.healthsystem.util.Criptography;
import com.healthsystem.util.ImageUtil;
import com.healthsystem.util.Validate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	private UserDAO userDAO = new UserDAO();

	@Context
	private UriInfo uriInfo;

	/**
	 * 
	 * _identity_code for type_of_user 1 is an UUID The others are based on
	 * identification document of a people User type 1 is an system user
	 */
	@POST
	public MessageNewUserModel add(
			@Context HttpHeaders httpHeaders,
			@FormParam("_login") String login, @FormParam("_password") String password,
			@FormParam("_name") String name, @FormParam("_identity_document") String identityDocument,
			@FormParam("_type_of_user") String typeOfUser, @FormParam("_born_date") String bornDate,
			@FormParam("_gender") String gender, @FormParam("_postal_code") String postalCode,
			@FormParam("_country") String country, @FormParam("_state") String state, @FormParam("_city") String city,
			@FormParam("_street") String street, @FormParam("_neighborhood") String neighborhood,
			@FormParam("_number") String number, @FormParam("_photo") String photo,
			@FormParam("_telephone") String telephone) {
		userDAO.setHttpHeaders(httpHeaders);

		String uuid = UUID.randomUUID().toString();

		UserModel userModel = new UserModel.UserModelBuilder(login, password).id(uuid).name(name).typeOfUser(typeOfUser)
				.bornDate(bornDate).gender(gender).postalCode(postalCode).country(country).state(state).city(city)
				.street(street).neighborhood(neighborhood).number(number).photo(photo)
				.identityDocument(identityDocument).telephone(telephone).build();

		MessageNewUserModel messageModel = new MessageNewUserModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserModel>> constraintViolations = validator.validate(userModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		if (userDAO.checkIfDocumentsExist(userModel.getIdentityDocument(), userModel.getCountry(),
				userModel.getTypeOfUser())) {
			messageModel.setCode(-3);
			messageModel.setSuccess(false);
			messageModel.setDescription("User already exist");
			return messageModel;
		}

		if (userDAO.checkIfUserExist(userModel.getLogin())) {
			messageModel.setCode(-2);
			messageModel.setSuccess(false);
			messageModel.setUserModel(userDAO.getUserByLogin(userModel.getLogin()));
			messageModel.setDescription("User already exist");
			return messageModel;
		}

		boolean status = userDAO.add(userModel);

		if (status) {

			if (!userModel.getTypeOfUser().equals("5")) {
				if (!new PatientDAO().checkIfUserExist(userModel.getId())) {
					PatientModel patientModel = new PatientModel();
					patientModel.setIdPatient(UUID.randomUUID().toString());
					patientModel.setIdUser(userModel.getId());
					patientModel.setStatus("1");

					boolean ok = new PatientDAO().add(patientModel);

					if (ok) {
						messageModel.setCode(0);
						messageModel.setSuccess(true);
						messageModel.setDescription("User added with success!");
						messageModel.setId(uuid);
						return messageModel;
					} else {
						messageModel.setCode(1);
						messageModel.setSuccess(false);
						messageModel.setDescription("Error when adding user to database!");

						return messageModel;
					}

				} else {
					messageModel.setCode(0);
					messageModel.setSuccess(true);
					messageModel.setDescription("User added with success!");
					messageModel.setId(uuid);
					return messageModel;
				}
			} else {
				messageModel.setCode(0);
				messageModel.setSuccess(true);
				messageModel.setDescription("User added with success!");
				messageModel.setId(uuid);
				return messageModel;
			}

		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding user to database!");

		return messageModel;
	}

	@POST
	@Path("/checkUserStatus")
	public MessageNewUserModel checkUserStatus(
			@Context HttpHeaders httpHeaders,
			@FormParam("_login") String login,
			@FormParam("_identity_document") String identityDocument, @FormParam("_type_of_user") String typeOfUser,
			@FormParam("_country") String country) {
	
		userDAO.setHttpHeaders(httpHeaders);

		MessageNewUserModel messageNewUserModel = new MessageNewUserModel();

		System.out.println(identityDocument + " --- " + login + " --- " + typeOfUser + " --- " + country);
		if (userDAO.checkIfDocumentsExist(identityDocument, country, typeOfUser)) {
			messageNewUserModel.setCode(-3);
			messageNewUserModel.setSuccess(false);
			messageNewUserModel.setDescription("User already exist");
			return messageNewUserModel;
		}

		if (userDAO.checkIfUserExist(login)) {

			messageNewUserModel.setCode(-2);
			messageNewUserModel.setUserModel(userDAO.getUserByLogin(login));
			messageNewUserModel.setSuccess(false);
			messageNewUserModel.setDescription("User already exist");
			return messageNewUserModel;
		}

		messageNewUserModel.setCode(0);
		messageNewUserModel.setSuccess(true);
		messageNewUserModel.setDescription("User does not exist");
		return messageNewUserModel;

	}

	@JWTTokenNeeded
	@PUT
	public MessageModel update(
			@Context HttpHeaders httpHeaders,
			@FormParam("_user_id") String userId, @FormParam("_login") String login,
			@FormParam("_password") String password, @FormParam("_name") String name,
			@FormParam("_identity_document") String identityDocument, @FormParam("_type_of_user") String typeOfUser,
			@FormParam("_born_date") String bornDate, @FormParam("_gender") String gender,
			@FormParam("_postal_code") String postalCode, @FormParam("_country") String country,
			@FormParam("_state") String state, @FormParam("_city") String city, @FormParam("_street") String street,
			@FormParam("_neighborhood") String neighborhood, @FormParam("_number") String number,
			@FormParam("_photo") String photo, @FormParam("_telephone") String telephone) {

		userDAO.setHttpHeaders(httpHeaders);

		UserModel userModel = new UserModel.UserModelBuilder((login == null || login.equals("")) ? "not-needed" : login,
				(password == null || password.equals("")) ? "not-needed" : password).id(userId).name(name)
						.typeOfUser(typeOfUser).bornDate(bornDate).gender(gender).postalCode(postalCode)
						.country(country).state(state).city(city).street(street).neighborhood(neighborhood)
						.number(number).photo(photo)
						.identityDocument((identityDocument == null || identityDocument.equals("")) ? "not-needed"
								: identityDocument)
						.telephone(telephone).build();

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserModel>> constraintViolations = validator.validate(userModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		if (password == null) {
			userModel.setPassword(password);
		}

		if (login == null) {
			userModel.setLogin(login);
		}

		if (identityDocument == null) {
			userModel.setIdentityDocument(identityDocument);
		}

		boolean ok = true;
		if (login != null) {

			if (userDAO.checkIfUserExist(userModel.getLogin())) {

				messageModel.setCode(-2);
				messageModel.setSuccess(false);
				messageModel.setDescription("User already exist");
				return messageModel;
			} else {
				ok = userDAO.updateLogin(userModel.getLogin(), userModel.getId());

			}
		}

		if (identityDocument != null && identityDocument.length() != 36) {

			if (userDAO.checkIfDocumentsExist(userModel.getIdentityDocument(), userModel.getCountry(),
					userModel.getTypeOfUser())) {
				messageModel.setCode(-2);
				messageModel.setSuccess(false);
				messageModel.setDescription("User already exist");
				return messageModel;
			}
		}

		if (ok) {
			ok = userDAO.update(userModel);
		}

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("User updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating user in database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/updateUserType")
	@PUT
	public MessageModel update(
			@Context HttpHeaders httpHeaders,
			@FormParam("_login") String login, @FormParam("_type_of_user") String userType) {
		MessageModel messageModel = new MessageModel();

		userDAO.setHttpHeaders(httpHeaders);

		boolean ok = userDAO.updateUserStatus(login, userType);
		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("User updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating user in database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@DELETE
	public MessageModel delete(
			@Context HttpHeaders httpHeaders,
			@FormParam("_user_id") String userId) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(userId);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("userId should not be null");
			return messageModel;
		}
		
		userDAO.setHttpHeaders(httpHeaders);

		boolean status = userDAO.delete(userId);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("User deleted with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when deleting user in database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/{idUser}")
	@GET
	public MessageListModel<UserModel> getUser(
			@Context HttpHeaders httpHeaders,
			@PathParam("idUser") String userId) {

		MessageListModel<UserModel> messageModel = new MessageListModel<>();

		boolean validationOk = Validate.validateNotNull(userId);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("userId should not be null");
			return messageModel;
		}

		userDAO.setHttpHeaders(httpHeaders);

		UserModel userModel = userDAO.getUser(userId);

		if (userModel == null) {
			messageModel.setCode(1);
			messageModel.setSuccess(false);
			messageModel.setDescription("User not found!");

			return messageModel;
		}

		List<UserModel> list = new ArrayList<>();
		list.add(userModel);
		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Success in retrieving user data!");
		messageModel.setList(list);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/secretCode")
	@GET
	public MessageModel getUserBySecretCode(
			@Context HttpHeaders httpHeaders,
			@QueryParam("secretCode") String secretCode) {

		MessageAuthModel messageModel = new MessageAuthModel();

		boolean validationOk = Validate.validateNotNull(secretCode);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("secretCode should not be null");
			return messageModel;
		}

		userDAO.setHttpHeaders(httpHeaders);

		UserModel userModel = userDAO.getUserBySecretCode(secretCode);

		if (userModel == null) {
			messageModel.setCode(1);
			messageModel.setSuccess(false);
			messageModel.setDescription("User not found!");

			return messageModel;
		}

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Success in retrieving user data!");
		messageModel.setUserId(userModel.getId());

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/search/{login}")
	@GET
	public MessageListModel<UserModel> getUserByDocument(
			@Context HttpHeaders httpHeaders,
			@PathParam("login") String login) {

		MessageListModel<UserModel> messageModel = new MessageListModel<>();

		boolean validationOk = Validate.validateNotNull(login);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("login should not be null");
			return messageModel;
		}

		userDAO.setHttpHeaders(httpHeaders);

		UserModel userModel = userDAO.getUserByLogin(login);

		if (userModel == null) {
			messageModel.setCode(-2);
			messageModel.setSuccess(false);
			messageModel.setDescription("User not found!");

			return messageModel;
		}

		List<UserModel> list = new ArrayList<>();
		list.add(userModel);
		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Success in retrieving user data!");
		messageModel.setList(list);

		return messageModel;
	}

	@Path("/auth")
	@POST
	public MessageModel authenticate(
			@Context HttpHeaders httpHeaders,
			@FormParam("_login") String login, @FormParam("_password") String password) {

		MessageAuthModel messageAuthModel = new MessageAuthModel();

		boolean validateOk = Validate.validateNotNull(login, password);
		if (!validateOk) {
			messageAuthModel.setCode(-1);
			messageAuthModel.setSuccess(false);
			messageAuthModel.setDescription("Inform login and password and app type when adding user!");

		}

		StringBuilder userId = new StringBuilder();
		StringBuilder typeOfUser = new StringBuilder();
		StringBuilder name = new StringBuilder();
		StringBuilder secretCode = new StringBuilder();

		userDAO.setHttpHeaders(httpHeaders);

		boolean status = userDAO.authenticate(login, password, userId, typeOfUser, name, secretCode);

		if (status) {

			messageAuthModel.setCode(0);
			messageAuthModel.setSuccess(true);
			messageAuthModel.setUserId(userId.toString());
			messageAuthModel.setToken(issueToken(userId.toString()));
			messageAuthModel.setUserName(name.toString());
			messageAuthModel.setUserType(typeOfUser.toString());
			messageAuthModel.setSecretCode(secretCode.toString());
			    
			messageAuthModel.setDescription("Success login!");

			return messageAuthModel;
		} else {
			messageAuthModel.setCode(1);
			messageAuthModel.setSuccess(false);
			messageAuthModel.setDescription("User not found!");

			return messageAuthModel;
		}

	}

	@JWTTokenNeeded
	@GET
	@Path("/healthinstitutionbind/{idUser}")
	public MessageListModel<HealthInstitutionModel> getHealthInstitutions(
			@Context HttpHeaders httpHeaders,
			@PathParam("idUser") String idUser,
			@QueryParam("status") String status) {

		MessageListModel<HealthInstitutionModel> messageModel = new MessageListModel<HealthInstitutionModel>();

		userDAO.setHttpHeaders(httpHeaders);

		List<HealthInstitutionModel> list = userDAO.getHealthInstitutionBind(idUser, status);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List with bind successfully loaded!");
		messageModel.setList(list);

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/sendPasswordMail")
	@POST
	public MessageModel sendPasswordMail(
			@Context HttpHeaders httpHeaders,
			@FormParam("_login") String login, @FormParam("_password") String password,
			@FormParam("_country") String country) {

		MessageAuthModel messageAuthModel = new MessageAuthModel();

		boolean validateOk = Validate.validateNotNull(login, password);
		if (!validateOk) {
			messageAuthModel.setCode(-1);
			messageAuthModel.setSuccess(false);
			messageAuthModel.setDescription("Inform login and password and app type when sending e-mail to user!");

		}

		userDAO.setHttpHeaders(httpHeaders);


		boolean status = userDAO.sendMailPassword(login, password, country);

		if (status) {

			messageAuthModel.setCode(0);
			messageAuthModel.setSuccess(true);
			messageAuthModel.setDescription("Email sent sucessfully!");

			return messageAuthModel;
		} else {
			messageAuthModel.setCode(1);
			messageAuthModel.setSuccess(false);
			messageAuthModel.setDescription("Error sending mail!");

			return messageAuthModel;
		}

	}

	@JWTTokenNeeded
	@Path("/uploadImage")
	@PUT
	public MessageModel updateImage(
			@Context HttpHeaders httpHeaders,
			byte[] image, @QueryParam("userId") String userId) {
		MessageModel messageAuthModel = new MessageModel();
		
		userDAO.setHttpHeaders(httpHeaders);

		System.out.println(image.length + " ------");  
		
		
		
		if (image.length == 0) {

			messageAuthModel.setCode(-1);
			messageAuthModel.setDescription("Image cannot be null!");
			messageAuthModel.setSuccess(false);

			return messageAuthModel;
		}

		if(image.length >= 200000){
			messageAuthModel.setCode(-2);
			messageAuthModel.setDescription("Image cannot have size greater than 200000 bytes");
			messageAuthModel.setSuccess(false);
 
			return messageAuthModel;
		}
		
		if (userId == null || userId.isEmpty()) {

			messageAuthModel.setCode(-3);
			messageAuthModel.setDescription("userId cannot be empty!");
			messageAuthModel.setSuccess(false);

			return messageAuthModel;
		}

		UserModel user = userDAO.getUser(userId);
		if (user == null) {
			messageAuthModel.setCode(-4);
			messageAuthModel.setDescription("User not found!");
			messageAuthModel.setSuccess(false);

			return messageAuthModel;
		} else {
			if (user.getPhoto().equals("USER_DEFAULT_PHOTO.jpg")) {
				
				String photo = UUID.randomUUID().toString() + ".jpg";
				
				byte[] temp = image;
				InputStream in = new ByteArrayInputStream(temp);
				try {
					BufferedImage buffered = ImageIO.read(in);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ImageIO.write(buffered, "jpg", os);
					InputStream is = new ByteArrayInputStream(os.toByteArray());
					
					if(ImageUtil.isJPEG(is)){
						InputStream inputStream = new ByteArrayInputStream(image);
						BufferedImage bufferedImage = ImageIO.read(inputStream);
						AzureBlob.upload(bufferedImage, photo, "userhealth");
						if(userDAO.updateUserPhoto(userId, photo)){
							messageAuthModel.setCode(0);
							messageAuthModel.setDescription("Image uploaded sucessfully!");
							messageAuthModel.setSuccess(true);

							return messageAuthModel;
						}
						else{
							messageAuthModel.setCode(0);
							messageAuthModel.setDescription("An error occourred when updating user photo!");
							messageAuthModel.setSuccess(false);

							return messageAuthModel;
						}
						
						
					}
					else{
						throw new IOException("Image is not jpeg");
					}

				} catch (IOException e) {
					e.printStackTrace();

					messageAuthModel.setCode(-5);
					messageAuthModel.setDescription("Formatting error: " + e.getMessage());
					messageAuthModel.setSuccess(false);

					return messageAuthModel;
				}
				

			} else {
				byte[] temp = image;
				InputStream in = new ByteArrayInputStream(temp);
				try {
					BufferedImage buffered = ImageIO.read(in);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ImageIO.write(buffered, "jpg", os);
					InputStream is = new ByteArrayInputStream(os.toByteArray());
					
					if(ImageUtil.isJPEG(is)){
						InputStream inputStream = new ByteArrayInputStream(image);
						BufferedImage bufferedImage = ImageIO.read(inputStream);
						AzureBlob.delete(user.getPhoto(), "userhealth");
						AzureBlob.upload(bufferedImage, user.getPhoto(), "userhealth");
						messageAuthModel.setCode(0);
						messageAuthModel.setDescription("Image uploaded sucessfully!");
						messageAuthModel.setSuccess(true);

						return messageAuthModel;
					}
					else{
						throw new IOException("Image is not jpeg");
					}

				} catch (IOException e) {
					e.printStackTrace();

					messageAuthModel.setCode(-5);
					messageAuthModel.setDescription("Formatting error: " + e.getMessage());
					messageAuthModel.setSuccess(false);

					return messageAuthModel;
				}

			}

		}
	}

	
	@Path("/image")
	@Produces("image/jpeg")
	@GET
	public Response getImage(
			@Context HttpHeaders httpHeaders,
			@QueryParam("userId") String userId) {

		boolean validateOk = Validate.validateNotNull(userId);
		if (!validateOk) {
			byte[] downloadInputStream = AzureBlob.downloadInputStream("USER_DEFAULT_PHOTO.jpg", "userhealth");

			return Response.ok(downloadInputStream).build();
		}

		UserModel user = userDAO.getUser(userId);
		if (user == null) {
			byte[] downloadInputStream = AzureBlob.downloadInputStream("USER_DEFAULT_PHOTO.jpg", "userhealth");

			return Response.ok(downloadInputStream).build();
		} else {
			byte[] downloadInputStream = AzureBlob.downloadInputStream(user.getPhoto(), "userhealth");

			return Response.ok(downloadInputStream).build();
		}

	}
	
	

	private String issueToken(String userId) {

		Key key = Criptography.generateKey();

		return Jwts.builder().setSubject(userId).setIssuer(uriInfo.getAbsolutePath().toString()).setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS512, key).compact();
	}

}
