package CloudPass.Infrastructure;

import com.google.gson.Gson;

import CloudPass.BusinessLogic.Queue;
import CloudPass.BusinessLogic.model.QueueMessage;
import CloudPass.Utils.model.Result;
import CloudPass.Utils.model.Status.FAILURE;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

public class QueueImpl implements Queue {
    private final SqsClient sqs;
    private final String queueUrl;
    private final Gson gson;

    public QueueImpl(SqsClient sqs, String queueUrl, Gson gson) {
        this.sqs = sqs;
        this.queueUrl = queueUrl;
        this.gson = gson;
    }

    public Result<Boolean> DispatchMessage(QueueMessage queueMessage) {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(gson.toJson(queueMessage))
                    .build();

            SendMessageResponse sendMessageResponse = this.sqs.sendMessage(sendMessageRequest);

            return Result.success(sendMessageResponse.sdkHttpResponse().isSuccessful());
        }

        catch (SqsException exception) {
            return Result.fail(FAILURE.SERVER_ERROR);
        }

    }
}
