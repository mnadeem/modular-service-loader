package com.prokarma.app;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prokarma.app.provider.AppSession;
import com.prokarma.app.provider.AppSessionFactory;
import com.prokarma.app.provider.DefaultAppSessionFactory;
import com.prokarma.app.provider.Provider;
import com.prokarma.app.provider.config.Config;
import com.prokarma.app.provider.config.JsonConfigProvider;
import com.prokarma.app.timer.ScheduledTask;
import com.prokarma.app.timer.ScheduledTaskRunner;
import com.prokarma.app.timer.TimerProvider;

public class ConsoleApplication {

	private static Logger logger = LoggerFactory.getLogger(ConsoleApplication.class);

	public static void main(String[] args) {
		loadConfig();
		DefaultAppSessionFactory sessionFactory = createSessionFactory();
		AppSession appSession = sessionFactory.create();

		for (Provider provider : appSession.getAllProviders(TimerProvider.class)) {
			logger.info(provider.getClass().getName());
		}

		setupScheduledTasks(sessionFactory);
	}

	private static DefaultAppSessionFactory createSessionFactory() {
		DefaultAppSessionFactory factory = new DefaultAppSessionFactory();
        factory.init();
        return factory;
	}

	public static void loadConfig() {
        try {
            URL configUrl = getConfig();

            if (configUrl != null) {
                JsonNode node = new ObjectMapper().readTree(configUrl);

                Properties properties = new Properties();
                properties.putAll(System.getProperties());
                for(Map.Entry<String, String> e : System.getenv().entrySet()) {
                    properties.put("env." + e.getKey(), e.getValue());
                }

                Config.init(new JsonConfigProvider(node, properties));

                logger.info("Loaded config from " + configUrl);
                return;
            } else {
            	logger.warn("Config 'config.json' not found");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

	private static URL getConfig() throws MalformedURLException {
		URL config = null;

		String configDir = System.getProperty("config.dir");
		if (configDir != null) {
		    File f = new File(configDir + File.separator + "config.json");
		    if (f.isFile()) {
		        config = f.toURI().toURL();
		    }
		}

		if (config == null) {
		    config = Thread.currentThread().getContextClassLoader().getResource("META-INF/config.json");
		}
		return config;
	}

	private static void setupScheduledTasks(final AppSessionFactory sessionFactory) {
        long interval = Config.scope("scheduled").getLong("interval", 60L) * 1000;
        
        logger.info("interval {}", interval);

        TimerProvider timer = sessionFactory.create().getProvider(TimerProvider.class);
        timer.schedule(new ScheduledTaskRunner(sessionFactory, new ScheduledTask() {
			
			public void run(AppSession session) {
				logger.info("Task Ran");				
			}
		}), interval, "Sample scheduled task");

    }
}
