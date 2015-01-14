package com.prokarma.app.provider.config;

import java.util.Properties;

import org.codehaus.jackson.JsonNode;

import com.prokarma.app.provider.util.StringPropertyReplacer;

public class JsonConfigProvider implements Config.ConfigProvider {

    private Properties properties;

    private JsonNode config;

    public JsonConfigProvider(JsonNode config, Properties properties) {
        this.config = config;
        this.properties = properties;
    }

    public String getProvider(String spi) {
        JsonNode n = getNode(config, spi, "provider");
        return n != null ? replaceProperties(n.getTextValue()) : null;
    }

    public Config.Scope scope(String... path) {
        return new JsonScope(getNode(config, path));
    }

    private static JsonNode getNode(JsonNode root, String... path) {
        if (root == null) {
            return null;
        }
        JsonNode n = root;
        for (String p : path) {
            n = n.get(p);
            if (n == null) {
                return null;
            }
        }
        return n;
    }

    private String replaceProperties(String value) {
        return StringPropertyReplacer.replaceProperties(value, properties);
    }

    public class JsonScope implements Config.Scope {

        private JsonNode config;

        public JsonScope(JsonNode config) {
            this.config = config;
        }

        public String get(String key) {
            return get(key, null);
        }

        public String get(String key, String defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            return replaceProperties(n.getTextValue());
        }

        public String[] getArray(String key) {
            if (config == null) {
                return null;
            }

            JsonNode n = config.get(key);
            if (n == null) {
                return null;
            } else if (n.isArray()) {
                String[] a = new String[n.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = replaceProperties(n.get(i).getTextValue());
                }
                return a;
            } else {
               return new String[] { replaceProperties(n.getTextValue()) };
            }
        }

        public Integer getInt(String key) {
            return getInt(key, null);
        }

        public Integer getInt(String key, Integer defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            if (n.isTextual()) {
                return Integer.parseInt(replaceProperties(n.getTextValue()));
            } else {
                return n.getIntValue();
            }
        }

        public Long getLong(String key) {
            return getLong(key, null);
        }

        public Long getLong(String key, Long defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            if (n.isTextual()) {
                return Long.parseLong(replaceProperties(n.getTextValue()));
            } else {
                return n.getLongValue();
            }
        }

        public Boolean getBoolean(String key) {
            return getBoolean(key, null);
        }

        public Boolean getBoolean(String key, Boolean defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            if (n.isTextual()) {
                return Boolean.parseBoolean(replaceProperties(n.getTextValue()));
            } else {
                return n.getBooleanValue();
            }
        }

        public Config.Scope scope(String... path) {
            return new JsonScope(getNode(config, path));
        }

    }

}

