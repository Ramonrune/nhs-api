package com.healthsystem.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Response.ResponseBuilder;

import com.healthsystem.dao.PatientDAO;
import com.healthsystem.filter.JWTTokenNeeded;
import com.healthsystem.model.DeficiencyModel;
import com.healthsystem.model.DiagnosisModel;
import com.healthsystem.model.DiagnosisProcedureModel;
import com.healthsystem.model.DiseaseModel;
import com.healthsystem.model.ExamAttachmentModel;
import com.healthsystem.model.ExamModel;
import com.healthsystem.model.HistoryModel;
import com.healthsystem.model.MedicineModel;
import com.healthsystem.model.MessageListModel;
import com.healthsystem.model.MessageModel;
import com.healthsystem.model.PatientHasDeficiencyModel;
import com.healthsystem.model.PatientHasDiseaseModel;
import com.healthsystem.model.PatientModel;
import com.healthsystem.model.PatientTagModel;
import com.healthsystem.model.PatientUseMedicineModel;
import com.healthsystem.model.PhysicianWorksOnHealthInstitutionModel;
import com.healthsystem.model.UserModel;
import com.healthsystem.util.AzureBlob;
import com.healthsystem.util.Validate;

@Path("/patient")
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

	private PatientDAO patientDAO = new PatientDAO();

	@JWTTokenNeeded
	@POST
	public MessageModel add(@Context HttpHeaders httpHeaders, @FormParam("_id_patient") String idPatient,
			@FormParam("_blood_type") String bloodType, @FormParam("_color") String color,
			@FormParam("_father_name") String fatherName, @FormParam("_mother_name") String motherName,
			@FormParam("_weight") double weight, @FormParam("_height") double height,
			@FormParam("_status") String status, @FormParam("_id_user") String idUser) {
		patientDAO.setHttpHeaders(httpHeaders);

		PatientModel patientModel = new PatientModel();
		patientModel.setIdPatient(idPatient);
		patientModel.setBloodType(bloodType);
		patientModel.setColor(color);
		patientModel.setFatherName(fatherName);
		patientModel.setMotherName(motherName);
		patientModel.setWeight(weight);
		patientModel.setHeight(height);
		patientModel.setStatus(status);
		patientModel.setIdUser(idUser);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PatientModel>> constraintViolations = validator.validate(patientModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		boolean ok = patientDAO.checkIfUserExist(patientModel.getIdUser());
		if (ok) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(patientDAO.getPatient(patientModel.getIdUser()).getIdPatient());

			return messageModel;
		}

		ok = patientDAO.add(patientModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Patient added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding patient to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@PUT
	public MessageModel update(@Context HttpHeaders httpHeaders, @FormParam("_id_patient") String idPatient,
			@FormParam("_blood_type") String bloodType, @FormParam("_color") String color,
			@FormParam("_father_name") String fatherName, @FormParam("_mother_name") String motherName,
			@FormParam("_weight") double weight, @FormParam("_height") double height,
			@FormParam("_status") String status, @FormParam("_id_user") String idUser) {

		PatientModel patientModel = new PatientModel();
		patientModel.setIdPatient(idPatient);
		patientModel.setBloodType(bloodType);
		patientModel.setColor(color);
		patientModel.setFatherName(fatherName);
		patientModel.setMotherName(motherName);
		patientModel.setWeight(weight);
		patientModel.setHeight(height);
		patientModel.setStatus(status);
		patientModel.setIdUser(idUser);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PatientModel>> constraintViolations = validator.validate(patientModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.update(patientModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Patient updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating patient to database!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/patientData")
	@GET
	public MessageModel getPatient(@Context HttpHeaders httpHeaders, @QueryParam("id_user") String idUser) {

		MessageListModel<PatientModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		PatientModel patient = patientDAO.getPatient(idUser);

		List<PatientModel> list = new ArrayList<>();
		list.add(patient);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setList(list);
		messageModel.setDescription("Patient sucessfully retrieved!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/tagRegister")
	@POST
	public MessageModel tagRegister(@Context HttpHeaders httpHeaders, @FormParam("_id_patient_tag") String idPatientTag,
			@FormParam("_id_tag") String idTag, @FormParam("_id_patient") String idPatient) {
		MessageModel messageModel = new MessageModel();

		patientDAO.setHttpHeaders(httpHeaders);

		boolean exists = patientDAO.registerTag(idPatientTag, idTag, idPatient);

		messageModel.setCode(0);
		messageModel.setSuccess(exists);
		messageModel.setDescription("Tag registered sucessfully!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/tagUpdate")
	@PUT
	public MessageModel tagUpdate(@Context HttpHeaders httpHeaders, @FormParam("_id_patient_tag") String idPatientTag,
			@FormParam("_name") String name, @FormParam("_tag_type") String tagType) {
		MessageModel messageModel = new MessageModel();

		patientDAO.setHttpHeaders(httpHeaders);

		boolean exists = patientDAO.updateTag(idPatientTag, name, tagType);

		messageModel.setCode(0);
		messageModel.setSuccess(exists);
		messageModel.setDescription("Tag updated sucessfully!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/tagDelete")
	@DELETE
	public MessageModel tagDelete(@Context HttpHeaders httpHeaders, @FormParam("_id_patient_tag") String idPatientTag) {
		MessageModel messageModel = new MessageModel();

		patientDAO.setHttpHeaders(httpHeaders);

		boolean exists = patientDAO.unregisterTag(idPatientTag);

		messageModel.setCode(0);
		messageModel.setSuccess(exists);
		messageModel.setDescription("Tag deleted sucessfully!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/listPatientTag")
	@GET
	public MessageModel listPatientTag(@Context HttpHeaders httpHeaders, @QueryParam("id_patient") String idPatient) {

		MessageListModel<PatientTagModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<PatientTagModel> listPatientTag = patientDAO.listPatientTag(idPatient);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setList(listPatientTag);
		messageModel.setDescription("Tags for this patient sucessfully retrieved!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/tagExist")
	@GET
	public MessageModel tagExist(@Context HttpHeaders httpHeaders, @QueryParam("id_tag") String idTag) {

		MessageListModel<PatientModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		boolean exists = patientDAO.tagExist(idTag);

		messageModel.setCode(0);
		messageModel.setSuccess(exists);
		messageModel.setDescription("Tag search retrieved sucessfully!");

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/tag")
	@GET
	public MessageListModel<PatientModel> getPatientByTagCode(@Context HttpHeaders httpHeaders,
			@QueryParam("id_tag") String idTag) {

		MessageListModel<PatientModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		PatientModel patientByTagCode = patientDAO.getPatientByTagCode(idTag);
		List<PatientModel> list = new ArrayList<>();
		list.add(patientByTagCode);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Patient retrieved with success!");
		messageModel.setList(list);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/diagnosis")
	@POST
	public MessageModel addDiagnosis(@Context HttpHeaders httpHeaders, @FormParam("_id_diagnosis") String idDiagnosis,
			@FormParam("_date_diagnosis") String dateDiagnosis, @FormParam("_anotation") String anotation,
			@FormParam("_id_patient") String idPatient, @FormParam("_id_physician") String idPhysician,
			@FormParam("_id_health_institution") String idHealthInstitution) {

		DiagnosisModel diagnosisModel = new DiagnosisModel();
		diagnosisModel.setAnnotation(anotation);
		diagnosisModel.setDateDiagnosis(dateDiagnosis);
		diagnosisModel.setIdDiagnosis(idDiagnosis);
		diagnosisModel.setIdPhysician(idPhysician);
		diagnosisModel.setIdPatient(idPatient);
		diagnosisModel.setIdHealthInstitution(idHealthInstitution);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<DiagnosisModel>> constraintViolations = validator.validate(diagnosisModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.addDiagnosis(diagnosisModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Diagnosis added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding diagnosis to database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/diagnosis")
	@PUT
	public MessageModel updateDiagnosis(@Context HttpHeaders httpHeaders,
			@FormParam("_id_diagnosis") String idDiagnosis, @FormParam("_anotation") String anotation) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idDiagnosis);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idDiagnosis should not be null");
			return messageModel;
		}
		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.updateDiagnosis(idDiagnosis, anotation);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Diagnosis updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating diagnosis to database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/diagnosis")
	@DELETE
	public MessageModel deleteDiagnosis(@Context HttpHeaders httpHeaders,
			@FormParam("_id_diagnosis") String idDiagnosis) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idDiagnosis);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idDiagnosis should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.deleteDiagnosis(idDiagnosis);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Diagnosis deleted with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when deleting diagnosis to database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/diagnosis")
	@GET
	public MessageListModel<DiagnosisModel> getPatientDiagnosis(@Context HttpHeaders httpHeaders,
			@QueryParam("id_patient") String idPatient) {

		MessageListModel<DiagnosisModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<DiagnosisModel> listPatientDiagnosis = patientDAO.listPatientDiagnosis(idPatient);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Diagnosis retrieved with success!");
		messageModel.setList(listPatientDiagnosis);

		return messageModel;
	}
	
	@JWTTokenNeeded
	@Path("/diagnosis/procedures")
	@GET
	public MessageListModel<DiagnosisProcedureModel> getDiagnosisProcedure(@Context HttpHeaders httpHeaders,
			@QueryParam("id_diagnosis") String idDiagnosis) {

		MessageListModel<DiagnosisProcedureModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		DiagnosisProcedureModel diagnosisProcedures = patientDAO.getDiagnosisProcedures(idDiagnosis);
		List<DiagnosisProcedureModel> listDiagnosisProcedures = new ArrayList<>();
		listDiagnosisProcedures.add(diagnosisProcedures);
		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Procedure retrieved with success!");
		messageModel.setList(listDiagnosisProcedures);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/disease")
	@GET
	public MessageListModel<DiseaseModel> getDiseases(@Context HttpHeaders httpHeaders) {

		MessageListModel<DiseaseModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<DiseaseModel> listDisease = patientDAO.listDiseases();

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Diseases retrieved with success!");
		messageModel.setList(listDisease);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/disease")
	@POST
	public MessageModel addDisease(@Context HttpHeaders httpHeaders,
			@FormParam("_id_patient_has_disease") String idPatientHasDisease,
			@FormParam("_id_patient") String idPatient, @FormParam("_id_disease") String idDisease,
			@FormParam("_anotation") String anotation) {

		PatientHasDiseaseModel patientHasDiseaseModel = new PatientHasDiseaseModel();
		patientHasDiseaseModel.setIdPatientHasDisease(idPatientHasDisease);
		patientHasDiseaseModel.setIdPatient(idPatient);
		patientHasDiseaseModel.setIdDisease(idDisease);
		patientHasDiseaseModel.setAnotation(anotation);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PatientHasDiseaseModel>> constraintViolations = validator
				.validate(patientHasDiseaseModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.bindDisease(patientHasDiseaseModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Disease binded with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when binding disease in database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/disease")
	@PUT
	public MessageModel updateDisease(@Context HttpHeaders httpHeaders,
			@FormParam("_id_patient_has_disease") String idPatientHasDisease,
			@FormParam("_anotation") String anotation) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idPatientHasDisease);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idPatientHasDisease should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.updateBindDisease(idPatientHasDisease, anotation);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Disease updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating disease on database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/disease")
	@DELETE
	public MessageModel deleteDisease(@Context HttpHeaders httpHeaders,
			@FormParam("_id_patient_has_disease") String idPatientHasDisease) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idPatientHasDisease);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idPatientHasDisease should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.unbindDisease(idPatientHasDisease);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Disease unbinded with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when unbinding disease on database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/patientDisease")
	@GET
	public MessageListModel<PatientHasDiseaseModel> getPatientDiseases(@Context HttpHeaders httpHeaders,
			@QueryParam("id_patient") String idPatient) {

		MessageListModel<PatientHasDiseaseModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<PatientHasDiseaseModel> listDisease = patientDAO.listPatientDiseases(idPatient);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Diseases from patient retrieved with success!");
		messageModel.setList(listDisease);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/exam")
	@POST
	public MessageModel addExam(@Context HttpHeaders httpHeaders, @FormParam("_id_exam") String idExam,
			@FormParam("_anotation") String anotation, @FormParam("_id_patient") String idPatient,
			@FormParam("_id_physician") String idPhysician,
			@FormParam("_id_health_institution") String idHealthInstitution, @FormParam("_date_exam") String dateExam) {

		ExamModel examModel = new ExamModel();
		examModel.setIdExam(idExam);
		examModel.setAnotation(anotation);
		examModel.setIdPatient(idPatient);
		examModel.setIdPhysician(idPhysician);
		examModel.setIdHealthInstitution(idHealthInstitution);
		examModel.setDateExam(dateExam);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ExamModel>> constraintViolations = validator.validate(examModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.addExam(examModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Exam added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding exam in database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/exam")
	@PUT
	public MessageModel updateExam(@Context HttpHeaders httpHeaders, @FormParam("_id_exam") String idExam,
			@FormParam("_anotation") String anotation) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idExam);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idExam should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.updateExam(idExam, anotation);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Exam updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating exam on database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/exam")
	@DELETE
	public MessageModel deleteExam(@Context HttpHeaders httpHeaders, @FormParam("_id_exam") String idExam) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idExam);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idExam should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.deleteExam(idExam);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Exam deleted with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when deleting exam on database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/exam")
	@GET
	public MessageListModel<ExamModel> getPatientExams(@Context HttpHeaders httpHeaders,
			@QueryParam("id_patient") String idPatient) {

		MessageListModel<ExamModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<ExamModel> listDisease = patientDAO.listPatientExams(idPatient);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Exams from patient retrieved with success!");
		messageModel.setList(listDisease);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/exam/attachment")
	@POST
	public MessageModel addAttachment(@Context HttpHeaders httpHeaders,
			@FormParam("_id_exam_attachment") String idExamAttachment,
			@FormParam("_attachment_name") String attachmentName, @FormParam("_id_exam") String idExam) {

		ExamAttachmentModel examAttachmentModel = new ExamAttachmentModel();
		examAttachmentModel.setIdExamAttachment(idExamAttachment);
		examAttachmentModel.setIdExam(idExam);
		examAttachmentModel.setAttachmentName(attachmentName);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ExamAttachmentModel>> constraintViolations = validator.validate(examAttachmentModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.addAttachment(examAttachmentModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Attachment added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding attachment in database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/exam/attachment")
	@DELETE
	public MessageModel deleteAttachment(@Context HttpHeaders httpHeaders,
			@FormParam("_id_exam_attachment") String idExamAttachment) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idExamAttachment);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idExamAttachment should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.deleteAttachment(idExamAttachment);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Attachment deleted with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when deleting attachment on database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/exam/attachment")
	@GET
	public MessageListModel<ExamAttachmentModel> getExamAttachments(@Context HttpHeaders httpHeaders,
			@QueryParam("id_exam") String idExam) {

		MessageListModel<ExamAttachmentModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<ExamAttachmentModel> listDisease = patientDAO.listExamAttachments(idExam);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Attachments from exam retrieved with success!");
		messageModel.setList(listDisease);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/deficiency")
	@GET
	public MessageListModel<DeficiencyModel> getDeficiency(@QueryParam("country") String country) {

		MessageListModel<DeficiencyModel> messageModel = new MessageListModel<>();

		List<DeficiencyModel> listDeficiency = patientDAO.listDeficiences(country);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Deficiencies retrieved with success!");
		messageModel.setList(listDeficiency);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/deficiency")
	@POST
	public MessageModel addDeficiency(@FormParam("_id_patient_has_deficiency") String idPatientHasDeficiency,
			@FormParam("_id_patient") String idPatient, @FormParam("_id_disease") String idDeficiency,
			@FormParam("_anotation") String anotation) {

		PatientHasDeficiencyModel patientHasDeficiencyModel = new PatientHasDeficiencyModel();
		patientHasDeficiencyModel.setIdPatientHasDeficiency(idPatientHasDeficiency);
		patientHasDeficiencyModel.setIdPatient(idPatient);
		patientHasDeficiencyModel.setIdDeficiency(idDeficiency);
		patientHasDeficiencyModel.setAnotation(anotation);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PatientHasDeficiencyModel>> constraintViolations = validator
				.validate(patientHasDeficiencyModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		boolean ok = patientDAO.bindDeficiency(patientHasDeficiencyModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Deficiency binded with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when binding deficiency in database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/deficiency")
	@PUT
	public MessageModel updateDeficiency(@FormParam("_id_patient_has_deficiency") String idPatientHasDeficiency,
			@FormParam("_anotation") String anotation) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idPatientHasDeficiency);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idPatientHasDeficiency should not be null");
			return messageModel;
		}

		boolean ok = patientDAO.updateBindDeficiency(idPatientHasDeficiency, anotation);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Deficiency updated with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when updating deficiency on database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/deficiency")
	@DELETE
	public MessageModel deleteDeficiency(@FormParam("_id_patient_has_deficiency") String idPatientHasDeficiency) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idPatientHasDeficiency);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idPatientHasDeficiency should not be null");
			return messageModel;
		}

		boolean ok = patientDAO.unbindDeficiency(idPatientHasDeficiency);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Deficiency unbinded with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when unbinding deficiency on database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/patientDeficiency")
	@GET
	public MessageListModel<PatientHasDeficiencyModel> getPatientDeficiencies(
			@QueryParam("id_patient") String idPatient) {

		MessageListModel<PatientHasDeficiencyModel> messageModel = new MessageListModel<>();

		List<PatientHasDeficiencyModel> listDisease = patientDAO.listPatientDeficiencies(idPatient);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Deficiencies from patient retrieved with success!");
		messageModel.setList(listDisease);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/listMedicines")
	@GET
	public MessageListModel<MedicineModel> listMedicines(@Context HttpHeaders httpHeaders,
			@QueryParam("search") String search, @QueryParam("language") String language,
			@QueryParam("country") String country, @QueryParam("status") String status) {

		MessageListModel<MedicineModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<MedicineModel> listMedicines = patientDAO.listMedicines(search, country, language, status);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Medicines retrieved with success!");
		messageModel.setList(listMedicines);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/registerMedicine")
	@POST
	public MessageModel addMedicine(@Context HttpHeaders httpHeaders, @FormParam("_id_medicine") String idMedicine,
			@FormParam("_name") String name, @FormParam("_language") String language,
			@FormParam("_country") String country, @FormParam("_status") String status) {

		MedicineModel medicineModel = new MedicineModel();
		medicineModel.setIdMedicine(idMedicine);
		medicineModel.setName(name);
		medicineModel.setLanguage(language);
		medicineModel.setCountry(country);
		medicineModel.setStatus(status);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<MedicineModel>> constraintViolations = validator.validate(medicineModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.addMedicine(medicineModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Medicine added with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when adding medicine in database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/bindMedicine")
	@POST
	public MessageModel bindMedicine(@Context HttpHeaders httpHeaders,
			@FormParam("_id_patient_use_medicine") String idPatientUseMedicine,
			@FormParam("_id_patient") String idPatient, @FormParam("_id_medicine") String idMedicine) {

		PatientUseMedicineModel patientUseMedicineModel = new PatientUseMedicineModel();
		patientUseMedicineModel.setIdPatientUseMedicine(idPatientUseMedicine);
		patientUseMedicineModel.setIdPatient(idPatient);
		patientUseMedicineModel.setIdMedicine(idMedicine);

		MessageModel messageModel = new MessageModel();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<PatientUseMedicineModel>> constraintViolations = validator
				.validate(patientUseMedicineModel);
		if (constraintViolations.size() > 0) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription(constraintViolations.iterator().next().getMessage());
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.bindMedicine(patientUseMedicineModel);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Medicine binded with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when binding medicine in database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/unbindMedicine/{idPatient}")
	@DELETE
	public MessageModel unbindMedicine(@Context HttpHeaders httpHeaders, @PathParam("idPatient") String idPatient) {

		MessageModel messageModel = new MessageModel();

		boolean validationOk = Validate.validateNotNull(idPatient);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idPatient should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		boolean ok = patientDAO.unbindMedicine(idPatient);

		if (ok) {
			messageModel.setCode(0);
			messageModel.setSuccess(true);
			messageModel.setDescription("Medicines unbinded with success!");

			return messageModel;
		}

		messageModel.setCode(1);
		messageModel.setSuccess(false);
		messageModel.setDescription("Error when unbinding medicines in database!");

		return messageModel;

	}

	@JWTTokenNeeded
	@Path("/listPatientMedicines")
	@GET
	public MessageListModel<MedicineModel> listPatientMedicines(@Context HttpHeaders httpHeaders,
			@QueryParam("id_patient") String idPatient, @QueryParam("language") String language) {

		MessageListModel<MedicineModel> messageModel = new MessageListModel<>();

		patientDAO.setHttpHeaders(httpHeaders);

		List<MedicineModel> listMedicines = patientDAO.listPatientMedicines(idPatient, language);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Patient medicines retrieved with success!");
		messageModel.setList(listMedicines);

		return messageModel;
	}

	@JWTTokenNeeded
	@Path("/diagnosis/image")
	@Produces("image/jpeg")
	@GET
	public Response getDiagnosisSignature(@Context HttpHeaders httpHeaders,
			@QueryParam("idDiagnosis") String idDiagnosis) {

		boolean validateOk = Validate.validateNotNull(idDiagnosis);
		if (!validateOk) {
			byte[] downloadInputStream = AzureBlob.downloadInputStream("NO_SIGNATURE" + ".jpg", "diagnosis");

			return Response.ok(downloadInputStream).build();
		}

		byte[] downloadInputStream = AzureBlob.downloadInputStream(idDiagnosis + ".jpg", "diagnosis");

		if (downloadInputStream == null) {
			downloadInputStream = AzureBlob.downloadInputStream("NO_SIGNATURE" + ".jpg", "diagnosis");
		}

		return Response.ok(downloadInputStream).build();

	}

	@JWTTokenNeeded
	@Path("/exam/attachment/document")

	@Produces({ MediaType.TEXT_PLAIN, "image/jpeg", "image/png", "image/gif", "text/csv", "audio/mpeg", "audio/ogg",
			"application/zip", "application/pdf", "application/msword",
			"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel",
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint",
			"application/vnd.openxmlformats-officedocument.presentationml.presentation",
			"application/vnd.oasis.opendocument.text" })
	@GET
	public Response getExamSignature(@Context HttpHeaders httpHeaders,
			@QueryParam("idExamAttachment") String idExamAttachment) {

		boolean validateOk = Validate.validateNotNull(idExamAttachment);
		if (!validateOk) {

			return Response.ok().build();
		}

		patientDAO.setHttpHeaders(httpHeaders);
		ExamAttachmentModel examAttachment = patientDAO.getExamAttachment(idExamAttachment);

		if (examAttachment != null) {
			String extension = "";
			String name = examAttachment.getAttachmentName();
			int lastIndexOf = name.lastIndexOf(".");
			if (lastIndexOf == -1) {
				extension = "";
			} else {
				extension = name.substring(lastIndexOf);
			}

			String mediaType = "";

			if (extension.equals(".txt")) {
				mediaType = MediaType.TEXT_PLAIN;
			}
			if (extension.equals(".jpg")) {
				mediaType = "image/jpeg";

			}
			if (extension.equals(".png")) {
				mediaType = "image/png";

			}

			if (extension.equals(".gif")) {
				mediaType = "image/gif";

			}

			if (extension.equals(".csv")) {
				mediaType = "text/csv";

			}

			if (extension.equals(".mpeg")) {
				mediaType = "audio/mpeg";

			}

			if (extension.equals(".ogg")) {
				mediaType = "audio/ogg";

			}

			if (extension.equals(".zip")) {
				mediaType = "application/zip";

			}

			if (extension.equals(".pdf")) {
				mediaType = "application/pdf";

			}

			if (extension.equals(".doc")) {
				mediaType = "application/msword";

			}

			if (extension.equals(".docx")) {
				mediaType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

			}

			if (extension.equals(".xls")) {
				mediaType = "application/vnd.ms-excel";

			}

			if (extension.equals(".xlsx")) {
				mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

			}

			if (extension.equals(".ppt")) {
				mediaType = "application/vnd.ms-powerpoint";

			}

			if (extension.equals(".pptx")) {
				mediaType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

			}

			if (extension.equals(".odt")) {
				mediaType = "application/vnd.oasis.opendocument.text";

			}
			try {
				System.out.println(mediaType + "----------");
				byte[] downloadInputStream = AzureBlob.downloadInputStream(idExamAttachment + extension,
						"exam-attachments");
				MediaType md = new MediaType(mediaType.split("/")[0], mediaType.split("/")[1]);

				Response build = Response.ok(downloadInputStream, md).build();

				System.out.println(build.getHeaderString("Content-Type"));
				return build;

			} catch (Exception e) {
				return Response.ok().build();

			}
		} else {

			return Response.ok().build();
		}

	}

	@JWTTokenNeeded
	@Path("/listPatientHistory")
	@GET
	public MessageListModel<HistoryModel> listPatientHistory(@Context HttpHeaders httpHeaders,
			@QueryParam("id_patient") String idPatient) {
		MessageListModel<HistoryModel> messageModel = new MessageListModel<>();

		boolean validationOk = Validate.validateNotNull(idPatient);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idPatient should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		List<HistoryModel> listHistory = patientDAO.listPatientHistory(idPatient);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Patient history retrieved with success!");
		messageModel.setList(listHistory);

		return messageModel;
	}
	
	@JWTTokenNeeded
	@Path("/listKnownPhysicians")
	@GET
	public MessageListModel<PhysicianWorksOnHealthInstitutionModel> listKnownPhysicians(@Context HttpHeaders httpHeaders,
			@QueryParam("id_patient") String idPatient,
			@QueryParam("language") String language) {
		MessageListModel<PhysicianWorksOnHealthInstitutionModel> messageModel = new MessageListModel<>();

		boolean validationOk = Validate.validateNotNull(idPatient);

		if (!validationOk) {
			messageModel.setCode(-1);
			messageModel.setSuccess(false);
			messageModel.setDescription("idPatient should not be null");
			return messageModel;
		}

		patientDAO.setHttpHeaders(httpHeaders);

		List<PhysicianWorksOnHealthInstitutionModel> physicianList = patientDAO.listKnownPhysicians(idPatient, language);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Physician list retrieved with success!");
		messageModel.setList(physicianList);

		return messageModel;
	}
}
