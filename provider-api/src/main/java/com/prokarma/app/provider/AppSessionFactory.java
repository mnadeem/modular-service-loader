package com.prokarma.app.provider;

import java.util.List;

public interface AppSessionFactory {

	AppSession create();
	<T extends Provider> ProviderFactory<T> getProviderFactory(Class<T> clazz);
	<T extends Provider> ProviderFactory<T> getProviderFactory(Class<T> clazz, String id);
	List<ProviderFactory> getProviderFactories(Class<? extends Provider> clazz);
	void close();
}
