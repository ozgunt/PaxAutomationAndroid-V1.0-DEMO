package hooks;

import io.cucumber.java.*;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.LoggerUtil;
import utilities.ScreenshotUtil;
import utilities.ReusableMethods;

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

    @Before
    public void beforeScenario(Scenario scenario) {
        ThreadContext.put("scenario", "[" + scenario.getName() + "]");
        logger.info("Senaryo başladı: {}", scenario.getName());
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

        ThreadContext.clearAll();
        try {
            Thread.sleep(Duration.ofMillis(200).toMillis());
        } catch (Exception ignored) {
        }
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

        // 1) Driver yoksa step atlanır (patlamasın)
        if (ReusableMethods.driver == null) {
            System.out.println("⚠️ Driver null, step atlanıyor!");
            return;
        }

        // 2) Sadece bilgi amaçlı package logla — AMA HİÇ MÜDAHALE ETME
        System.out.println("🔍 [BeforeStep] Aktif Package: " + ReusableMethods.driver.getCurrentPackage());

        // 3) UI snapshot tazele (hata verirse yut)
        try {
            ReusableMethods.driver.getPageSource();
        } catch (Exception ignore) {
        }

        // 4) Kısa nefes — stall/donma riskini azaltır
        Thread.sleep(300);
    }
}

