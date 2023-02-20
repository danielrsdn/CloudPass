package CloudPass.BusinessLogic.model;

public class AssociatedUser {
    private final int id;
    private final String username;
    private final String fname;
    private final String lname;
    private final String mobile;

    public AssociatedUser(int id, String username, String fname, String lname, String mobile) {
        this.id = id;
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
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
}
