package CloudPass.Utils.model;

import CloudPass.Utils.model.Status.FAILURE;
import CloudPass.Utils.model.Status.SUCCESS;

public class Result<T> {
    public interface SuccessCallback<T> {
        Result<T> call(T t);
    }

    public interface FailCallback {
        <N> Result<N> call(Status status);
    }

    private final T resultObject;
    private final Status status;

    private Result(T resultObject, Status status) {
        this.resultObject = resultObject;
        this.status = status;
    }

    private Result(Status status) {
        this.resultObject = null;
        this.status = status;
    }

    public T get() {
        return this.resultObject;
    }

    public Status getStatus() {
        return this.status;
    }

    public T getOrThrow() throws FailiureException {
        if (status == SUCCESS.SUCCESS) {
            return this.resultObject;
        }

        throw new FailiureException(this.status);
    }

    public void getOrFail(SuccessCallback<T> success, FailCallback fail) {
        if (status == SUCCESS.SUCCESS) {
            success.call(this.resultObject);
        }

        fail.call(this.status);
    }

    public Status status() {
        return this.status;
    }

    public static <T> Result<T> success(T resultObject) {
        return new Result<T>(resultObject, SUCCESS.SUCCESS);
    }

    public static <T> Result<T> fail(Status status) {
        return new Result<T>(status);
    }
    
    public static int getStatusAsHttpCode(Status status) {
        if (status == SUCCESS.SUCCESS) {
            return 200;
        }

        if (status == FAILURE.NOT_FOUND) {
            return 404;
        }

        if (status == FAILURE.UNAUTHORIZED) {
            return 403;
        }

        return 500;
    }

}
