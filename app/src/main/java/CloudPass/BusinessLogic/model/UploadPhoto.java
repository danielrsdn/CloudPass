package CloudPass.BusinessLogic.model;

import java.util.List;
import java.util.Map;

public class UploadPhoto {
    private final String url;
    private final String httpRequest;
    private final Map<String, List<String>> headers;

    public UploadPhoto(String url, String httpRequest, Map<String, List<String>> headers) {
        this.url = url;
        this.httpRequest = httpRequest;
        this.headers = headers;
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
