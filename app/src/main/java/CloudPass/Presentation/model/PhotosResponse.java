package CloudPass.Presentation.model;

import java.util.List;

public class PhotosResponse {
    private final List<PhotoResponse> photos;

    public PhotosResponse(List<PhotoResponse> photos) {
        this.photos = photos;
    }

    public List<PhotoResponse> getPhotos() {
        return this.photos;
    }
}
