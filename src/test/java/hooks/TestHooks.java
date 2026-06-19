package hooks;

import io.cucumber.java.*;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.*;

public class TestHooks {

    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);
    private static boolean logcatStarted = false;

    @BeforeAll
    public static void beforeAll() {
        logger.info("=== TEST SUITE STARTED ===");
    }

    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {
        ThreadContext.put("scenario", "[" + scenario.getName() + "]");
        logger.info("Scenario started: {}", scenario.getName());
        logcatStarted = false;
    }

    @Before(order = 0)
    public void ensureActiveApp() throws Exception {
        PageContext.setUp();
    }

    @BeforeStep
    public void beforeStep(Scenario scenario) {
        if (ReusableMethods.driver != null) {
            PageContext.waitForAppStability();
        }
        if (!logcatStarted) {
            try {
                LogcatUtility.startLogcat(scenario.getName());
                logcatStarted = true;
            } catch (Exception e) {
                logger.error("Logcat error", e);
            }
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario FAILED -> {}", scenario.getName());
            ReusableMethods.quitDriver();
        } else {
            logger.info("Scenario PASSED -> {}", scenario.getName());
        }
        String rawPath = LogcatUtility.stopLogcat();
        try {
            LogFilter.processRawLog(rawPath, scenario.getName(), scenario.isFailed());
        } catch (Exception e) {
            logger.error("Error processing log filter", e);
        }
        ThreadContext.clearAll();
    }

    @AfterAll
    public static void afterAll() {
        logger.info("=== TEST SUITE ENDED ===");
    }
}