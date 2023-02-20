package CloudPass.BusinessLogic.model;

public class User {
    private final int id;
    private final String username;
    private final String fname;
    private final String lname;
    private final String mobile;
    private final int notify;
    private final String deviceName;

    public User(int id, String username, String fname, String lname, String mobile, int notify, String deviceName) {
        this.id = id;
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.notify = notify;
        this.deviceName = deviceName;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFirstName() {
        return this.fname;
    }

    public String getLastName() {
        return this.lname;
    }

    public String getMobile() {
        return this.mobile;
    }

    public int getNotify() {
        return this.notify;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}
