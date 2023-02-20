package CloudPass.Presentation.model;

import java.util.List;
import java.util.Map;

public class UploadPhotoResponse {
    private final String photoName;
    private final String url;
    private final String httpRequest;
    private final Map<String, List<String>> headers;

    public UploadPhotoResponse(String photoName, String url, String httpRequest, Map<String, List<String>> headers) {
        this.photoName = photoName;
        this.url = url;
        this.httpRequest = httpRequest;
        this.headers = headers;
    }
    
    public String getPhotoName() {
        return this.photoName;
    }

    public String getUrl() {
        return this.url;
    }

    public String getHttpRequest() {
        return this.httpRequest;
    }

    public  Map<String, List<String>> getHeaders() {
        return this.headers;
    }
}
