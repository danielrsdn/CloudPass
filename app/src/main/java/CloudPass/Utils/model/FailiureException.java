package CloudPass.Utils.model;

public class FailiureException extends Exception {
    private final Status status;
    private String errorMessage;

    public FailiureException(Status status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public FailiureException(Status status) {
        this.status = status;
        this.errorMessage = null;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
