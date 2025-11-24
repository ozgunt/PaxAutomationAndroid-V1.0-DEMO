package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppManager {

    private static Properties properties = new Properties();
    private static Map<String, String> apps = new HashMap<>();
    private static String activeAppKey;

    static {
        try (FileInputStream fis = new FileInputStream("src/test/java/AppConfiguration.properties")) {
            properties.load(fis);

            // aktif app key
            activeAppKey = properties.getProperty("activeApp");

            // tüm app key -> path map
            for (String key : properties.stringPropertyNames()) {
                if (!key.equals("activeApp")) {
                    apps.put(key, properties.getProperty(key));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("AppConfiguration.properties okunamadı!");
        }
    }


    // 🔹 Belirli bir app key'e göre path döner
    public static String getAppPath(String appKey) {
        return apps.get(appKey);
    }

}