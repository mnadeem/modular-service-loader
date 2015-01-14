package com.prokarma.app.provider;

public interface ProviderSpi {
	String getName();
    Class<? extends Provider> getProviderClass();
    Class<? extends ProviderFactory> getProviderFactoryClass();
}
