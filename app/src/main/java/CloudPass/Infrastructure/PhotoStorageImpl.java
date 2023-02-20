package CloudPass.Infrastructure;

import java.time.Duration;

import CloudPass.BusinessLogic.PhotoStorage;
import CloudPass.BusinessLogic.model.UploadPhoto;
import CloudPass.Utils.model.Result;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.GetObjectAttributesRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectAttributes;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

public class PhotoStorageImpl implements PhotoStorage {
        private final S3Client s3;
        private final S3Presigner presigner;
        private final String bucket;

        public PhotoStorageImpl(S3Client s3, S3Presigner presigner, String bucket) {
                this.s3 = s3;
                this.presigner = presigner;
                this.bucket = bucket;
        }

        public Result<String> getPhoto(String name) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                .bucket(bucket)
                                .key(name)
                                .build();

                GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                                .signatureDuration(Duration.ofHours(1))
                                .getObjectRequest(getObjectRequest)
                                .build();

                PresignedGetObjectRequest presignedGetObjectRequest = presigner
                                .presignGetObject(getObjectPresignRequest);

                return Result.success(presignedGetObjectRequest.url().toString());
        }

        public Result<Boolean> checkPhotoUploaded(String name) {
                GetObjectAttributesRequest getObjectAttributesRequest = GetObjectAttributesRequest.builder()
                                .bucket(bucket)
                                .key(name)
                                .objectAttributes(ObjectAttributes.OBJECT_SIZE)
                                .build();

                return Result.success(this.s3.getObjectAttributes(getObjectAttributesRequest).sdkHttpResponse()
                                .statusCode() == 200);
        }

        public Result<UploadPhoto> getPhotoUpload(String key) {
                PresignedPutObjectRequest presignedPutObjectRequest = presigner
                                .presignPutObject(r -> r.signatureDuration(Duration.ofMinutes(10))
                                                .putObjectRequest(por -> por.bucket(bucket).key(key)));

                return Result.success(
                                new UploadPhoto(
                                                presignedPutObjectRequest.url().toString(),
                                                presignedPutObjectRequest.httpRequest().method().toString(),
                                                presignedPutObjectRequest.signedHeaders()));
        }

}
