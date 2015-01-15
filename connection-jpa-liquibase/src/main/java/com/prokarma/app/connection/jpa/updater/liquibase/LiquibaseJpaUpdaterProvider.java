package com.prokarma.app.connection.jpa.updater.liquibase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.RanChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.logging.LogFactory;
import liquibase.logging.LogLevel;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.servicelocator.ServiceLocator;

import org.jboss.logging.Logger;

import com.prokarma.app.connection.jpa.updater.JpaUpdaterProvider;

public class LiquibaseJpaUpdaterProvider implements JpaUpdaterProvider {

    private static final Logger logger = Logger.getLogger(LiquibaseJpaUpdaterProvider.class);

    private static final String CHANGELOG = "META-INF/jpa-changelog-master.xml";

    public String getCurrentVersionSql() {
        return "SELECT ID from DATABASECHANGELOG ORDER BY DATEEXECUTED DESC LIMIT 1";
    }

    public void update(Connection connection) {
        logger.debug("Starting database update");

        try {
            Liquibase liquibase = getLiquibase(connection);

            List<ChangeSet> changeSets = liquibase.listUnrunChangeSets((Contexts) null);
            if (!changeSets.isEmpty()) {
                if (changeSets.get(0).getId().equals(FIRST_VERSION)) {
                    Statement statement = connection.createStatement();
                    try {
                        statement.executeQuery("SELECT id FROM REALM");

                        logger.infov("Updating database from {0} to {1}", FIRST_VERSION, changeSets.get(changeSets.size() - 1).getId());
                        liquibase.markNextChangeSetRan((Contexts) null);
                    } catch (SQLException e) {
                        logger.info("Initializing database schema");
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        List<RanChangeSet> ranChangeSets = liquibase.getDatabase().getRanChangeSetList();
                        logger.debugv("Updating database from {0} to {1}", ranChangeSets.get(ranChangeSets.size() - 1).getId(), changeSets.get(changeSets.size() - 1).getId());
                    } else {
                        logger.infov("Updating database");
                    }
                }

                liquibase.update((Contexts) null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update database", e);
        }
        logger.debug("Completed database update");
    }

    public void validate(Connection connection) {
        try {
            Liquibase liquibase = getLiquibase(connection);

            liquibase.validate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate database", e);
        }
    }

    private Liquibase getLiquibase(Connection connection) throws Exception {
        ServiceLocator sl = ServiceLocator.getInstance();

        if (!System.getProperties().containsKey("liquibase.scan.packages")) {
            if (sl.getPackages().remove("liquibase.core")) {
                sl.addPackageToScan("liquibase.core.xml");
            }

            if (sl.getPackages().remove("liquibase.parser")) {
                sl.addPackageToScan("liquibase.parser.core.xml");
            }

            if (sl.getPackages().remove("liquibase.serializer")) {
                sl.addPackageToScan("liquibase.serializer.core.xml");
            }

            sl.getPackages().remove("liquibase.ext");
            sl.getPackages().remove("liquibase.sdk");
        }

        LogFactory.setInstance(new LogWrapper());
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        return new Liquibase(CHANGELOG, new ClassLoaderResourceAccessor(getClass().getClassLoader()), database);
    }

    public void close() {
    }

    private static class LogWrapper extends LogFactory {

        private liquibase.logging.Logger logger = new liquibase.logging.Logger() {

            public void setName(String name) {
            }

            public void setLogLevel(String level) {
            }

            public void setLogLevel(LogLevel level) {
            }

            public void setLogLevel(String logLevel, String logFile) {
            }

            public void severe(String message) {
                LiquibaseJpaUpdaterProvider.logger.error(message);
            }

            public void severe(String message, Throwable e) {
                LiquibaseJpaUpdaterProvider.logger.error(message, e);
            }

            public void warning(String message) {
                LiquibaseJpaUpdaterProvider.logger.warn(message);
            }

            public void warning(String message, Throwable e) {
                LiquibaseJpaUpdaterProvider.logger.warn(message, e);
            }

            public void info(String message) {
                LiquibaseJpaUpdaterProvider.logger.debug(message);
            }

            public void info(String message, Throwable e) {
                LiquibaseJpaUpdaterProvider.logger.debug(message, e);
            }

            public void debug(String message) {
                LiquibaseJpaUpdaterProvider.logger.trace(message);
            }

            public LogLevel getLogLevel() {
                if (LiquibaseJpaUpdaterProvider.logger.isTraceEnabled()) {
                    return LogLevel.DEBUG;
                } else if (LiquibaseJpaUpdaterProvider.logger.isDebugEnabled()) {
                    return LogLevel.INFO;
                } else {
                    return LogLevel.WARNING;
                }
            }

            public void debug(String message, Throwable e) {
                LiquibaseJpaUpdaterProvider.logger.trace(message, e);
            }

            public void setChangeLog(DatabaseChangeLog databaseChangeLog) {
            }

            public void setChangeSet(ChangeSet changeSet) {
            }

            public int getPriority() {
                return 0;
            }
        };

        @Override
        public liquibase.logging.Logger getLog(String name) {
            return logger;
        }

        @Override
        public liquibase.logging.Logger getLog() {
            return logger;
        }

    }

}
