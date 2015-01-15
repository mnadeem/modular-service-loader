package com.prokarma.app.connection.jpa.updater;

import com.prokarma.app.provider.Provider;
import com.prokarma.app.provider.ProviderFactory;
import com.prokarma.app.provider.ProviderSpi;

public class JpaUpdaterSpi implements ProviderSpi {

    public String getName() {
        return "connectionsJpaUpdater";
    }

    public Class<? extends Provider> getProviderClass() {
        return JpaUpdaterProvider.class;
    }

    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return JpaUpdaterProviderFactory.class;
    }

}
