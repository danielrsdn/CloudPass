package CloudPass.Utils.model;

public interface Status {
  
    public enum FAILURE implements Status {
        UNAUTHORIZED, FORBIDDEN, SERVER_ERROR, NOT_FOUND;
    }

    public enum SUCCESS implements Status {
        SUCCESS;
    }
}
