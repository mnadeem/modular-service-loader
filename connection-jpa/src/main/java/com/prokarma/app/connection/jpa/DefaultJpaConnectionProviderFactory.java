package com.prokarma.app.connection.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.ejb.AvailableSettings;
import org.jboss.logging.Logger;

import com.prokarma.app.provider.AppSession;
import com.prokarma.app.provider.config.Config;


public class DefaultJpaConnectionProviderFactory implements JpaConnectionProviderFactory {

    private static final Logger logger = Logger.getLogger(DefaultJpaConnectionProviderFactory.class);

    private volatile EntityManagerFactory emf;

    private Config.Scope config;

    public JpaConnectionProvider create(AppSession session) {
        lazyInit(session);

        EntityManager em = emf.createEntityManager();
        em = PersistenceExceptionConverter.create(em);
        session.getTransaction().enlist(new JpaAppTransaction(em));
        return new DefaultJpaConnectionProvider(em);
    }

    public void close() {
        if (emf != null) {
            emf.close();
        }
    }

    public String getId() {
        return "default";
    }

    public void init(Config.Scope config) {
        this.config = config;
    }

    private void lazyInit(AppSession session) {
        if (emf == null) {
            synchronized (this) {
                if (emf == null) {
                    logger.debug("Initializing JPA connections");

                    String unitName = config.get("unitName");
                    String databaseSchema = config.get("databaseSchema");

                    Map<String, Object> properties = new HashMap<String, Object>();

                    // Only load config from keycloak-server.json if unitName is not specified
                    if (unitName == null) {
                        unitName = "keycloak-default";

                        String dataSource = config.get("dataSource");
                        if (dataSource != null) {
                            if (config.getBoolean("jta", false)) {
                                properties.put(AvailableSettings.JTA_DATASOURCE, dataSource);
                            } else {
                                properties.put(AvailableSettings.NON_JTA_DATASOURCE, dataSource);
                            }
                        } else {
                            properties.put(AvailableSettings.JDBC_URL, config.get("url"));
                            properties.put(AvailableSettings.JDBC_DRIVER, config.get("driver"));

                            String user = config.get("user");
                            if (user != null) {
                                properties.put(AvailableSettings.JDBC_USER, user);
                            }
                            String password = config.get("password");
                            if (password != null) {
                                properties.put(AvailableSettings.JDBC_PASSWORD, password);
                            }
                        }

                        String driverDialect = config.get("driverDialect");
                        if (driverDialect != null && driverDialect.length() > 0) {
                            properties.put("hibernate.dialect", driverDialect);
                        }

                        if (databaseSchema != null) {
                            if (databaseSchema.equals("development-update")) {
                                properties.put("hibernate.hbm2ddl.auto", "update");
                                databaseSchema = null;
                            } else if (databaseSchema.equals("development-validate")) {
                                properties.put("hibernate.hbm2ddl.auto", "validate");
                                databaseSchema = null;
                            }
                        }

                        properties.put("hibernate.show_sql", config.getBoolean("showSql", false));
                        properties.put("hibernate.format_sql", config.getBoolean("formatSql", true));
                    }

                    logger.trace("Creating EntityManagerFactory");
                    emf = Persistence.createEntityManagerFactory(unitName, properties);
                    logger.trace("EntityManagerFactory created");
                    
                }
            }
        }
    }
}
