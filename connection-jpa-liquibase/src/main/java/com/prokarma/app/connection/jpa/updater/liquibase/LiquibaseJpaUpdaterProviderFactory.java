package com.prokarma.app.connection.jpa.updater.liquibase;

import com.prokarma.app.connection.jpa.updater.JpaUpdaterProvider;
import com.prokarma.app.connection.jpa.updater.JpaUpdaterProviderFactory;
import com.prokarma.app.provider.AppSession;
import com.prokarma.app.provider.config.Config;


public class LiquibaseJpaUpdaterProviderFactory implements JpaUpdaterProviderFactory {

    public JpaUpdaterProvider create(AppSession session) {
        return new LiquibaseJpaUpdaterProvider();
    }

    public void init(Config.Scope config) {
    }

    public void close() {
    }

    public String getId() {
        return "liquibase";
    }
}
