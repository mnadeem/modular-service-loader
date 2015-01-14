package com.prokarma.app.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultAppSession implements AppSession {

	private final DefaultAppSessionFactory factory;
	private final Map<Integer, Provider> providers = new HashMap<Integer, Provider>();
	private final List<Provider> closable = new LinkedList<Provider>();
	private final DefaultAppTransactionManager transactionManager;

	public DefaultAppSession(DefaultAppSessionFactory defaultAppSessionFactory) {
		this.factory = defaultAppSessionFactory;
		this.transactionManager = new DefaultAppTransactionManager();
	}

	public <T extends Provider> T getProvider(Class<T> clazz) {
		Integer hash = clazz.hashCode();
		T provider = (T) providers.get(hash);
		if (provider == null) {
			ProviderFactory<T> providerFactory = factory.getProviderFactory(clazz);
			if (providerFactory != null) {
				provider = providerFactory.create(this);
				providers.put(hash, provider);
			}
		}
		return provider;
	}

	public <T extends Provider> T getProvider(Class<T> clazz, String id) {
		Integer hash = clazz.hashCode() + id.hashCode();
		T provider = (T) providers.get(hash);
		if (provider == null) {
			ProviderFactory<T> providerFactory = factory.getProviderFactory(clazz, id);
			if (providerFactory != null) {
				provider = providerFactory.create(this);
				providers.put(hash, provider);
			}
		}
		return provider;
	}

	public <T extends Provider> Set<String> listProviderIds(Class<T> clazz) {
		return factory.getAllProviderIds(clazz);
	}

	public <T extends Provider> Set<T> getAllProviders(Class<T> clazz) {
		Set<T> providers = new HashSet<T>();
		for (String id : listProviderIds(clazz)) {
			providers.add(getProvider(clazz, id));
		}
		return providers;
	}

	public void enlistForClose(Provider provider) {
		closable.add(provider);
	}

	public AppSessionFactory getAppSessionFactory() {
		return this.factory;
	}

	public void close() {
		for (Provider p : providers.values()) {
			try {
				p.close();
			} catch (Exception e) {
			}
		}
		for (Provider p : closable) {
			try {
				p.close();
			} catch (Exception e) {
			}
		}
	}

	public AppTransactionManager getTransaction() {
		return this.transactionManager;
	}
}
