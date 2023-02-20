package CloudPass.BusinessLogic;

import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.Context;

import CloudPass.BusinessLogic.model.AssociatedUser;
import CloudPass.BusinessLogic.model.PhotoMetadata;
import CloudPass.BusinessLogic.model.User;
import CloudPass.Utils.model.Result;

public interface DataDB {

    public Result<String> UserAuthenticate(String username, String password, Context context);

    public Result<Integer> UserAuthenticate(String sessionID, Context context);

    public Result<String> DeviceAuthenticate(String deviceName, String deviceKey, Context context);

    public Result<Integer> DeviceAuthenticate(String sessionID, Context context);

    public Result<ArrayList<PhotoMetadata>> GetAllPhotos(String sessionID, Context context);

    public Result<String> UploadPendingPhoto(String sessionID, Context context);

    public Result<Boolean> ConfirmPendingPhoto(String sessionID, String photoName, Context context);

    public Result<ArrayList<AssociatedUser>> GetAssociatedUsers(String sessionID, Context context);

    public Result<User> GetSelfUser(String sessionId, Context context);

    public Result<Boolean> UpdateUser(String sessionId, User user, Context context);

    public Result<Boolean> CheckUsernameExists(String username, Context context);

}
