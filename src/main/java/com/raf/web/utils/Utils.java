package com.raf.web.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class Utils {

	static String dbPassword = (String) System.getenv("DATABASE_PASSWORD");
	static String dbHost = (String) System.getenv("DATABASE_HOST");
	static String dbUsername = (String) System.getenv("DATABASE_USERNAME");

	public static boolean isAuthorized(HttpServletRequest request) {
		try {
			String encodedCreds = request.getHeader(HttpHeaders.AUTHORIZATION);
			
			if(encodedCreds == null) {
				return false;
			}
			
			encodedCreds = encodedCreds.split(" ")[1];
			

			final String decodedCreds = new String(Base64.getDecoder().decode(encodedCreds.getBytes()));

			if (decodedCreds == null || decodedCreds.split(":").length < 2) {
				return false;
			}

			final String name = decodedCreds.split(":")[0];
			final String password = decodedCreds.split(":")[1];

			Connection conn = DriverManager.getConnection(dbHost, dbUsername, dbPassword);

			Statement statement = conn.createStatement();
			statement.execute(String.format("SELECT * FROM users WHERE name = '%s' LIMIT 1", name));

			ResultSet results = statement.getResultSet();

			if (results.next()) {
				BCrypt.checkpw(password, results.getString("passwordHash"));
				return true;

			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

}
