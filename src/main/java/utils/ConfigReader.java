package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Warning: config.properties not found in resources. Using default properties.");
            }
        } catch (Exception e) {
            System.err.println("Error reading config.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getAppiumServerUrl() {
        return getProperty("appium.server.url", "http://127.0.0.1:4723");
    }

    public static String getPlatformName() {
        return getProperty("platform.name", "Android");
    }

    public static String getAutomationName() {
        return getProperty("automation.name", "UiAutomator2");
    }

    public static String getDeviceName() {
        return getProperty("device.name", "Android Emulator");
    }

    public static String getAppPath() {
        return getProperty("app.path", "app/SwagLabs.apk");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "15"));
    }
}
