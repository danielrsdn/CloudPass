package CloudPass.Presentation.model;

public class GetUserResponse {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String mobile;
    private final boolean enableNotifications;
    private final String deviceName;

    public GetUserResponse(String username, String firstName, String lastName, String mobile, boolean enableNotifications, String deviceName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.enableNotifications = enableNotifications;
        this.deviceName = deviceName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getMobile() {
        return this.mobile;
    }

    public boolean getEnableNotifications() {
        return this.enableNotifications;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}
