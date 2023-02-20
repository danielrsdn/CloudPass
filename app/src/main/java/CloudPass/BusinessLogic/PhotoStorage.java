package CloudPass.BusinessLogic;

import CloudPass.BusinessLogic.model.UploadPhoto;
import CloudPass.Utils.model.Result;

public interface PhotoStorage {

        public Result<String> getPhoto(String name);

        public Result<Boolean> checkPhotoUploaded(String name);

        public Result<UploadPhoto> getPhotoUpload(String key);
}
