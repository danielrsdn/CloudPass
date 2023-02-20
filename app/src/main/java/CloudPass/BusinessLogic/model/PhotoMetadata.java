package CloudPass.BusinessLogic.model;

public class PhotoMetadata {
    private final int photo_id;
    private final String photo_name;

    public PhotoMetadata(int photo_id, String photo_name)
    {
        this.photo_id = photo_id;
        this.photo_name = photo_name;
    }

    public int getPhotoId()
    {
        return this.photo_id;
    }

    public String getPhotoName()
    {
        return this.photo_name;
    }
}
