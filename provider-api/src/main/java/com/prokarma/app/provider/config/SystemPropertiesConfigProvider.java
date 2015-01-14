package com.prokarma.app.provider.config;

import com.prokarma.app.provider.config.Config.ConfigProvider;
import com.prokarma.app.provider.config.Config.Scope;

public class SystemPropertiesConfigProvider implements ConfigProvider {

    public String getProvider(String spi) {
        return System.getProperties().getProperty("app." + spi + ".provider");
    }

    public Scope scope(String... scope) {
        StringBuilder sb = new StringBuilder();
        sb.append("app.");
        for (String s : scope) {
            sb.append(s);
            sb.append(".");
        }
        return new SystemPropertiesScope(sb.toString());
    }
    
    public static class SystemPropertiesScope implements Scope {

        private String prefix;

        public SystemPropertiesScope(String prefix) {
            this.prefix = prefix;
        }

        public String get(String key) {
            return get(key, null);
        }

        public String get(String key, String defaultValue) {
            return System.getProperty(prefix + key, defaultValue);
        }

        public String[] getArray(String key) {
            String value = get(key);
            if (value != null) {
                String[] a = value.split(",");
                for (int i = 0; i < a.length; i++) {
                    a[i] = a[i].trim();
                }
                return a;
            } else {
                return null;
            }
        }

        public Integer getInt(String key) {
            return getInt(key, null);
        }

        public Integer getInt(String key, Integer defaultValue) {
            String v = get(key, null);
            return v != null ? Integer.parseInt(v) : defaultValue;
        }

        public Long getLong(String key) {
            return getLong(key, null);
        }

        public Long getLong(String key, Long defaultValue) {
            String v = get(key, null);
            return v != null ? Long.parseLong(v) : defaultValue;
        }

        public Boolean getBoolean(String key) {
            return getBoolean(key, null);
        }

        public Boolean getBoolean(String key, Boolean defaultValue) {
            String v = get(key, null);
            return v != null ? Boolean.parseBoolean(v) : defaultValue;
        }

        public Scope scope(String... scope) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix + ".");
            for (String s : scope) {
                sb.append(s);
                sb.append(".");
            }
            return new SystemPropertiesScope(sb.toString());
        }
    }
}

