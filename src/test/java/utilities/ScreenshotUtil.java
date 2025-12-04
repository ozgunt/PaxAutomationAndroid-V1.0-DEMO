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
    // Son alınan screenshot yolu – crash olursa buradan kopyalayacağız
    private static volatile String lastScreenshotPath;

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

            // 🔹 Artık target/screenshots değil → logs/test
            File dir = new File("logs/test");
            if (!dir.exists()) {
                Files.createDirectories(dir.toPath());
            }

            File out = new File(dir, safeScenario + "_" + ts + ".png");
            try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(bytes);
            }

            lastScreenshotPath = out.getAbsolutePath();
            logger.info("Screenshot kaydedildi: {}", out.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Screenshot alınırken hata: {}", e.getMessage(), e);
        }
    }

    public static String getLastScreenshotPath() {
        return lastScreenshotPath;
    }

    // 🔹 Crash varsa screenshot'ı logs/crash altına kopyala
    public static void copyLastScreenshotToCrashFolder(String scenarioName) {
        try {
            if (lastScreenshotPath == null) {
                return;
            }
            File src = new File(lastScreenshotPath);
            if (!src.exists()) {
                return;
            }

            File crashDir = new File("logs/crash");
            if (!crashDir.exists()) {
                crashDir.mkdirs();
            }

            String ts = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date());
            String safeScenario = scenarioName.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
            File dest = new File(crashDir, safeScenario + "_" + ts + ".png");

            Files.copy(src.toPath(), dest.toPath());
            logger.info("Crash screenshot kopyalandı: {}", dest.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Crash screenshot kopyalanırken hata: {}", e.getMessage(), e);
        }
    }
}
