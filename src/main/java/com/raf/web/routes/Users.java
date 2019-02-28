package com.raf.web.routes;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.raf.web.errors.Errors;
import com.raf.web.models.UserModel;
import com.raf.web.services.UserService;


@Path("/users")
public class Users {

	static String dbPassword = (String) System.getenv("DATABASE_PASSWORD");
	static String dbHost = (String) System.getenv("DATABASE_HOST");
	static String dbUsername = (String) System.getenv("DATABASE_USERNAME");

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response read() {
		System.out.println("db Host: " + dbHost);
		try {
			Connection conn = DriverManager.getConnection(dbHost, dbUsername, dbPassword);

			Statement statement = conn.createStatement();
			statement.execute("SELECT * FROM users");
			ResultSet results = statement.getResultSet();

			Map<String, String> obj = new HashMap<String, String>();

			while (results.next()) {
				obj.put(results.getString("name"), results.getString("dateOfBirth"));
			}

			return Response.ok(obj).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok().entity("No users found").build();

		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response create(@Context HttpServletRequest request, UserModel user) {
		try {
			ArrayList<Map<String, String>> errors = Errors.validate(user);
			
			if(!errors.isEmpty()) {
				return Response.ok(errors).build();
			}

			String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
			user.setPassword(passwordHash);

			UserService.create(user);

			return Response.ok(user).build();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.status(Status.BAD_REQUEST).build();
	}
}