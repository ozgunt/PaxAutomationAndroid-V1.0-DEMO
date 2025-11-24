package utilities;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.time.Duration;

import static utilities.ReusableMethods.driver;

public class DriverRuner {

    private AndroidDriver driver;

    @BeforeEach
    public void setUp() throws Exception {



        // Aktif cihaz bilgilerini al
        Device device = DeviceManager.getActiveDevice();

        // Aktif uygulama bilgisi
        String activeAppKey = ConfigReader.AppConfigReader.getAppProperty("activeApp");
        String relativeAppPath = ConfigReader.AppConfigReader.getAppProperty(activeAppKey);

        // APK dosyasını kontrol et
        File appFile = new File(System.getProperty("user.dir") + "/" + relativeAppPath);
        if (!appFile.exists()) {
            throw new RuntimeException("APK dosyası bulunamadı: " + appFile.getAbsolutePath());
        }

        System.out.println("Using APK path: " + appFile.getAbsolutePath());

        // DesiredCapabilities Ayarları
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", device.getPlatformName());
        caps.setCapability("automationName", device.getAutomationName());
        caps.setCapability("deviceName", device.getName());
        caps.setCapability("udid", device.getUdid());
        caps.setCapability("app", appFile.getAbsolutePath());
        caps.setCapability("newCommandTimeout", 300);

        System.out.println("Starting Appium session on device: " + device.getName());

        // AndroidDriver Başlat
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
    }

    @Test
    public void openApp1() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("✅ APK başarıyla açıldı!");
    }

    // Eğer istersen teardown ekleyebilirsin:
    // @AfterEach
    // public void tearDown() {
    //     if (driver != null) driver.quit();
    // }
}
