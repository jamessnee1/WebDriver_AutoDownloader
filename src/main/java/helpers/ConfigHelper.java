package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {
    private static ConfigHelper instance;
    private Properties properties = new Properties();

    // Constructor to load properties from the config file
    public ConfigHelper() {
        System.out.println("Loading configuration file...");
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalArgumentException("Unable to find config.properties in the classpath.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration properties", e);
        }
    }

    // Static method to provide access to the singleton instance
    public static synchronized ConfigHelper getInstance() {
        if (instance == null) {
            instance = new ConfigHelper();
        }
        return instance;
    }

    // Get the property value for the given key
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Optionally, provide a method to get the property with a default value
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
