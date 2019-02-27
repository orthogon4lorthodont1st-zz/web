package com.raf.web.routes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.raf.web.utils.Utils;

@Path("/auth")
public class Auth {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response auth(@Context HttpServletRequest request) throws IOException {
		Map<String, String> obj = new HashMap<String, String>();
		obj.put("SESSION_ID", (String) request.getAttribute("SESSION_ID"));
		return Response.ok(obj).status(Status.ACCEPTED).build();
	}
}
