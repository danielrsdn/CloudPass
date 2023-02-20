package CloudPass.Presentation.model;

public class NotifyRequest {
    private final String photoName;

    public NotifyRequest(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoName() {
        return this.photoName;
    }
}
