package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties deviceProperties = new Properties();
    private static Properties configProperties = new Properties(); // ✅ Configuration.properties için
    private static final String CONFIG_FILE_PATH =
            System.getProperty("user.dir") + "/src/test/java/Configuration.properties"; // ✅ YAZMAK İÇİN

    static {
        // DeviceConfiguration.properties yükleme
        try {
            FileInputStream fis = new FileInputStream(
                    System.getProperty("user.dir") + "/src/test/java/DeviceConfiguration.properties");
            deviceProperties.load(fis);
        } catch (IOException e) {
            System.out.println("Error reading DeviceConfiguration.properties");
            e.printStackTrace();
        }

        // Configuration.properties yükleme
        try {
            FileInputStream fis3 = new FileInputStream(
                    System.getProperty("user.dir") + "/src/test/java/Configuration.properties");
            configProperties.load(fis3); // ✅ ZATEN VARDI
        } catch (IOException e) {
            System.out.println("Warning: Configuration.properties okunamadı!");
        }
    }

    public static String getProperty(String key) {

        if (deviceProperties.containsKey(key)) {
            return deviceProperties.getProperty(key);
        }

        if (configProperties.containsKey(key)) { // ✅ ZATEN VARDI
            return configProperties.getProperty(key);
        }

        return null;
    }

    /**
     * ✅ YENİ: Runtime'da Configuration.properties içine değer yazmak için.
     * Örn: ConfigReader.setProperty("sonIslemStanNo", "211");
     */
    public static synchronized void setProperty(String key, String value) {
        // Sadece Configuration.properties üzerinde çalışıyoruz
        configProperties.setProperty(key, value);
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
            configProperties.store(fos, "updated by automation");
        } catch (IOException e) {
            System.out.println("Error writing Configuration.properties");
            e.printStackTrace();
        }
    }

    public static class AppConfigReader {
        private static Properties appProperties = new Properties();

        static {
            try {
                FileInputStream fis = new FileInputStream(
                        System.getProperty("user.dir") + "/src/test/java/AppConfiguration.properties");
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
