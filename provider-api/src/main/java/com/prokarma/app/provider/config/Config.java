package com.prokarma.app.provider.config;


public class Config {

	private static ConfigProvider configProvider = new SystemPropertiesConfigProvider();

	public static void init(ConfigProvider configProvider) {
		Config.configProvider = configProvider;
	}

	public static String getProvider(String spi) {
		String provider = configProvider.getProvider(spi);
		if (provider == null || provider.trim().equals("")) {
			return null;
		} else {
			return provider;
		}
	}

	public static Scope scope(String... scope) {
		return configProvider.scope(scope);
	}

	public static interface ConfigProvider {

		String getProvider(String spi);

		Scope scope(String... scope);
	}

	public static interface Scope {

		String get(String key);

		String get(String key, String defaultValue);

		String[] getArray(String key);

		Integer getInt(String key);

		Integer getInt(String key, Integer defaultValue);

		Long getLong(String key);

		Long getLong(String key, Long defaultValue);

		Boolean getBoolean(String key);

		Boolean getBoolean(String key, Boolean defaultValue);

		Scope scope(String... scope);
	}
}
