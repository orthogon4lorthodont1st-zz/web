package com.raf.web.config;

import com.raf.web.filter.AuthFilter;
import com.raf.web.routes.Auth;
import com.raf.web.routes.Users;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(Users.class);
		register(Auth.class);
		register(AuthFilter.class);
	}
}