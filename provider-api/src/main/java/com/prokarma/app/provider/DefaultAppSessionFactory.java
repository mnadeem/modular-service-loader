package com.prokarma.app.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prokarma.app.provider.config.Config;

public class DefaultAppSessionFactory implements AppSessionFactory {

	private static Logger logger = LoggerFactory.getLogger(DefaultAppSessionFactory.class);

	private Map<Class<? extends Provider>, String> provider = new HashMap<Class<? extends Provider>, String>();
	private Map<Class<? extends Provider>, Map<String, ProviderFactory>> factoriesMap = new HashMap<Class<? extends Provider>, Map<String, ProviderFactory>>();

	public void init() {
		for (ProviderSpi spi : ServiceLoader.load(ProviderSpi.class)) {
			Map<String, ProviderFactory> factories = new HashMap<String, ProviderFactory>();
			factoriesMap.put(spi.getProviderClass(), factories);

			String provider = Config.getProvider(spi.getName());
			if (provider != null) {
				loadConfiguredProvider(spi, provider, factories);
			} else {
				loadUnconfiguredProvider(spi, factories);
			}
		}
	}

	private void loadUnconfiguredProvider(ProviderSpi spi, Map<String, ProviderFactory> factories) {
		for (ProviderFactory factory : ServiceLoader.load(spi.getProviderFactoryClass())) {
			Config.Scope scope = Config.scope(spi.getName(), factory.getId());
			factory.init(scope);

			factories.put(factory.getId(), factory);
		}

		if (factories.size() == 1) {
			String provider = factories.values().iterator().next().getId();
			this.provider.put(spi.getProviderClass(), provider);

			logger.debug("Loaded SPI {}  (provider = {})", spi.getName(), provider);
		} else {
			logger.debug("Loaded SPI {} (providers = {})", spi.getName(), factories.keySet());
		}
	}

	private void loadConfiguredProvider(ProviderSpi spi, String provider, Map<String, ProviderFactory> factories) {
		this.provider.put(spi.getProviderClass(), provider);

		ProviderFactory factory = loadProviderFactory(spi, provider);
		Config.Scope scope = Config.scope(spi.getName(), provider);
		factory.init(scope);

		factories.put(factory.getId(), factory);

		logger.debug("Loaded SPI {} (provider = {})", spi.getName(), provider);
	}

	private ProviderFactory loadProviderFactory(ProviderSpi spi, String id) {
		for (ProviderFactory factory : ServiceLoader.load(spi.getProviderFactoryClass())) {
			if (factory.getId().equals(id)){
				return factory;
			}
		}
		throw new RuntimeException("Failed to find provider " + id + " for " + spi.getName());
	}

	public AppSession create() {
		return new DefaultAppSession(this);
	}

	public <T extends Provider> ProviderFactory<T> getProviderFactory(Class<T> clazz) {
		return getProviderFactory(clazz, provider.get(clazz));
	}

	public <T extends Provider> ProviderFactory<T> getProviderFactory(Class<T> clazz, String id) {
		return factoriesMap.get(clazz).get(id);
	}

	public List<ProviderFactory> getProviderFactories(Class<? extends Provider> clazz) {
		List<ProviderFactory> list = new LinkedList<ProviderFactory>();
		if (factoriesMap == null) return list;
		Map<String, ProviderFactory> providerFactoryMap = factoriesMap.get(clazz);
		if (providerFactoryMap == null) return list;
		list.addAll(providerFactoryMap.values());
		return list;
	}

	public void close() {
		for (Map<String, ProviderFactory> factories : factoriesMap.values()) {
			for (ProviderFactory factory : factories.values()) {
				factory.close();
			}
		}
	}

	<T extends Provider> Set<String> getAllProviderIds(Class<T> clazz) {
		Set<String> ids = new HashSet<String>();
		for (ProviderFactory f : factoriesMap.get(clazz).values()) {
			ids.add(f.getId());
		}
		return ids;
	}
	<T extends Provider> String getDefaultProvider(Class<T> clazz) {
		return provider.get(clazz);
	}
}
