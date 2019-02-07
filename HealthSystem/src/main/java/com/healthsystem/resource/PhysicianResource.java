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
import com.healthsystem.dao.PhysicianDAO;
import com.healthsystem.filter.JWTTokenNeeded;
import com.healthsystem.model.DiagnosisHistoryModel;
import com.healthsystem.model.ExamHistoryModel;
import com.healthsystem.model.GraphModel;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.MessageListModel;
import com.healthsystem.model.MessageModel;
import com.healthsystem.model.PhysicianAttendanceModel;
import com.healthsystem.model.PhysicianModel;
import com.healthsystem.model.PhysicianWorksOnHealthInstitutionModel;
import com.healthsystem.model.SpecializationModel;
import com.healthsystem.model.UserModel;

@Path("/physician")
@Produces(MediaType.APPLICATION_JSON)
public class PhysicianResource {
	private PhysicianDAO physicianDAO = new PhysicianDAO();

	@Context
	private UriInfo uriInfo;

	@JWTTokenNeeded
	@POST
	public MessageModel add(
			@Context HttpHeaders httpHeaders,
			@FormParam("_id_user") String idUser,
			@FormParam("_id_physician") String idPhysician,
			@FormParam("_practice_document") String practiceDocument) {

		PhysicianModel physicianModel = new PhysicianModel();
		physicianModel.setIdPhysician(idPhysician);
		physicianModel.setIdUser(idUser);
		physicianModel.setPracticeDocument(practiceDocument);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PhysicianModel>> constraintViolations = validator.validate(physicianModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		physicianDAO.setHttpHeaders(httpHeaders);

		boolean status = physicianDAO.add(physicianModel);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Physician added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding physician to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@PUT
	public MessageModel update(
			@Context HttpHeaders httpHeaders,
			@FormParam("_id_user") String idUser, @FormParam("_id_physician") String idPhysician,
			@FormParam("_practice_document") String practiceDocument) {
		PhysicianModel physicianModel = new PhysicianModel();
		physicianModel.setIdPhysician(idPhysician);
		physicianModel.setIdUser(idUser);
		physicianModel.setPracticeDocument(practiceDocument);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PhysicianModel>> constraintViolations = validator.validate(physicianModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		physicianDAO.setHttpHeaders(httpHeaders);

		boolean status = physicianDAO.update(physicianModel);

		if (status) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Physician updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating physician to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@GET
	public MessageListModel<PhysicianModel> getPhysician(
			@Context HttpHeaders httpHeaders,
			@QueryParam("id_user") String idUser) {

		MessageListModel<PhysicianModel> messageModel = new MessageListModel<>();
		
		physicianDAO.setHttpHeaders(httpHeaders);

		PhysicianModel physician = physicianDAO.getPhysician(idUser);

		if (physician != null) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Physician retrieved with success!");
			List<PhysicianModel> list = new ArrayList<>();
			list.add(physician);
			messageModel.setList(list);
			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error retrieving physician from database!");

		return messageModel;
	}

	
	@JWTTokenNeeded
	@Path("/userdata")
	@GET  
	public MessageListModel<UserModel> getPhysicianUser(
			@Context HttpHeaders httpHeaders,
			@QueryParam("id_physician") String idPhysician) {

		MessageListModel<UserModel> messageModel = new MessageListModel<>();
		
		physicianDAO.setHttpHeaders(httpHeaders);

		UserModel userModel = physicianDAO.getUser(idPhysician);

		if (userModel != null) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Physician retrieved with success!");
			List<UserModel> list = new ArrayList<>();
			list.add(userModel);
			messageModel.setList(list);
			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error retrieving physician from database!");

		return messageModel;
	}

	
	@JWTTokenNeeded
	@Path("/listSpecializations")
	@GET
	public MessageListModel<SpecializationModel> listPhysicianSpecialization(
			@Context HttpHeaders httpHeaders,
			@QueryParam("id_physician") String idPhysician) {

		MessageListModel<SpecializationModel> messageModel = new MessageListModel<>();
		
		physicianDAO.setHttpHeaders(httpHeaders);

		List<SpecializationModel> listPhysicianSpecialization = physicianDAO.listPhysicianSpecialization(idPhysician);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List of physician specializations retrieved with success!");
		messageModel.setList(listPhysicianSpecialization);
		return messageModel;
	}
	
	@JWTTokenNeeded
	@Path("/specialization")
	@GET
	public MessageListModel<SpecializationModel> listSpecialization(
			@Context HttpHeaders httpHeaders,
			@QueryParam("country") String country,
			@QueryParam("language") String language
			) {

		MessageListModel<SpecializationModel> messageModel = new MessageListModel<>();
		
		physicianDAO.setHttpHeaders(httpHeaders);

		List<SpecializationModel> listSpecialization = physicianDAO.listSpecialization(country, language);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List of specializations retrieved with success!");
		messageModel.setList(listSpecialization);
		return messageModel;
	}

	
	@JWTTokenNeeded
	@Path("/bindSpecialization")
	@POST
	public MessageModel bindSpecialization(
			@Context HttpHeaders httpHeaders,
			@FormParam("_id_physician") String idPhysician,
			@FormParam("_id_specialization") String idSpecialization
			) {

		MessageModel messageModel = new MessageModel();
		
		physicianDAO.setHttpHeaders(httpHeaders);

		boolean ok = physicianDAO.bindSpecialization(idPhysician, idSpecialization);

		if(ok){
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
	public MessageModel unbindSpecialization(
			@Context HttpHeaders httpHeaders,
			@FormParam("_id_physician") String idPhysician,
			@FormParam("_id_specialization") String idSpecialization
			) {

		MessageModel messageModel = new MessageModel();
		
		physicianDAO.setHttpHeaders(httpHeaders);

		boolean ok = physicianDAO.unbindSpecialization(idPhysician, idSpecialization);

		if(ok){
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
	@Path("/listDiagnosisHistory")
	@GET
	public MessageListModel<DiagnosisHistoryModel> listDiagnosisHistory(
			@Context HttpHeaders httpHeaders,
			@QueryParam("id_physician") String idPhysician,
			@QueryParam("id_health_institution") String idHealthInstitution

			) {
		

		MessageListModel<DiagnosisHistoryModel> messageModel = new MessageListModel<>();
		
		physicianDAO.setHttpHeaders(httpHeaders);

		List<DiagnosisHistoryModel> listDiagnosisHistory = physicianDAO.listDiagnosisHistory(idPhysician, idHealthInstitution);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List of diagnosis history retrieved with success!");
		messageModel.setList(listDiagnosisHistory);
		return messageModel;
	}
	
	
	@JWTTokenNeeded
	@Path("/listExamsHistory")
	@GET
	public MessageListModel<ExamHistoryModel> listExamHistory(
			@Context HttpHeaders httpHeaders,
			@QueryParam("id_physician") String idPhysician,
			@QueryParam("id_health_institution") String idHealthInstitution
			) {
		

		MessageListModel<ExamHistoryModel> messageModel = new MessageListModel<>();

		physicianDAO.setHttpHeaders(httpHeaders);
		List<ExamHistoryModel> listExamHistory = physicianDAO.listExamHistory(idPhysician, idHealthInstitution);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List of exam history retrieved with success!");
		messageModel.setList(listExamHistory);
		return messageModel;
	}
	
	@JWTTokenNeeded
	@GET
	@Path("/listPhysicianInNearArea")
	public MessageListModel<PhysicianModel> listPhysiciansInNearArea(
			@Context HttpHeaders httpHeaders,
			@QueryParam("latitute") double latitute,
			@QueryParam("longitute") double longitute, @QueryParam("km") int km,
			@QueryParam("language") String language,
			@QueryParam("specializationList") List<String> specializationList) {
		

		MessageListModel<PhysicianModel> messageModel = new MessageListModel<PhysicianModel>();

		if (km <= 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("Inform a km that is greater than 0!");
			return messageModel;
		}
		
		
		physicianDAO.setHttpHeaders(httpHeaders);

		List<PhysicianWorksOnHealthInstitutionModel> listPhysicianInNearArea = physicianDAO.listPhysicianInNearArea(latitute, longitute, km, language, specializationList);
		
		
		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List with physicians successfully loaded!");
		messageModel.setList(listPhysicianInNearArea);


		return messageModel;

	}
	
	
	
	@JWTTokenNeeded
	@Path("/physicianAttendanceRegister")
	@POST
	public MessageModel physicianAttendanceRegister(
			@Context HttpHeaders httpHeaders, 
			@FormParam("_id_physician_attendance") String idPhysicianAttendance,
			@FormParam("_date_attendance") String dateAttendance, 
			@FormParam("_id_patient") String idPatient,
			@FormParam("_id_physician") String idPhysician,
			@FormParam("_id_health_institution") String idHealthInstitution
			) {
		MessageModel messageModel = new MessageModel();

		PhysicianAttendanceModel physicianAttendanceModel = new PhysicianAttendanceModel();
		physicianAttendanceModel.setIdPhysicianAttendance(idPhysicianAttendance);
		physicianAttendanceModel.setDateAttendance(dateAttendance);
		physicianAttendanceModel.setIdPatient(idPatient);
		physicianAttendanceModel.setIdPhysician(idPhysician);
		physicianAttendanceModel.setIdHealthInstitution(idHealthInstitution);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PhysicianAttendanceModel>> constraintViolations = validator
				.validate(physicianAttendanceModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		physicianDAO.setHttpHeaders(httpHeaders);

		boolean ok = physicianDAO.registerPhysicianAttendance(idPhysicianAttendance, dateAttendance, idPatient, idPhysician, idHealthInstitution);

		messageModel.setCode(0);
		messageModel.setSuccess(ok);
		if(ok)
			messageModel.setDescription("Add physician attendance successfully!");
		else
			messageModel.setDescription("Error on physician attendance registration!");
		return messageModel;
	}
	
	@JWTTokenNeeded
	@Path("/listPhysicianAttendance")
	@GET
	public MessageListModel<PhysicianAttendanceModel> listPhysicianAttendance(
			@Context HttpHeaders httpHeaders,
			@QueryParam("id_physician") String idPhysician,
			@QueryParam("id_health_institution") String idHealthInstitution,
			@QueryParam("date") String date

			) {
		

		MessageListModel<PhysicianAttendanceModel> messageModel = new MessageListModel<>();

		physicianDAO.setHttpHeaders(httpHeaders);
		List<PhysicianAttendanceModel> listPhysicianAttendance = physicianDAO.listPhysicianAttendance(idPhysician, idHealthInstitution, date);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("List of physician attendance retrieved with success!");
		messageModel.setList(listPhysicianAttendance);
		return messageModel;
	}
	
	
	@GET
	@Path("/listAttendanceGraph")
	public MessageListModel<GraphModel> listPhysicianAttendancesGraph(
			@QueryParam("id_physician") String idPhysician,
			@QueryParam("id_health_institution") String idHealthInstitution) {
		   
	     
		System.out.println(idPhysician + " --- " + idHealthInstitution);
		MessageListModel<GraphModel> messageModel = new MessageListModel<GraphModel>();
     

		List<GraphModel> list = physicianDAO.listPhysicianAttendancesGraph(idPhysician, idHealthInstitution);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Error sucessfully listed!");
		messageModel.setList(list);


		return messageModel;

	}
	


}
