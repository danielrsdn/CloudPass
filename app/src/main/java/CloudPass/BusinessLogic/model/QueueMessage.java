package CloudPass.BusinessLogic.model;

import java.util.List;

public class QueueMessage {
    private final List<UserRecipient> recipients;
    private final String photoName;

    public QueueMessage(List<UserRecipient> recipients, String photoName) {
        this.recipients = recipients;
        this.photoName = photoName;
    }

    public List<UserRecipient> getRecipients() {
        return this.recipients;
    }

    public String getPhotoName() {
        return this.photoName;
    }
}
