package com.raf.web.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBConnector {

	private static boolean hasFactory;
	private static SessionFactory factory;

	private static void initiate() throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(DBConnector.class.getClassLoader().getResourceAsStream("application.properties"));
	
		Configuration configuration = new Configuration();
		
		configuration.configure("hibernate.cfg.xml");
		configuration.setProperty("hibernate.connection.url", props.getProperty("DATABASE_HOST"));
		configuration.setProperty("hibernate.connection.username", props.getProperty("DATABASE_USERNAME"));
		configuration.setProperty("hibernate.connection.password", props.getProperty("DATABASE_PASSWORD"));
		
		configuration.addAnnotatedClass(com.raf.web.models.UserModel.class); 
		
	
		System.out.println("host" + configuration.getProperty("hibernate.connection.url"));

		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();

		factory = configuration.buildSessionFactory(ssr);
		hasFactory = true;
	}

	public static SessionFactory getFactory() throws FileNotFoundException, IOException {
		try {
			if (hasFactory) {
				return factory;
			}
			initiate();
			return factory;

		} catch (Exception e) {
			System.out.println("exception: " + e.getMessage());
			System.out.println("exception: " + e.toString());
		}

		return factory;
	}

}
