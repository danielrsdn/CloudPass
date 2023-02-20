package CloudPass.Presentation.model;

public class DeviceLoginRequest {
    private final String deviceName;
    private final String deviceKey;

    public DeviceLoginRequest(String deviceName, String deviceKey) {
        this.deviceName = deviceName;
        this.deviceKey = deviceKey;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDeviceKey() {
        return this.deviceKey;
    }
}
