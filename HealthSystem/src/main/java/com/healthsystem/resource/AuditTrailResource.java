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

import com.healthsystem.dao.AuditTrailDAO;
import com.healthsystem.dao.ErrorLogDAO;
import com.healthsystem.dao.HealthInstitutionDAO;
import com.healthsystem.filter.JWTTokenNeeded;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.ErrorLogModel;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.MessageListModel;
import com.healthsystem.model.MessageModel;
import com.healthsystem.model.PhysicianWorksOnHealthInstitutionModel;
import com.healthsystem.model.UserModel;
import com.healthsystem.util.AzureBlob;
import com.healthsystem.util.JwtUtil;
import com.healthsystem.util.Validate;
import com.pusher.rest.Pusher;

@Path("/auditTrail")
@Produces(MediaType.APPLICATION_JSON) 
public class AuditTrailResource {

	private AuditTrailDAO auditTrailDAO = new AuditTrailDAO();
	
	@Context
	private UriInfo uriInfo;
	
	@JWTTokenNeeded
	@GET
	@Path("/listAuditTrail")
	public MessageListModel<AuditTrailModel> getErrors(@QueryParam("startDate") String startDate) {
		 
		
		MessageListModel<AuditTrailModel> messageModel = new MessageListModel<AuditTrailModel>();
  

		List<AuditTrailModel> list = auditTrailDAO.list(startDate);

		messageModel.setCode(0);
		messageModel.setSuccess(true);
		messageModel.setDescription("Audit trail sucessfully listed!");
		messageModel.setList(list);


		return messageModel;

	}

}