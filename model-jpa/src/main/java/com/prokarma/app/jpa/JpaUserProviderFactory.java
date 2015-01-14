package com.prokarma.app.jpa;

import javax.persistence.EntityManager;

import com.prokarma.app.connection.jpa.JpaConnectionProvider;
import com.prokarma.app.model.UserProvider;
import com.prokarma.app.model.UserProviderFactory;
import com.prokarma.app.provider.AppSession;
import com.prokarma.app.provider.config.Config;

public class JpaUserProviderFactory implements UserProviderFactory {

	public void init(Config.Scope config) {
	}

	public String getId() {
		return "jpa";
	}

	public void close() {
	}

	public UserProvider create(AppSession session) {
		EntityManager em = session.getProvider(JpaConnectionProvider.class).getEntityManager();
		return new JpaUserProvider(session, em);
	}

}
