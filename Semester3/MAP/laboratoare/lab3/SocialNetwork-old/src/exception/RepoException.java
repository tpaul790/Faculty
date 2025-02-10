package exception;

public class RepoException extends Throwable {
    private String msg;

    public RepoException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
