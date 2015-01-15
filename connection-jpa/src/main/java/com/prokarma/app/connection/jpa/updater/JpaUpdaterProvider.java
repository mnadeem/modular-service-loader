package com.prokarma.app.connection.jpa.updater;

import java.sql.Connection;

import com.prokarma.app.provider.Provider;

public interface JpaUpdaterProvider extends Provider {

	public String FIRST_VERSION = "1.0.0.Final";

    public String LAST_VERSION = "1.1.0.Beta1";

    public String getCurrentVersionSql();

    public void update(Connection connection);

    public void validate(Connection connection);
}
