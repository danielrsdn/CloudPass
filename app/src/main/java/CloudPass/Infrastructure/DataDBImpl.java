package CloudPass.Infrastructure;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;

import CloudPass.BusinessLogic.DataDB;
import CloudPass.BusinessLogic.model.AssociatedUser;
import CloudPass.BusinessLogic.model.PhotoMetadata;
import CloudPass.BusinessLogic.model.User;
import CloudPass.Utils.model.FailiureException;
import CloudPass.Utils.model.Result;
import CloudPass.Utils.model.Status.FAILURE;

public class DataDBImpl implements DataDB {
    private final Connection connection;

    public DataDBImpl(Connection connection) {
        this.connection = connection;
    }

    public Result<String> UserAuthenticate(String username, String password, Context context) {

        try {
            String selectUsersStatement = "SELECT id FROM users WHERE username = ? AND password = ?";
            PreparedStatement selectUsers = this.connection.prepareStatement(selectUsersStatement);
            selectUsers.setString(1, username);
            selectUsers.setString(2, password);
            ResultSet resultSet = selectUsers.executeQuery();
            int userId = -1;

            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }

            else {
                return Result.fail(FAILURE.UNAUTHORIZED);
            }

            String insertStatement = "INSERT INTO user_sessions (id, session_id) VALUES (?, ?)";
            PreparedStatement insertSessions = this.connection.prepareStatement(insertStatement);
            String sessionID = UUID.randomUUID().toString();

            insertSessions.setInt(1, userId);
            insertSessions.setString(2, sessionID);

            if (insertSessions.executeUpdate() != 1) {
                return Result.fail(FAILURE.SERVER_ERROR);
            }

            return Result.success(sessionID);
        }

        catch (SQLException e) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<Integer> UserAuthenticate(String sessionID, Context context) {

        try {
            String queryStatement = "SELECT id FROM user_sessions WHERE session_id = ?";
            PreparedStatement selectID = this.connection.prepareStatement(queryStatement);
            selectID.setString(1, sessionID);

            ResultSet resultSet = selectID.executeQuery();

            if (resultSet.next()) {
                return Result.success(resultSet.getInt("id"));
            }
            return Result.fail(FAILURE.UNAUTHORIZED);
        }

        catch (SQLException e) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<String> DeviceAuthenticate(String deviceName, String deviceKey, Context context) {

        try {
            String selectUsersStatement = "SELECT id FROM devices WHERE deviceName = ? AND deviceKey = ?";
            PreparedStatement selectUsers = this.connection.prepareStatement(selectUsersStatement);
            selectUsers.setString(1, deviceName);
            selectUsers.setString(2, deviceKey);
            ResultSet resultSet = selectUsers.executeQuery();
            int userId = -1;

            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }

            else {
                return Result.fail(FAILURE.UNAUTHORIZED);
            }

            String insertStatement = "INSERT INTO device_sessions (id, session_id) VALUES (?, ?)";
            PreparedStatement insertSessions = this.connection.prepareStatement(insertStatement);
            String sessionID = UUID.randomUUID().toString();

            insertSessions.setInt(1, userId);
            insertSessions.setString(2, sessionID);

            if (insertSessions.executeUpdate() != 1) {
                return Result.fail(FAILURE.SERVER_ERROR);
            }

            return Result.success(sessionID);
        }

        catch (SQLException e) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<Integer> DeviceAuthenticate(String sessionID, Context context) {

        try {
            String queryStatement = "SELECT id FROM device_sessions WHERE session_id = ?";
            PreparedStatement selectID = this.connection.prepareStatement(queryStatement);
            selectID.setString(1, sessionID);

            ResultSet resultSet = selectID.executeQuery();

            if (resultSet.next()) {
                return Result.success(resultSet.getInt("id"));
            }

            context.getLogger().log("Cannot authorize deivce");
            return Result.fail(FAILURE.UNAUTHORIZED);
        }

        catch (SQLException e) {
            context.getLogger().log("SQL Server Error");
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<ArrayList<PhotoMetadata>> GetAllPhotos(String sessionID, Context context) {
        try {
            int userId = UserAuthenticate(sessionID, context).getOrThrow();
            String getDeviceStatement = "SELECT device FROM users WHERE id = ?";
            PreparedStatement getDevice = this.connection.prepareStatement(getDeviceStatement);
            getDevice.setInt(1, userId);
            ResultSet rs = getDevice.executeQuery();
            int deviceId = -1;
            if (rs.next()) {
                deviceId = rs.getInt("device");
            }

            if (deviceId == -1) {
                return Result.fail(FAILURE.FORBIDDEN);
            }

            String getAllPhotosStatement = "SELECT photo_id, photo_name FROM photos WHERE device_id = ?";
            PreparedStatement getAllPhotos = this.connection.prepareStatement(getAllPhotosStatement);
            getAllPhotos.setInt(1, deviceId);
            ResultSet photosRS = getAllPhotos.executeQuery();
            ArrayList<PhotoMetadata> allPhotos = new ArrayList<>();
            while (photosRS.next()) {
                allPhotos.add(
                        new PhotoMetadata(
                                photosRS.getInt("photo_id"),
                                photosRS.getString("photo_name")));
            }

            return Result.success(allPhotos);
        }

        catch (FailiureException e) {
            return Result.fail(e.getStatus());
        }

        catch (SQLException se) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<String> UploadPendingPhoto(String sessionID, Context context) {
        try {
            int deviceId = DeviceAuthenticate(sessionID, context).getOrThrow();
            String photoName = UUID.randomUUID().toString() + ".jpg";
            String uploadPendingPhotoStatement = "INSERT INTO pending_photos (photo_name, device_id) VALUES (?, ?)";
            PreparedStatement uploadPendingPhoto = this.connection.prepareStatement(uploadPendingPhotoStatement);
            uploadPendingPhoto.setString(1, photoName);
            uploadPendingPhoto.setInt(2, deviceId);

            if (uploadPendingPhoto.executeUpdate() != 1) {
                return Result.fail(FAILURE.SERVER_ERROR);
            }

            return Result.success(photoName);
        }

        catch (FailiureException e) {
            return Result.fail(e.getStatus());
        }

        catch (SQLException se) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<Boolean> ConfirmPendingPhoto(String sessionID, String photoName, Context context) {
        try {
            int deviceId = DeviceAuthenticate(sessionID, context).getOrThrow();
            String getPendingPhotoStatement = "SELECT device_id from pending_photos WHERE photo_name = ?";
            PreparedStatement getPendingPhoto = this.connection.prepareStatement(getPendingPhotoStatement);
            getPendingPhoto.setString(1, photoName);
            ResultSet rs = getPendingPhoto.executeQuery();

            if (!rs.next()) {
                context.getLogger().log("Could not find photo");
                return Result.fail(FAILURE.NOT_FOUND);
            }

            if (rs.getInt("device_id") != deviceId) {
                context.getLogger().log("Device not authorized to confirm photo");
                return Result.fail(FAILURE.FORBIDDEN);
            }

            String uploadPhotoStatement = "INSERT INTO photos (photo_name, device_id) VALUES (?, ?)";
            PreparedStatement uploadPhoto = this.connection.prepareStatement(uploadPhotoStatement);
            uploadPhoto.setString(1, photoName);
            uploadPhoto.setInt(2, deviceId);
            if (uploadPhoto.executeUpdate() != 1) {
                context.getLogger().log("Internal MySQL server error");
                return Result.fail(FAILURE.SERVER_ERROR);
            }

            String deletePendingPhotoStatement = "DELETE FROM pending_photos WHERE photo_name = ?";
            PreparedStatement deletePendingPhoto = this.connection.prepareStatement(deletePendingPhotoStatement);
            deletePendingPhoto.setString(1, photoName);
            if (deletePendingPhoto.executeUpdate() != 1) {
                context.getLogger().log("Internal MySQL server error");
                return Result.fail(FAILURE.SERVER_ERROR);
            }

            return Result.success(true);
        }

        catch (FailiureException e) {
            return Result.fail(e.getStatus());
        }

        catch (SQLException se) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<ArrayList<AssociatedUser>> GetAssociatedUsers(String sessionID, Context context) {
        try {
            int deviceId = DeviceAuthenticate(sessionID, context).getOrThrow();
            String selectUsersStatement = "SELECT id, username, fname, lname, mobile FROM users WHERE device = ? AND notify = ?";
            PreparedStatement selectUsers = this.connection.prepareStatement(selectUsersStatement);
            selectUsers.setInt(1, deviceId);
            selectUsers.setInt(2, 1);
            ResultSet rs = selectUsers.executeQuery();
            ArrayList<AssociatedUser> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new AssociatedUser(rs.getInt("id"), rs.getString("username"), rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("mobile")));
            }

            return Result.success(users);
        }

        catch (FailiureException e) {
            return Result.fail(e.getStatus());
        }

        catch (SQLException se) {
            context.getLogger().log("Internal MySQL server error");
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<User> GetSelfUser(String sessionId, Context context) {
        try {
            int userId = UserAuthenticate(sessionId, context).getOrThrow();
            String selectUserStatement = "SELECT u.id, u.username, u.fname, u.lname, u.mobile, u.notify, d.deviceName FROM users AS u, devices AS d WHERE u.id = ? AND (u.device = d.deviceID)";
            PreparedStatement selectUser = this.connection.prepareStatement(selectUserStatement);
            selectUser.setInt(1, userId);
            ResultSet result = selectUser.executeQuery();
            if (!result.next()) {
                return Result.fail(FAILURE.NOT_FOUND);
            }
            return Result.success(
                    new User(result.getInt("u.id"), result.getString("u.username"), result.getString("u.fname"),
                            result.getString("u.lname"), result.getString("u.mobile"), result.getInt("u.notify"),
                            result.getString("d.deviceName")));
        }

        catch (FailiureException e) {
            return Result.fail(e.getStatus());
        }

        catch (SQLException se) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<Boolean> UpdateUser(String sessionId, User user, Context context) {
        try {
            int userId = UserAuthenticate(sessionId, context).getOrThrow();
            String updateUserStatement = "UPDATE users SET username = ?, fname = ?, lname = ?, mobile = ?, notify = ? WHERE id = ?";
            PreparedStatement updateUser = this.connection.prepareStatement(updateUserStatement);
            updateUser.setString(1, user.getUsername());
            updateUser.setString(2, user.getFirstName());
            updateUser.setString(3, user.getLastName());
            updateUser.setString(4, user.getMobile());
            updateUser.setInt(5, user.getNotify());
            updateUser.setInt(6, userId);
            if (updateUser.executeUpdate() != 1) {
                return Result.fail(FAILURE.SERVER_ERROR);
            }

            return Result.success(true);
        }

        catch (FailiureException e) {
            return Result.fail(e.getStatus());
        }

        catch (SQLException se) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }

    public Result<Boolean> CheckUsernameExists(String username, Context context) {
        try {
            String selectUsernameStatement = "SELECT id FROM users WHERE username = ?";
            PreparedStatement selectUsername = this.connection.prepareStatement(selectUsernameStatement);
            selectUsername.setString(1, username);
            ResultSet rs = selectUsername.executeQuery();
            if (rs.next()) {
                return Result.success(true);
            }

            return Result.success(false);
        }

        catch (SQLException e) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }
    }
}
