package com.prokarma.app.connection.jpa;

import javax.persistence.EntityManager;

public class DefaultJpaConnectionProvider implements JpaConnectionProvider {

    private final EntityManager em;

    public DefaultJpaConnectionProvider(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void close() {
        em.close();
    }
}
