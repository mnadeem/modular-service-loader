package com.prokarma.app.jpa;

import javax.persistence.EntityManager;

import com.prokarma.app.model.UserModel;
import com.prokarma.app.model.UserProvider;
import com.prokarma.app.provider.AppSession;


public class JpaUserProvider implements UserProvider {

    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";

    private final AppSession session;
    protected EntityManager em;

    public JpaUserProvider(AppSession session, EntityManager em) {
        this.session = session;
        this.em = em;
    }

	public UserModel addUser(UserModel user) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeUser(UserModel user) {
		// TODO Auto-generated method stub
		return false;
	}

	public UserModel getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}
}
