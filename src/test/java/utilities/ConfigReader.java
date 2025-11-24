package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties deviceProperties = new Properties();
    private static Properties configProperties = new Properties(); // ✅ EKLENDİ

    static {
        try {
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/DeviceConfiguration.properties");
            deviceProperties.load(fis);
        } catch (IOException e) {
            System.out.println("Error reading DeviceConfiguration.properties");
            e.printStackTrace();
        }

        try {
            FileInputStream fis3 = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/Configuration.properties");
            configProperties.load(fis3); // ✅ EKLENDİ
        } catch (IOException e) {
            System.out.println("Warning: Configuration.properties okunamadı!");
        }
    }

    public static String getProperty(String key) {

        if (deviceProperties.containsKey(key)) {
            return deviceProperties.getProperty(key);
        }

        if (configProperties.containsKey(key)) { // ✅ EKLENDİ
            return configProperties.getProperty(key);
        }

        return null;
    }

    public static class AppConfigReader {
        private static Properties appProperties = new Properties();

        static {
            try {
                FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/AppConfiguration.properties");
                appProperties.load(fis);
            } catch (IOException e) {
                System.out.println("Error reading AppConfiguration.properties");
                e.printStackTrace();
            }
        }

        public static String getAppProperty(String key) {
            return appProperties.getProperty(key);
        }
    }
}
