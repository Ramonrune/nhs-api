package com.healthsystem.filter;


import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.healthsystem.model.MessageModel;
import com.healthsystem.util.Criptography;

import io.jsonwebtoken.Jwts;


@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

	/* (non-Javadoc)
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		MessageModel messageModel = new MessageModel();
		messageModel.setCode(-1000);
		messageModel.setSuccess(false);
		messageModel.setDescription("Token invalid");
		Response resp = Response.status(Response.Status.UNAUTHORIZED).entity(messageModel).build();
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			requestContext.abortWith(resp);
		} else {
			try {
				String token = authorizationHeader.substring("Bearer".length()).trim();
				System.out.println("token valee " + token);
				
				Key key =  Criptography.generateKey();
				Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			} catch (Exception e) {
				requestContext.abortWith(resp);
			}

		}
	}
}