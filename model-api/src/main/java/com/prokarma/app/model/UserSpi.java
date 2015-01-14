package com.prokarma.app.model;

import com.prokarma.app.provider.Provider;
import com.prokarma.app.provider.ProviderFactory;
import com.prokarma.app.provider.ProviderSpi;

public class UserSpi implements ProviderSpi {

    public String getName() {
        return "user";
    }

    public Class<? extends Provider> getProviderClass() {
        return UserProvider.class;
    }

    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return UserProviderFactory.class;
    }
}
