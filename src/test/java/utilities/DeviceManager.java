package utilities;

import java.util.HashMap;
import java.util.Map;

public class DeviceManager {

    public static Device getActiveDevice() {
        String deviceName = ConfigReader.getProperty("activeDevice");
        String udid = ConfigReader.getProperty("activeUdid");
        String platformName = ConfigReader.getProperty("platformName");
        String automationName = ConfigReader.getProperty("automationName");

        return new Device(deviceName, udid, platformName, automationName);
    }
    }

