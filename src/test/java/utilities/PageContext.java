package utilities;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.PGmanager;
import pages.PGsampleSale;
import pages.PGtechPos;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class PageContext {

    // ── Page object'lerin tek yaşadığı yer ──
    public static PGsampleSale sampleSalePage;
    public static PGmanager    managerPage;
    public static PGtechPos    techPosPage;

    // ── Driver başlatılır + page object'ler init edilir ──
    public static void setUp() throws Exception {

        Device device = DeviceManager.getActiveDevice();
        if (device == null) {
            throw new RuntimeException("❌ Device bulunamadı. DeviceConfiguration.properties kontrol et!");
        }

        String activeAppKey    = ConfigReader.getProperty("activeApp");
        String appPackage      = ConfigReader.AppConfigReader.getAppProperty(activeAppKey + "PackageName");
        String relativeAppPath = ConfigReader.AppConfigReader.getAppProperty(activeAppKey);
        File appFile = new File(System.getProperty("user.dir") + "/" + relativeAppPath);

        String serverUrl = ConfigReader.getProperty("appium.server.url");
        if (serverUrl == null || serverUrl.isEmpty()) {
            serverUrl = "http://127.0.0.1:4723/";
        }

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName",           device.getPlatformName());
        caps.setCapability("automationName",         device.getAutomationName());
        caps.setCapability("deviceName",             device.getName());
        caps.setCapability("udid",                   device.getUdid());
        caps.setCapability("disableWindowAnimation", true);

        if (appPackage != null && !appPackage.isEmpty()) {
            caps.setCapability("appPackage", appPackage);
            System.out.println("📌 Package üzerinden başlatılıyor: " + appPackage);
        } else {
            caps.setCapability("app", appFile.getAbsolutePath());
            System.out.println("📌 APK yükleniyor: " + appFile.getAbsolutePath());
        }

        String appActivity = ConfigReader.AppConfigReader.getAppProperty(activeAppKey + "MainActivity");
        if (appActivity != null && !appActivity.isEmpty()) {
            caps.setCapability("appActivity", appActivity);
            System.out.println("🎯 Activity eklendi: " + appActivity);
        }

        caps.setCapability("noReset",                    true);
        caps.setCapability("fullReset",                  false);
        caps.setCapability("dontStopAppOnReset",         true);
        caps.setCapability("newCommandTimeout",          300);
        caps.setCapability("ignoreHiddenApiPolicyError", true);
        caps.setCapability("autoGrantPermissions",       true);

        ReusableMethods.driver = new AndroidDriver(new URL(serverUrl), caps);
        ReusableMethods.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        init(ReusableMethods.driver);

        System.out.println("✅ AndroidDriver başlatıldı → " + device.getName());
        System.out.println("✅ Açılan uygulama → " + activeAppKey);
    }

    // ── Tüm page object'leri (yeniden) init et ──
    // new PG*(driver) ekranı/uygulamayı DEĞİŞTİRMEZ, sadece PageFactory bağlar
    public static void init(AndroidDriver driver) {
        sampleSalePage = new PGsampleSale(driver);
        managerPage    = new PGmanager(driver);
        techPosPage    = new PGtechPos(driver);
        System.out.println("✅ PageContext init edildi.");
    }

    // ── Sadece null olanları yeniden init et (her adım öncesi çağrılır) ──
    public static void reinitIfNull(AndroidDriver driver) {
        if (driver == null) return;
        if (sampleSalePage == null) sampleSalePage = new PGsampleSale(driver);
        if (managerPage    == null) managerPage    = new PGmanager(driver);
        if (techPosPage    == null) techPosPage    = new PGtechPos(driver);
    }
}