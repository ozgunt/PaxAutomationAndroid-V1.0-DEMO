package hooks;

import io.cucumber.java.*;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.Logger;
import utilities.LoggerUtil;
import utilities.PageContext;
import utilities.ScreenshotUtil;
import utilities.ReusableMethods;
import utilities.LogcatUtility;
import utilities.LogFilter;

public class TestHooks {

    private static final Logger logger = LoggerUtil.getLogger();

    // Logcat'in sadece 1 kere başlaması için
    private boolean logcatStarted = false;

    @BeforeAll
    public static void beforeAll() {
        LoggerUtil.getLogger().info("=== TEST SUITE BAŞLADI ===");
    }

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {
        ThreadContext.put("scenario", "[" + scenario.getName() + "]");
        logger.info("Senaryo başladı: {}", scenario.getName());
        logcatStarted = false;
    }

    @Before(order = 0)
    public void ensureSampleSale() throws Exception {
        if (ReusableMethods.driver == null) return;
        String currentPkg = ReusableMethods.driver.getCurrentPackage();
        if (!"com.pax.samplesalea".equals(currentPkg)) {
            PageContext.setUp();        }
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {

        // Driver varsa null olan page object'leri reinit et
        if (ReusableMethods.driver != null) {
            PageContext.reinitIfNull(ReusableMethods.driver);
        }

        // Logcat sadece ilk step'te başlasın
        if (!logcatStarted) {
            try {
                LogcatUtility.startLogcat(scenario.getName());
                logcatStarted = true;
                logger.info("🔵 Logcat ilk stepte başlatıldı.");
            } catch (Exception e) {
                logger.error("❌ Logcat başlatılamadı", e);
            }
        }
    }



    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.warn("❌ Adım FAIL oldu → Screenshot alınıyor...");
            if (ReusableMethods.driver != null) {
                ScreenshotUtil.captureAndAttach(ReusableMethods.driver, scenario);
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            logger.error("❌ Senaryo FAIL → {}", scenario.getName());
            ReusableMethods.quitDriver();
        } else {
            logger.info("✅ Senaryo PASS → {}", scenario.getName());
        }

        // RAW logu kapat ve kategorilere ayır
        String rawPath = LogcatUtility.stopLogcat();
        try {
            LogFilter.processRawLog(rawPath, scenario.getName(), scenario.isFailed());
        } catch (Exception e) {
            logger.error("LogFilter çalışırken hata oluştu", e);
        }

        ThreadContext.clearAll();
    }

    @AfterAll
    public static void afterAll() {
        LoggerUtil.getLogger().info("=== TEST SUITE BİTTİ ===");
    }
}