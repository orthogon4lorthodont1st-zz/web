package com.raf.web.filter;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.raf.web.utils.Utils;

@Provider
public class AuthFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest request;

	@Context
	ResourceInfo resourceInfo;

	public void filter(ContainerRequestContext req) throws IOException {
		Method method = resourceInfo.getResourceMethod();

		if (method.isAnnotationPresent(PermitAll.class)) {
			return;
		}
		
		HttpSession session = request.getSession(false);

		if (session == null) {
			String auth = req.getHeaderString(HttpHeaders.AUTHORIZATION);

			if (auth == null || auth.isEmpty()) {
				req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource")
						.header("WWW-Authenticate", "Basic").build());
				return;
			}

			boolean isAuthed = Utils.isAuthorized(request);

			if (auth != null && isAuthed) {
				session = request.getSession(true);
				request.setAttribute("SESSION_ID", session.getId());
				return;
			}

			if (auth != null && !isAuthed) {
				req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials.").build());
				return;
			}
		}

		if (session != null) {
			String sessionId = session.getId();
			String sessionHeader = req.getHeaderString("SESSION_ID");

			if (sessionHeader == null || sessionHeader.isEmpty()) {
				req.abortWith(
						Response.status(Response.Status.UNAUTHORIZED).entity("Missing header SESSION_ID").build());
				return;
			}

			if (req.getHeaderString("SESSION_ID").equals(sessionId)) {
				return;
			} else {
				req.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Mismatching session ids").build());
				return;
			}
		}
	}

}
