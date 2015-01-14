package com.prokarma.app.connection.jpa;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import org.hibernate.exception.ConstraintViolationException;

import com.prokarma.app.model.ModelDuplicateException;
import com.prokarma.app.model.ModelException;

public class PersistenceExceptionConverter implements InvocationHandler {

    private EntityManager em;

    public static EntityManager create(EntityManager em) {
        return (EntityManager) Proxy.newProxyInstance(EntityManager.class.getClassLoader(), new Class[]{EntityManager.class}, new PersistenceExceptionConverter(em));
    }

    private PersistenceExceptionConverter(EntityManager em) {
        this.em = em;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(em, args);
        } catch (InvocationTargetException e) {
            throw convert(e.getCause());
        }
    }

    public static ModelException convert(Throwable t) {
        if (t.getCause() != null && t.getCause() instanceof ConstraintViolationException) {
            throw new ModelDuplicateException(t);
        } if (t instanceof EntityExistsException) {
            throw new ModelDuplicateException(t);
        } else {
            throw new ModelException(t);
        }
    }

}
