package utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigReader {

    private static Properties deviceProperties = new Properties();
    private static Properties configProperties = new Properties(); // Configuration.properties

    private static final String CONFIG_FILE_PATH =
            System.getProperty("user.dir") + "/src/test/java/Configuration.properties";

    static {
        // DeviceConfiguration.properties
        try (FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/test/java/DeviceConfiguration.properties")) {
            deviceProperties.load(fis);
        } catch (IOException e) {
            System.out.println("Error reading DeviceConfiguration.properties");
            e.printStackTrace();
        }

        // Configuration.properties
        try (FileInputStream fis3 = new FileInputStream(CONFIG_FILE_PATH)) {
            configProperties.load(fis3);
        } catch (IOException e) {
            System.out.println("Warning: Configuration.properties okunamadı!");
        }
    }

    public static String getProperty(String key) {
        if (deviceProperties.containsKey(key)) {
            return deviceProperties.getProperty(key);
        }
        if (configProperties.containsKey(key)) {
            return configProperties.getProperty(key);
        }
        return null;
    }

    /**
     * Runtime'da Configuration.properties içine değer yazmak için.
     * Örn: ConfigReader.setProperty("sonIslemStanNo", "211");
     *
     * NOT: Properties.store() dosyayı baştan yazıp sıralamayı bozduğu için
     * bu metot satır üstünde sadece ilgili key'in değerini değiştirir.
     */
    public static synchronized void setProperty(String key, String value) {
        if (key == null || key.trim().isEmpty()) return;
        if (value == null) value = "";

        // memory'de de güncel kalsın
        configProperties.setProperty(key, value);

        Path filePath = Paths.get(CONFIG_FILE_PATH);
        if (!Files.exists(filePath)) {
            // dosya yoksa eski davranışla oluştur
            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
                configProperties.store(fos, "updated by automation");
            } catch (IOException e) {
                System.out.println("Error writing Configuration.properties");
                e.printStackTrace();
            }
            return;
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<String> out = new ArrayList<>(lines.size());

            // key=  veya key:  formatlarını yakala, sadece value kısmını değiştir
            Pattern p = Pattern.compile("^([ \\t]*" + Pattern.quote(key) + "[ \\t]*)([=:])([ \\t]*).*$");

            boolean replaced = false;
            String escapedValue = escapePropertyValue(value);

            for (String line : lines) {
                Matcher m = p.matcher(line);
                if (!replaced && m.matches()) {
                    // prefix + delimiter + spacesAfter + newValue
                    out.add(m.group(1) + m.group(2) + m.group(3) + escapedValue);
                    replaced = true;
                } else {
                    out.add(line);
                }
            }

            // key yoksa en sona ekle (normalde sen zaten yer ayırıyorsun)
            if (!replaced) {
                out.add(key + "=" + escapedValue);
            }

            Files.write(filePath, out, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

        } catch (IOException e) {
            System.out.println("Error writing Configuration.properties");
            e.printStackTrace();
        }
    }

    // Properties formatına minimum güvenli escape:
    // - \  -> \\
    // - \n \r \t -> \\n \\r \\t
    // - value başında " " ":" "=" "#" "!" varsa kaçır
    private static String escapePropertyValue(String s) {
        if (s == null) return "";

        StringBuilder out = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default: out.append(c);
            }
        }

        // leading özel karakterler
        if (out.length() > 0) {
            char first = out.charAt(0);
            if (first == ' ' || first == ':' || first == '=' || first == '#' || first == '!') {
                out.insert(0, '\\');
            }
        }

        return out.toString();
    }

    public static class AppConfigReader {
        private static Properties appProperties = new Properties();

        static {
            try (FileInputStream fis = new FileInputStream(
                    System.getProperty("user.dir") + "/src/test/java/AppConfiguration.properties")) {
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
