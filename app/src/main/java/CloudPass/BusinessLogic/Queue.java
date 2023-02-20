package CloudPass.BusinessLogic;

import CloudPass.BusinessLogic.model.QueueMessage;
import CloudPass.Utils.model.Result;

public interface Queue {

    public Result<Boolean> DispatchMessage(QueueMessage queueMessage);
}
