package hooks;

import io.cucumber.java.*;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.Logger;
import utilities.LoggerUtil;
import utilities.ScreenshotUtil;
import utilities.ReusableMethods;
import utilities.LogcatUtility;

import java.time.Duration;

import static utilities.ReusableMethods.driver;
import static utilities.ReusableMethods.takeScreenshot;

public class TestHooks {

    private static final Logger logger = LoggerUtil.getLogger();

    @BeforeAll
    public static void beforeAll() {
        logger.info("=== TEST SUITE BAŞLADI ===");
    }

    @AfterAll
    public static void afterAll() {
        logger.info("=== TEST SUITE BİTTİ ===");
    }

    // ============================================================
    //   🚀 SADECE 1 ADET beforeScenario — Logcat buraya eklendi
    // ============================================================
    @Before
    public void beforeScenario(Scenario scenario) throws Exception {

        ThreadContext.put("scenario", "[" + scenario.getName() + "]");
        logger.info("Senaryo başladı: {}", scenario.getName());

        // 🔥 FULL LOGCAT BAŞLAT
        LogcatUtility.startLogcat(scenario.getName());
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.warn("❌ Adım FAIL oldu → Screenshot alınıyor...");
            if (driver != null) {
                ScreenshotUtil.captureAndAttach(driver, scenario);
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            logger.error("❌ Senaryo FAIL → {}", scenario.getName());
            if (driver != null) {
                ScreenshotUtil.captureAndAttach(driver, scenario);
            }
        } else {
            logger.info("✅ Senaryo PASS → {}", scenario.getName());
        }

        // 🔥 FULL LOGCAT DURDUR
        LogcatUtility.stopLogcat();

        ThreadContext.clearAll();
        try { Thread.sleep(200); } catch (Exception ignored) {}
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDown() {
        System.out.println("✅ Tüm testler bitti → uygulama kapatılıyor");
        ReusableMethods.quitDriver();
    }

    @AfterStep
    public void takeScreenshotAfterFailure(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed()) {
            takeScreenshot(scenario.getName());
            System.out.println("⚠️ Step fail oldu ama UYGULAMA KAPANMIYOR!");
        }
    }

    @BeforeStep
    public void beforeEachStep() throws InterruptedException {

        if (ReusableMethods.driver == null) {
            System.out.println("⚠️ Driver null, step atlanıyor!");
            return;
        }

        System.out.println("🔍 [BeforeStep] Aktif Package: " + ReusableMethods.driver.getCurrentPackage());

        try { ReusableMethods.driver.getPageSource(); } catch (Exception ignore) {}

        Thread.sleep(300);
    }
}
