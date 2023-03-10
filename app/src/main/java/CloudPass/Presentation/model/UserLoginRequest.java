package CloudPass.Presentation.model;

public class UserLoginRequest {
    private final String username;
    private final String password;

    public UserLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
