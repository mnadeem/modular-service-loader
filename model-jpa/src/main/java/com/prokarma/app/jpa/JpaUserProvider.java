package com.prokarma.app.jpa;

import javax.persistence.EntityManager;

import com.prokarma.app.jpa.entity.UserEntity;
import com.prokarma.app.model.UserModel;
import com.prokarma.app.model.UserProvider;
import com.prokarma.app.provider.AppSession;


public class JpaUserProvider implements UserProvider {

    protected EntityManager em;

    public JpaUserProvider(AppSession session, EntityManager em) {
        this.em = em;
    }

	public UserModel addUser(UserModel user) {
		UserEntity entity = new UserEntity();
		entity.setUsername(user.getUsername());
		entity.setFirstName(user.getFirstName());
		entity.setLastName(user.getLastName());
		entity.setEmail(user.getEmail());
		entity.setEnabled(user.isEnabled());
		em.persist(entity);
        em.flush();
		return user;
	}

	public boolean removeUser(UserModel user) {
		UserEntity userEntity = em.find(UserEntity.class, user.getUsername());
        if (userEntity == null) return false;
        em.remove(user);
		return true;
	}

	public UserModel getUserByUsername(String username) {
		return null;
	}

	public void close() {
		
	}
}
