package CloudPass.Presentation.model;

public class PhotoResponse {
    private final String photoName;
    private final String url;

    public PhotoResponse(String photoName, String url) {
        this.photoName = photoName;
        this.url = url;
    }

    public String getPhotoName() {
        return this.photoName;
    }

    public String getUrl() {
        return this.url;
    }

}
