package com.raf.web.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.raf.web.models.UserModel;

import com.raf.web.database.DBConnector;

public class UserService {

	public static UserModel create(UserModel user) {
		Transaction tx = null;
		Session dbSession = null;
		try {
			SessionFactory factory = DBConnector.getFactory();

			dbSession = factory.openSession();
			tx = dbSession.beginTransaction();
			dbSession.save(user);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();

		} finally {
			if (dbSession != null) {
				dbSession.close();
			}
		}

		return user;
	}

}
