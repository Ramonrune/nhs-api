package com.healthsystem.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import com.healthsystem.dao.NurseDAO;
import com.healthsystem.filter.JWTTokenNeeded;
import com.healthsystem.model.MessageListModel;
import com.healthsystem.model.MessageModel;
import com.healthsystem.model.NurseModel;
import com.healthsystem.model.NurseProcedureModel;
import com.healthsystem.model.NurseProcessModel;
import com.healthsystem.model.SpecializationModel;
import com.healthsystem.util.Validate;

@Path("/nurse")
@Produces(MediaType.APPLICATION_JSON)
public class NurseResource {
	private NurseDAO nurseDAO = new NurseDAO();

	@Context
	private UriInfo uriInfo;

	@JWTTokenNeeded
	@POST
	public MessageModel add(@Context HttpHeaders httpHeaders, @FormParam("_id_user") String idUser,
			@FormParam("_id_nurse") String idNurse, @FormParam("_nurse_code") String nurseCode,
			@FormParam("_nurse_type") String nurseType) {

		NurseModel nurseModel = new NurseModel();
		nurseModel.setIdNurse(idNurse);
		nurseModel.setIdUser(idUser);
		nurseModel.setNurseCode(nurseCode);
		nurseModel.setNurseType(nurseType);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<NurseModel>> constraintViolations = validator.validate(nurseModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		nurseDAO.setHttpHeaders(httpHeaders);

		boolean status = nurseDAO.add(nurseModel);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Nurse added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding nurse to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@PUT
	public MessageModel update(@Context HttpHeaders httpHeaders, @FormParam("_id_user") String idUser,
			@FormParam("_id_nurse") String idNurse, @FormParam("_nurse_code") String nurseCode,
			@FormParam("_nurse_type") String nurseType) {
		NurseModel nurseModel = new NurseModel();
		nurseModel.setIdNurse(idNurse);
		nurseModel.setIdUser(idUser);
		nurseModel.setNurseCode(nurseCode);
		nurseModel.setNurseType(nurseType);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<NurseModel>> constraintViolations = validator.validate(nurseModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}
		nurseDAO.setHttpHeaders(httpHeaders);

		boolean status = nurseDAO.update(nurseModel);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Nurse updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating nurse to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@GET
	public MessageListModel<NurseModel> getNurse(@Context HttpHeaders httpHeaders,
			@QueryParam("id_user") String idUser) {

		MessageListModel<NurseModel> messageModel = new MessageListModel<>();

		nurseDAO.setHttpHeaders(httpHeaders);

		NurseModel nurseModel = nurseDAO.getNurse(idUser);

		if (nurseModel != null) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Nurse retrieved with success!");
			List<NurseModel> list = new ArrayList<>();
			list.add(nurseModel);
			messageModel.setList(list);
			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error retrieving nurse from database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/listSpecializations")
	@GET
	public MessageListModel<SpecializationModel> listNurseSpecialization(@Context HttpHeaders httpHeaders,
			@QueryParam("id_nurse") String idNurse) {

		MessageListModel<SpecializationModel> messageModel = new MessageListModel<>();

		nurseDAO.setHttpHeaders(httpHeaders);

		List<SpecializationModel> listPhysicianSpecialization = nurseDAO.listNurseSpecialization(idNurse);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List of nurse specializations retrieved with success!");
		messageModel.setList(listPhysicianSpecialization);
		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/specialization")
	@GET
	public MessageListModel<SpecializationModel> listSpecialization(@Context HttpHeaders httpHeaders,
			@QueryParam("country") String country, @QueryParam("language") String language) {

		MessageListModel<SpecializationModel> messageModel = new MessageListModel<>();

		nurseDAO.setHttpHeaders(httpHeaders);

		List<SpecializationModel> listSpecialization = nurseDAO.listSpecialization(country, language);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List of specializations retrieved with success!");
		messageModel.setList(listSpecialization);
		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/bindSpecialization")
	@POST
	public MessageModel bindSpecialization(@Context HttpHeaders httpHeaders, @FormParam("_id_nurse") String idNurse,
			@FormParam("_id_specialization") String idSpecialization) {

		MessageModel messageModel = new MessageModel();

		nurseDAO.setHttpHeaders(httpHeaders);

		boolean ok = nurseDAO.bindSpecialization(idNurse, idSpecialization);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Specialization binded with success!");
			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error binding specialization!");
		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/unbindSpecialization")
	@DELETE
	public MessageModel unbindSpecialization(@Context HttpHeaders httpHeaders, @FormParam("_id_nurse") String idNurse,
			@FormParam("_id_specialization") String idSpecialization) {

		MessageModel messageModel = new MessageModel();

		nurseDAO.setHttpHeaders(httpHeaders);

		boolean ok = nurseDAO.unbindSpecialization(idNurse, idSpecialization);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Specialization unbinded with success!");
			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error unbinding specialization!");
		return messageModel;
	}
	
	
	@JWTTokenNeeded
	@Path("/addAttendance")
	@POST
	public MessageModel addAttendance(
			@Context HttpHeaders httpHeaders,
			@FormParam("_id_diagnosis_procedure") String idDiagnosisProcedure,
			@FormParam("_date_procedure") String dateProcedure,
			@FormParam("_anotation") String anotation,
			@FormParam("_id_diagnosis") String idDiagnosis,
			@FormParam("_id_nurse") String idNurse,
			@FormParam("_status") String status) {

		MessageModel messageModel = new MessageModel();
		
		
		NurseProcedureModel nurseProcedureModel = new NurseProcedureModel();
		nurseProcedureModel.setAnotation(anotation);
		nurseProcedureModel.setDateProcedure(dateProcedure);
		nurseProcedureModel.setIdDiagnosis(idDiagnosis);
		nurseProcedureModel.setIdDiagnosisProcedure(idDiagnosisProcedure);
		nurseProcedureModel.setIdNurse(idNurse);
		nurseProcedureModel.setStatus(status);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<NurseProcedureModel>> constraintViolations = validator.validate(nurseProcedureModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		nurseDAO.setHttpHeaders(httpHeaders);

		boolean ok = nurseDAO.addProcedure(nurseProcedureModel);

		if(ok){
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Nurse process added with success!");
			return messageModel;
		}
		else{
			messageModel.setCode(1);
			messageModel.setSuccess(false);
			messageModel.setDescription("Error when adding nurse process to database!");

			return messageModel;
		}

	}

	
	@JWTTokenNeeded
	@Path("/updateAttendance")
	@POST
	public MessageModel updateAttendance(
			@Context HttpHeaders httpHeaders,
			@FormParam("_id_diagnosis_procedure") String idDiagnosisProcedure,
			@FormParam("_anotation") String anotation,
			@FormParam("_status") String status) {

		MessageModel messageModel = new MessageModel();
	

		boolean validationOk = Validate.validateNotNull(idDiagnosisProcedure, anotation, status);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idDiagnosisProcedure, anotation and status should not be null");
			return messageModel;
		}

		nurseDAO.setHttpHeaders(httpHeaders);

		boolean ok = nurseDAO.updateNurseProcedure(idDiagnosisProcedure, status, anotation);

		if(ok){
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Nurse process updated with success!");
			return messageModel;
		}
		else{
			messageModel.setCode(1);
			messageModel.setSuccess(false);
			messageModel.setDescription("Error when updating nurse process to database!");

			return messageModel;
		}

	}


	@JWTTokenNeeded
	@Path("/listAttendance")
	@GET
	public MessageListModel<NurseProcessModel> listAttendance(@Context HttpHeaders httpHeaders,
			@QueryParam("idHealthInstitution") String idHealthInstitution, @QueryParam("idNurse") String idNurse,
			@QueryParam("status") String status) {

		MessageListModel<NurseProcessModel> messageModel = new MessageListModel<>();

		nurseDAO.setHttpHeaders(httpHeaders);

		List<NurseProcessModel> listNurseProcedure = nurseDAO.listAttendance(idHealthInstitution, idNurse, status);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Nurse process retrieved with success!");
		messageModel.setList(listNurseProcedure);
		return messageModel;
	}

}
