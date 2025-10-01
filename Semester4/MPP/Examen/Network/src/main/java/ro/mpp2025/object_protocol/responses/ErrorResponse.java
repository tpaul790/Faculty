package ro.mpp2025.object_protocol.responses;


public class ErrorResponse implements Response {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
