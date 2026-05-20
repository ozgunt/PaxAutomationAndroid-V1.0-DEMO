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

    @Test
    public void openApp() throws Exception {
        Device device = DeviceManager.getActiveDevice();
        String appKey = ConfigReader.AppConfigReader.getAppProperty("activeApp");
        String apkPath = ConfigReader.AppConfigReader.getAppProperty(appKey);

        File appFile = new File(System.getProperty("user.dir") + "/" + apkPath);
        if (!appFile.exists()) throw new RuntimeException("APK bulunamadı: " + appFile.getAbsolutePath());

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", device.getPlatformName());
        caps.setCapability("automationName", device.getAutomationName());
        caps.setCapability("deviceName", device.getName());
        caps.setCapability("udid", device.getUdid());
        caps.setCapability("app", appFile.getAbsolutePath());
        caps.setCapability("newCommandTimeout", 300);

        AndroidDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), caps);
        Thread.sleep(5000);
        System.out.println("✅ APK açıldı: " + appKey);
        driver.quit();
    }
}

