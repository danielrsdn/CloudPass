package CloudPass.Presentation.model;

public class UpdateUserRequest {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String mobile;
    private final boolean enableNotifications;

    public UpdateUserRequest(String username, String firstName, String lastName, String mobile, boolean enableNotifications) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.enableNotifications = enableNotifications;
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
}
