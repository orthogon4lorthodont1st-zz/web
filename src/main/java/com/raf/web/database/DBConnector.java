package com.raf.web.database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DBConnector {

	private static boolean hasFactory;
	private static SessionFactory factory;

	private static void initiate() {
		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
		Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();
		factory = meta.getSessionFactoryBuilder().build();
		hasFactory = true;
	}

	public static SessionFactory getFactory() {
		if (hasFactory) {
			return factory;
		}
		
		initiate();
		return factory;

	}

}
