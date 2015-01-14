package com.prokarma.app.connection.jpa;

import com.prokarma.app.provider.Provider;
import com.prokarma.app.provider.ProviderFactory;
import com.prokarma.app.provider.ProviderSpi;

public class JpaConnectionSpi implements ProviderSpi {

    public String getName() {
        return "connectionsJpa";
    }

    public Class<? extends Provider> getProviderClass() {
        return JpaConnectionProvider.class;
    }

    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return JpaConnectionProviderFactory.class;
    }

}
