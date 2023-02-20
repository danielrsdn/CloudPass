package CloudPass.BusinessLogic.model;

public class UserRecipient {
    private final String fname;
    private final String lname;
    private final String mobile;

    public UserRecipient(String fname, String lname, String mobile) {
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
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
