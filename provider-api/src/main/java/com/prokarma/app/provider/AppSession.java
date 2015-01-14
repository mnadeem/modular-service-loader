package com.prokarma.app.provider;

import java.util.Set;

public interface AppSession {

	AppTransactionManager getTransaction();
	<T extends Provider> T getProvider(Class<T> clazz);
    <T extends Provider> T getProvider(Class<T> clazz, String id);
    <T extends Provider> Set<String> listProviderIds(Class<T> clazz);
    <T extends Provider> Set<T> getAllProviders(Class<T> clazz);
    void enlistForClose(Provider provider);
    AppSessionFactory getAppSessionFactory();
    void close();
}
