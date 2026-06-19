package utilities;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PageContext {

    public static PGsampleSale sampleSalePage;
    public static PGmanager    managerPage;
    public static PGtechPos    techPosPage;
    public static PGtosla      toslaPage;
    public static PGodeal      odealPage;

    private static final Map<String, Object> pageCache = new ConcurrentHashMap<>();

    public static void setUp() throws Exception {
        if (ReusableMethods.driver != null) {
            try {
                ReusableMethods.driver.getCapabilities();
                reinitIfNull(ReusableMethods.driver);
                return;
            } catch (Exception e) {
                ReusableMethods.driver = null;
            }
        }

        Device device = DeviceManager.getActiveDevice();
        if (device == null) {
            throw new RuntimeException("Device not found");
        }

        String activeAppKey    = ConfigReader.getProperty("activeApp");
        String appPackage      = ConfigReader.AppConfigReader.getAppProperty(activeAppKey + "PackageName");
        String relativeAppPath = ConfigReader.AppConfigReader.getAppProperty(activeAppKey);

        String serverUrl = ConfigReader.getProperty("appium.server.url");
        if (serverUrl == null || serverUrl.isEmpty()) {
            serverUrl = "http://127.0.0.1:4723/";
        }

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName",           device.getPlatformName());
        caps.setCapability("automationName",         device.getAutomationName());
        caps.setCapability("deviceName",             device.getName());
        caps.setCapability("udid",                   device.getUdid());

        caps.setCapability("appium:noSign", true);
        caps.setCapability("appium:disableWindowAnimation", true);
        caps.setCapability("appium:ignoreUnimportantViews", true);
        caps.setCapability("appium:skipServerInstallation", false);
        caps.setCapability("appium:skipDeviceInitialization", false);

        if (relativeAppPath != null && !relativeAppPath.isEmpty() && !relativeAppPath.equalsIgnoreCase("null")) {
            File appFile = new File(System.getProperty("user.dir") + "/" + relativeAppPath);
            caps.setCapability("app", appFile.getAbsolutePath());
        }

        if (appPackage != null && !appPackage.isEmpty()) {
            caps.setCapability("appPackage", appPackage);
        }

        String appActivity = ConfigReader.AppConfigReader.getAppProperty(activeAppKey + "MainActivity");
        if (appActivity != null && !appActivity.isEmpty()) {
            caps.setCapability("appActivity", appActivity);
        }

        caps.setCapability("noReset",                    true);
        caps.setCapability("fullReset",                  false);
        caps.setCapability("dontStopAppOnReset",         true);
        caps.setCapability("newCommandTimeout",          300);
        caps.setCapability("ignoreHiddenApiPolicyError", true);
        caps.setCapability("autoGrantPermissions",       true);

        caps.setCapability("appium:uiautomator2ServerInstallTimeout", 30000);
        caps.setCapability("appium:uiautomator2ServerLaunchTimeout",  30000);
        caps.setCapability("appium:androidInstallTimeout",            60000);

        ReusableMethods.driver = new AndroidDriver(new URL(serverUrl), caps);
        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        init(ReusableMethods.driver);

        try {
            new WebDriverWait(ReusableMethods.driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOf(sampleSalePage.txtTutar));
        } catch (Exception ignored) {}
    }

    public static void init(AndroidDriver driver) {
        sampleSalePage = new PGsampleSale(driver);
        managerPage    = new PGmanager(driver);
        techPosPage    = new PGtechPos(driver);
        toslaPage      = new PGtosla(driver);
        odealPage      = new PGodeal(driver);
    }

    public static void reinitIfNull(AndroidDriver driver) {
        if (driver == null) return;
        if (sampleSalePage == null || managerPage == null || techPosPage == null || toslaPage == null || odealPage == null) {
            init(driver);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getPage(Class<T> pageClass) {
        if (ReusableMethods.driver == null) {
            throw new RuntimeException("Driver not initialized");
        }
        String className = pageClass.getName();
        if (!pageCache.containsKey(className)) {
            try {
                java.lang.reflect.Constructor<T> constructor = pageClass.getConstructor(AndroidDriver.class);
                T pageInstance = constructor.newInstance(ReusableMethods.driver);
                pageCache.put(className, pageInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (T) pageCache.get(className);
    }

    public static void waitForAppStability() {
        if (ReusableMethods.driver == null) return;
        try {
            String p1 = ReusableMethods.driver.getCurrentPackage();
            Thread.sleep(150);
            String p2 = ReusableMethods.driver.getCurrentPackage();
            int retry = 0;
            while (p1 != null && !p1.equals(p2) && retry < 12) {
                p1 = p2;
                Thread.sleep(200);
                p2 = ReusableMethods.driver.getCurrentPackage();
                retry++;
            }
        } catch (Exception ignored) {}
    }
}