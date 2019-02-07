package com.healthsystem.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
import com.healthsystem.dao.HealthInstitutionDAO;
import com.healthsystem.filter.JWTTokenNeeded;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.MessageListModel;
import com.healthsystem.model.MessageModel;
import com.healthsystem.model.NurseWorksOnHealthInstitutionModel;
import com.healthsystem.model.PhysicianWorksOnHealthInstitutionModel;
import com.healthsystem.model.UserModel;
import com.healthsystem.util.AzureBlob;
import com.healthsystem.util.JwtUtil;
import com.healthsystem.util.Validate;
import com.pusher.rest.Pusher;

@Path("/healthinstitution")
@Produces(MediaType.APPLICATION_JSON)
public class HealthInstitutionResource {

	private HealthInstitutionDAO healthInstitutionDAO = new HealthInstitutionDAO();

	@Context
	private UriInfo uriInfo;

	@JWTTokenNeeded
	@POST
	public MessageModel add(@Context HttpHeaders httpHeaders, @FormParam("_identity_code") String identityCode,
			@FormParam("_name") String name, @FormParam("_postal_code") String postalCode,
			@FormParam("_country") String country, @FormParam("_state") String state, @FormParam("_city") String city,
			@FormParam("_street") String street, @FormParam("_neighborhood") String neighborhood,
			@FormParam("_number") String number, @FormParam("_photo") String photo,
			@FormParam("_telephone") String telephone) {

		String uuid = UUID.randomUUID().toString();

		HealthInstitutionModel healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(uuid,
				name).identityCode(identityCode).postalCode(postalCode).country(country).state(state).city(city)
						.street(street).neighborhood(neighborhood).number(number).photo(photo).telephone(telephone)
						.build();

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<HealthInstitutionModel>> constraintViolations = validator
				.validate(healthInstitutionModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		boolean status = healthInstitutionDAO.add(healthInstitutionModel);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Health institution added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding Health institution to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@PUT
	public MessageModel update(@Context HttpHeaders httpHeaders,
			@FormParam("_id_health_institution") String idHealthInstitution,
			@FormParam("_identity_code") String identityCode, @FormParam("_name") String name,
			@FormParam("_postal_code") String postalCode, @FormParam("_country") String country,
			@FormParam("_state") String state, @FormParam("_city") String city, @FormParam("_street") String street,
			@FormParam("_neighborhood") String neighborhood, @FormParam("_number") String number,
			@FormParam("_photo") String photo, @FormParam("_telephone") String telephone) {

		HealthInstitutionModel healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(
				idHealthInstitution, name).identityCode(identityCode).postalCode(postalCode).country(country)
						.state(state).city(city).street(street).neighborhood(neighborhood).number(number).photo(photo)
						.telephone(telephone).build();

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<HealthInstitutionModel>> constraintViolations = validator
				.validate(healthInstitutionModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		boolean status = healthInstitutionDAO.update(healthInstitutionModel);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Health institution updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating Health institution to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@GET
	public MessageListModel<HealthInstitutionModel> getHealthInstitutions(@Context HttpHeaders httpHeaders,
			@QueryParam("latitute") double latitute, @QueryParam("longitute") double longitute,
			@QueryParam("km") int km) {

		MessageListModel<HealthInstitutionModel> messageModel = new MessageListModel<HealthInstitutionModel>();

		if (km <= 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("Inform a km that is greater than 0!");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		List<HealthInstitutionModel> list = healthInstitutionDAO.getHealthInstitutions(latitute, longitute, km);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List with health institutions successfully loaded!");
		messageModel.setList(list);

		return messageModel;

	}

	@GET
	@Path("/list")
	public MessageListModel<HealthInstitutionModel> getHealthInstitutions(@Context HttpHeaders httpHeaders,
			@QueryParam("start") int start, @QueryParam("end") int end, @QueryParam("country") String country,
			@QueryParam("search") String search) {

		MessageListModel<HealthInstitutionModel> messageModel = new MessageListModel<HealthInstitutionModel>();

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		List<HealthInstitutionModel> list = healthInstitutionDAO.getHealthInstitutions(start, end, country, search);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List with health institutions successfully loaded!");
		messageModel.setList(list);

		return messageModel;

	}

	@JWTTokenNeeded
	@DELETE
	public MessageModel delete(@Context HttpHeaders httpHeaders,
			@FormParam("_id_health_institution") String idHealthInstitution) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idHealthInstitution);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idHealthInstitution should not be null");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		boolean status = healthInstitutionDAO.delete(idHealthInstitution);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Health institution deleted with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Health institution when deleting user in database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/{idHealthInstitution}")
	@GET
	public MessageListModel<HealthInstitutionModel> getHealthInstitution(@Context HttpHeaders httpHeaders,
			@PathParam("idHealthInstitution") String idHealthInstitution) {

		MessageListModel<HealthInstitutionModel> messageModel = new MessageListModel<>();

		boolean validationOk = Validate.validateNotNull(idHealthInstitution);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idHealthInstitution should not be null");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		HealthInstitutionModel healthInstitutionModel = healthInstitutionDAO.getHealthInstitution(idHealthInstitution);

		if (healthInstitutionModel == null) {
			messageModel.setCode(1);
			messageModel.setSuccess(false);
			messageModel.setDescription("Health institution not found!");

			return messageModel;
		}

		List<HealthInstitutionModel> list = new ArrayList<>();
		list.add(healthInstitutionModel);
		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Success in retrieving health institution data!");
		messageModel.setList(list);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/bind")
	@POST
	public MessageModel addBind(@Context HttpHeaders httpHeaders, @FormParam("_id_user") String idUser,
			@FormParam("_id_health_institution") String idHealthInstitution, @FormParam("_status") String status) {

		MessageModel messageModel = new MessageModel();
		boolean validationOk = Validate.validateNotNull(idUser, idHealthInstitution, status);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idHealthInstitution and idUser and status should not be null");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		if (healthInstitutionDAO.containsBind(idUser, idHealthInstitution)) {
			messageModel.setCode(-2);
			messageModel.setSuccess(false);
			messageModel.setDescription("already in table");
			return messageModel;
		}

		boolean ok = healthInstitutionDAO.addBind(idHealthInstitution, idUser, status);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Bind added with success!");
			Pusher pusher = new Pusher("556519", "6ba2a6129f4cf6d110a6", "5fc3f9d3786568a73222");
			pusher.setCluster("us2");
			pusher.setEncrypted(true);

			pusher.trigger(idUser, "notification", true);

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when binding!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/updatebind")
	@PUT
	public MessageModel updateBind(@Context HttpHeaders httpHeaders, @FormParam("_id_user") String idUser,
			@FormParam("_id_health_institution") String idHealthInstitution, @FormParam("_status") String status) {

		MessageModel messageModel = new MessageModel();
		boolean validationOk = Validate.validateNotNull(idUser, idHealthInstitution, status);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idHealthInstitution and idUser and status should not be null");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		boolean ok = healthInstitutionDAO.updateBind(idHealthInstitution, idUser, status);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Bind updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating bind!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/deletebind")
	@DELETE
	public MessageModel deleteBind(@Context HttpHeaders httpHeaders, @FormParam("_id_user") String idUser,
			@FormParam("_id_health_institution") String idHealthInstitution) {

		MessageModel messageModel = new MessageModel();
		boolean validationOk = Validate.validateNotNull(idUser, idHealthInstitution);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idHealthInstitution and idUser should not be null");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		boolean status = healthInstitutionDAO.deleteBind(idHealthInstitution, idUser);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Bind deleted with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when unbinding!");

		return messageModel;
	}

	@JWTTokenNeeded
	@GET
	@Path("/userlist/{idHealthInstitution}")
	public MessageListModel<UserModel> getHealthInstitutions(@Context HttpHeaders httpHeaders,
			@PathParam("idHealthInstitution") String idHealthInstitution, @QueryParam("status") String status) {

		MessageListModel<UserModel> messageModel = new MessageListModel<UserModel>();

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		List<UserModel> list = healthInstitutionDAO.getBindedUsers(idHealthInstitution, status);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List with bind successfully loaded!");
		messageModel.setList(list);

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/image")
	@Produces("image/jpeg")
	@GET
	public Response getImage(@Context HttpHeaders httpHeaders,
			@QueryParam("idHealthInstitution") String idHealthInstitution) {

		boolean validateOk = Validate.validateNotNull(idHealthInstitution);
		if (!validateOk) {
			byte[] downloadInputStream = AzureBlob.downloadInputStream("HEALTH.jpg", "healthinstitution");

			return Response.ok(downloadInputStream).build();
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		HealthInstitutionModel healthInstitution = healthInstitutionDAO.getHealthInstitution(idHealthInstitution);
		if (healthInstitution == null) {
			byte[] downloadInputStream = AzureBlob.downloadInputStream("HEALTH.jpg", "healthinstitution");

			return Response.ok(downloadInputStream).build();
		} else {
			byte[] downloadInputStream = AzureBlob.downloadInputStream(healthInstitution.getPhoto(),
					"healthinstitution");

			return Response.ok(downloadInputStream).build();
		}

	}

	@JWTTokenNeeded
	@GET
	@Path("/physician")
	public MessageListModel<PhysicianWorksOnHealthInstitutionModel> getPhysicians(@Context HttpHeaders httpHeaders,
			@QueryParam("idHealthInstitution") String idHealthInstitution, @QueryParam("language") String language) {

		MessageListModel<PhysicianWorksOnHealthInstitutionModel> messageModel = new MessageListModel<>();
		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		List<PhysicianWorksOnHealthInstitutionModel> list = healthInstitutionDAO.getPhysicians(idHealthInstitution,
				language);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List with bind successfully loaded!");
		messageModel.setList(list);

		return messageModel;

	}

	@JWTTokenNeeded
	@GET
	@Path("/nurse")
	public MessageListModel<NurseWorksOnHealthInstitutionModel> getNurses(@Context HttpHeaders httpHeaders,
			@QueryParam("idHealthInstitution") String idHealthInstitution) {

		MessageListModel<NurseWorksOnHealthInstitutionModel> messageModel = new MessageListModel<>();
		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		List<NurseWorksOnHealthInstitutionModel> list = healthInstitutionDAO.getNurses(idHealthInstitution);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List with bind successfully loaded!");
		messageModel.setList(list);

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/waitlist/{idHealthInstitution}")
	@POST
	public MessageModel addToWaitList(@Context HttpHeaders httpHeaders,
			@PathParam("idHealthInstitution") String idHealthInstitution,
			@FormParam("_id_diagnosis") String idDiagnosis) {

		MessageModel messageModel = new MessageModel();
		boolean validationOk = Validate.validateNotNull(idDiagnosis, idHealthInstitution);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idHealthInstitution and idDiagnosis should not be null");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		boolean ok = healthInstitutionDAO.addToWaitList(idHealthInstitution, idDiagnosis);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Patient added to list with success!");
		

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding patient to list!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/updatewaitlist/{idHealthInstitution}")
	@POST
	public MessageModel updateWaitList(@Context HttpHeaders httpHeaders,
			@PathParam("idHealthInstitution") String idHealthInstitution) {

		MessageModel messageModel = new MessageModel();
		boolean validationOk = Validate.validateNotNull(idHealthInstitution);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idHealthInstitution should not be null");
			return messageModel;
		}

		healthInstitutionDAO.setHttpHeaders(httpHeaders);

		Pusher pusher = new Pusher("556519", "6ba2a6129f4cf6d110a6", "5fc3f9d3786568a73222");
		pusher.setCluster("us2");
		pusher.setEncrypted(true);

		pusher.trigger(idHealthInstitution + ";nurse", "new-patient-in-list", true);
		

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Patient added to list with success!");
		
		
		return messageModel;
	}

	/*
	 * @JWTTokenNeeded
	 * 
	 * @Path("/waitlist/{idHealthInstitution}")
	 * 
	 * @GET public MessageListModel getWaitList(
	 * 
	 * @Context HttpHeaders httpHeaders,
	 * 
	 * @PathParam("idHealthInstitution") String idHealthInstitution) {
	 * 
	 * MessageListModel messageModel = new MessageListModel(); boolean
	 * validationOk = Validate.validateNotNull(idHealthInstitution);
	 * 
	 * if (!validationOk) { messageModel.setCode(-1);
	 * messageModel.setSuccess(false); messageModel.setDescription(
	 * "idHealthInstitution should not be null"); return messageModel; }
	 * 
	 * healthInstitutionDAO.setHttpHeaders(httpHeaders);
	 * 
	 * 
	 * boolean ok = healthInstitutionDAO.getWaitList(idHealthInstitution);
	 * 
	 * if (ok) { messageModel.setCode(0); messageModel.setSuccess(true);
	 * messageModel.setDescription("Patient list retrieved with success!");
	 * 
	 * return messageModel; }
	 * 
	 * messageModel.setCode(1); messageModel.setSuccess(false);
	 * messageModel.setDescription("Error when retrieving patient list!");
	 * 
	 * return messageModel; }
	 */

}
