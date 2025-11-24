package utilities;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final Logger logger = LoggerUtil.getLogger();

    public static void captureAndAttach(WebDriver driver, Scenario scenario) {
        try {
            if (!(driver instanceof TakesScreenshot)) {
                logger.warn("Driver TakesScreenshot değil, ekran görüntüsü alınamadı.");
                return;
            }

            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(bytes, "image/png", "Screenshot");

            String ts = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date());
            String safeScenario = scenario.getName().replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            File dir = new File("target/screenshots");
            if (!dir.exists()) Files.createDirectories(dir.toPath());
            File out = new File(dir, safeScenario + "_" + ts + ".png");

            try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(bytes);
            }

            logger.info("Screenshot kaydedildi: {}", out.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Screenshot alınırken hata: {}", e.getMessage(), e);
        }
    }
}
