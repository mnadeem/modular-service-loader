package com.prokarma.app.connection.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.prokarma.app.provider.AppTransaction;


public class JpaAppTransaction implements AppTransaction {

    protected EntityManager em;

    public JpaAppTransaction(EntityManager em) {
        this.em = em;
    }

    public void begin() {
        em.getTransaction().begin();
    }

    public void commit() {
        try {
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw PersistenceExceptionConverter.convert(e.getCause() != null ? e.getCause() : e);
        }
    }

    public void rollback() {
        em.getTransaction().rollback();
    }

    public void setRollbackOnly() {
        em.getTransaction().setRollbackOnly();
    }

    public boolean getRollbackOnly() {
        return  em.getTransaction().getRollbackOnly();
    }

    public boolean isActive() {
        return em.getTransaction().isActive();
    }
}
