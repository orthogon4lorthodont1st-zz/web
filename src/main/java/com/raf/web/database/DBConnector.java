package com.raf.web.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.raf.web.models.UserModel;

public class DBConnector {

	private static boolean hasFactory;
	private static SessionFactory factory;

	private static void initiate() throws FileNotFoundException, NullPointerException, IOException {
		System.setProperty("org.jboss.logging.provider", "slf4j");
		Properties props = new Properties();
		InputStream resourceAsStream = DBConnector.class.getClassLoader().getResourceAsStream("application.properties");

		props.load(resourceAsStream);

		Configuration configuration = new Configuration();
		
		

		configuration.setProperty("hibernate.hbm2ddl.auto", props.getProperty("HIBERNATE_SCHEMA_DDL"));
		configuration.setProperty("hibernate.connection.dialect", props.getProperty("HIBERNATE_DIALECT"));
		configuration.setProperty("hibernate.connection.driver_class", props.getProperty("HIBERNATE_DRIVER"));
		configuration.setProperty("hibernate.connection.url", props.getProperty("DATABASE_HOST"));
		configuration.setProperty("hibernate.connection.username", props.getProperty("DATABASE_USERNAME"));
		configuration.setProperty("hibernate.connection.password", props.getProperty("DATABASE_PASSWORD"));

		configuration.addAnnotatedClass(UserModel.class);

		factory = configuration.buildSessionFactory();
		hasFactory = true;
	}

	public static SessionFactory getFactory() throws FileNotFoundException, IOException {
		if (hasFactory) {
			return factory;
		}
		initiate();
		return factory;
	}

}
