package com.prokarma.app.connection.jpa;

import javax.persistence.EntityManager;

import com.prokarma.app.provider.Provider;

public interface JpaConnectionProvider extends Provider {

    EntityManager getEntityManager();
}

