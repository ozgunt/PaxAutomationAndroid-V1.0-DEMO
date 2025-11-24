package utilities;

public class Device {

    private String name;
    private String udid;
    private String platformName;
    private String automationName;

    // Constructor
    public Device(String name, String udid, String platformName, String automationName) {
        this.name = name;
        this.udid = udid;
        this.platformName = platformName;
        this.automationName = automationName;
    }

    // Getter'lar
    public String getName() {
        return name;
    }

    public String getUdid() {
        return udid;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getAutomationName() {
        return automationName;
    }

    // Setter'lar (isteğe bağlı)
    public void setName(String name) {
        this.name = name;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public void setAutomationName(String automationName) {
        this.automationName = automationName;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", udid='" + udid + '\'' +
                ", platformName='" + platformName + '\'' +
                ", automationName='" + automationName + '\'' +
                '}';
    }
}
