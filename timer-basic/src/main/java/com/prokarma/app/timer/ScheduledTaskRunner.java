package com.prokarma.app.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prokarma.app.provider.AppSession;
import com.prokarma.app.provider.AppSessionFactory;


public class ScheduledTaskRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskRunner.class);

    private final AppSessionFactory sessionFactory;
    private final ScheduledTask task;

    public ScheduledTaskRunner(AppSessionFactory sessionFactory, ScheduledTask task) {
        this.sessionFactory = sessionFactory;
        this.task = task;
    }

    public void run() {
    	AppSession session = sessionFactory.create();
        try {
        	session.getTransaction().begin();
            task.run(session);
            session.getTransaction().commit();
            logger.debug("Executed scheduled task ", task.getClass().getSimpleName());
        } catch (Throwable t) {
            logger.error("Failed to run scheduled task ", task.getClass().getSimpleName(), t);
            session.getTransaction().rollback();
        } finally {
            try {
                session.close();
            } catch (Throwable t) {
                logger.error("Failed to close ProviderSession", t);
            }
        }
    }
}