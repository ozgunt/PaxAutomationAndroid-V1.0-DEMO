package utilities;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PGmanager;
import pages.PGsampleSale;
import pages.PGtechPos;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

public class ReusableMethods {

    public static Process logcatProcess;
    public static String currentLogFilePath;

    public static AndroidDriver driver;

    public static PGsampleSale sampleSalePage;
    public static PGmanager managerPage;
    public static PGtechPos techPosPage;

    public static void setUp() throws Exception {

        // ✅ Aktif cihaz bilgilerini al
        Device device = DeviceManager.getActiveDevice();
        if (device == null) {
            throw new RuntimeException("❌ Device bulunamadı. DeviceConfiguration.properties kontrol et!");
        }

        // ✅ Aktif uygulamayı al (ör: Samplesale / Techpos / Manager)
        String activeAppKey = ConfigReader.getProperty("activeApp");

        // ✅ Package al
        String appPackage = ConfigReader.AppConfigReader.getAppProperty(activeAppKey + "PackageName");

        // ✅ APK yolu al (fallback)
        String relativeAppPath = ConfigReader.AppConfigReader.getAppProperty(activeAppKey);
        File appFile = new File(System.getProperty("user.dir") + "/" + relativeAppPath);

        // ✅ Appium server URL
        String serverUrl = ConfigReader.getProperty("appium.server.url");
        if (serverUrl == null || serverUrl.isEmpty()) {
            serverUrl = "http://127.0.0.1:4723/wd/hub";
        }

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", device.getPlatformName());
        caps.setCapability("automationName", device.getAutomationName());
        caps.setCapability("deviceName", device.getName());
        caps.setCapability("udid", device.getUdid());
        caps.setCapability("disableWindowAnimation", true);

        // ✅ HIZLI AÇILIŞ
        if (appPackage != null && !appPackage.isEmpty()) {
            caps.setCapability("appPackage", appPackage);
            System.out.println("📌 Package üzerinden başlatılıyor: " + appPackage);
        } else {
            caps.setCapability("app", appFile.getAbsolutePath());
            System.out.println("📌 APK yükleniyor: " + appFile.getAbsolutePath());
        }

        // ✅ Samplesale özel case → Activity gerekli
        if (activeAppKey.equalsIgnoreCase("Samplesale")) {
            String appActivity = ConfigReader.AppConfigReader.getAppProperty(activeAppKey + "MainActivity");
            caps.setCapability("appActivity", appActivity);
            System.out.println("🎯 Activity eklendi: " + appActivity);
        }

        caps.setCapability("noReset", true);
        caps.setCapability("fullReset", false);
        caps.setCapability("newCommandTimeout", 300);
        caps.setCapability("ignoreHiddenApiPolicyError", true);
        caps.setCapability("autoGrantPermissions", true);

        // ✅ Driver Başlat
        driver = new AndroidDriver(new URL(serverUrl), caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // ✅ Page init
        sampleSalePage = new PGsampleSale(driver);
        managerPage = new PGmanager(driver);
        techPosPage = new PGtechPos(driver);

        System.out.println("✅ AndroidDriver başlatıldı → " + device.getName());
        System.out.println("✅ Açılan uygulama → " + activeAppKey);
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                System.out.println("🛑 Uygulama kapatılıyor...");

                // ✅ Uygulamaları cihazdan öldür (senin mevcut davranışın)
                driver.terminateApp("com.pax.techpos");
                driver.terminateApp("com.pax.samplesalea");

            } catch (Exception e) {
                System.out.println("⚠️ App terminate sırasında sorun: " + e.getMessage());
            }

            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("⚠️ driver.quit sırasında sorun: " + e.getMessage());
            } finally {
                driver = null;
                System.out.println("🧹 AndroidDriver kapatıldı ✅");
            }
        }
    }

    // ✅ Senin step’ler bozulmasın diye İSMİ KORUDUM
    public static void driverWaitForApp() {
        AndroidDriver driverCast = (AndroidDriver) driver;
        WebDriverWait wait = new WebDriverWait(driverCast, Duration.ofSeconds(30));

        System.out.println("⏳ Uygulama geçişi bekleniyor...");

        String targetPackage = driverCast.getCurrentPackage();

        switch (targetPackage) {
            case "com.pax.samplesalea":
                wait.until(d -> "com.pax.samplesalea".equals(driverCast.getCurrentPackage()));
                System.out.println("✅ Samplesale aktif.");
                break;

            case "com.pax.techpos":
                wait.until(d -> "com.pax.techpos".equals(driverCast.getCurrentPackage()));
                System.out.println("✅ TechPOS aktif.");
                break;

            case "com.pax.mainapp":
                wait.until(d -> "com.pax.mainapp".equals(driverCast.getCurrentPackage()));
                System.out.println("✅ Manager aktif.");
                break;

            default:
                throw new RuntimeException("❌ Tanımsız package: " + targetPackage);
        }
    }

    public static void switchToApp(String expectedPackage) {
        try {
            String currentPackage = driver.getCurrentPackage();
            System.out.println("Aktif Package: " + currentPackage);

            if (expectedPackage.equals(currentPackage)) {
                System.out.println("✅ Zaten " + expectedPackage + " içindesin.");
                return;
            }

            System.out.println("⏳ " + expectedPackage + " uygulamasına geçiş bekleniyor...");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(d -> expectedPackage.equals(driver.getCurrentPackage()));

            System.out.println("✅ " + expectedPackage + " uygulamasına geçildi!");
        } catch (Exception e) {
            throw new RuntimeException("❌ switchToApp hata: " + e.getMessage());
        }
    }

    public static String getActivePackage() {
        String appKey = ConfigReader.getProperty("activeApp");
        return ConfigReader.AppConfigReader.getAppProperty(appKey + "PackageName");
    }

    public static void waitUntilActiveAppLoaded() {
        String expectedPackage = getActivePackage();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        wait.until(d -> expectedPackage.equals(driver.getCurrentPackage()));
        System.out.println("✅ Uygulama aktif: " + expectedPackage);
    }

    public static WebDriverWait iwait() {
        return new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public static boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ STALE/RACE için tek noktadan “stabil click”
    // Step’lerde tek tek locator yazmadan kullanırsın: ReusableMethods.safeClick(techPosPage.btnMKE, "MKE");
    public static void safeClick(WebElement element, String name) {
        int retry = 3;
        while (retry-- > 0) {
            try {
                iwait().until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
                element.click();
                System.out.println("✅ Click OK: " + name);
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException stale) {
                System.out.println("⚠️ STALE yakalandı: " + name + " (retry)");
            } catch (Exception e) {
                if (retry <= 0) {
                    throw e;
                }
                System.out.println("⚠️ Click retry: " + name + " → " + e.getMessage());
            }
        }
    }

    public static void assertElementVisible(String name, WebElement element) {
        try {
            iwait().until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
            System.out.println("✅ " + name + " görüldü.");
        } catch (Exception e) {
            System.out.println("❌ " + name + " GÖRÜLMEDİ!");
            Assertions.fail(name + " görünmedi!");
        }
    }

    public static void swipeUp() {
        try {
            Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.80);
            int endY = (int) (size.height * 0.20);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(0));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(0));

            driver.perform(Collections.singletonList(swipe));
            System.out.println("📱 swipeUp OK (pointer)");
        } catch (Exception e) {
            System.out.println("❌ swipeUp pointer hata: " + e.getMessage());
        }
    }

    public static void takeScreenshot(String name) {
        try {
            File src = driver.getScreenshotAs(OutputType.FILE);
            String path = "target/screenshots/" + name + "_" + System.currentTimeMillis() + ".png";
            File target = new File(path);
            org.apache.commons.io.FileUtils.copyFile(src, target);
            System.out.println("📸 Screenshot kaydedildi: " + path);
        } catch (Exception e) {
            System.out.println("❌ Screenshot alınamadı: " + e.getMessage());
        }
    }

    public static void forceCloseApp(String packageName) {
        try {
            Runtime.getRuntime().exec("adb shell am force-stop " + packageName);
            System.out.println("✅ " + packageName + " zorla kapatıldı.");
        } catch (Exception e) {
            System.out.println("⚠️ " + packageName + " kapatma başarısız: " + e.getMessage());
        }
    }

    public static void closeKeyboard() {
        try {
            driver.hideKeyboard();
            System.out.println("klavye kapatıldı");
        } catch (Exception e) {
            System.out.println("klavye zaten kapalı");
        }
    }

    public static void pressBack() {
        Map<String, Object> args = new HashMap<>();
        args.put("command", "input");
        args.put("args", Arrays.asList("keyevent", "4"));
        driver.executeScript("mobile: shell", args);
    }
}
