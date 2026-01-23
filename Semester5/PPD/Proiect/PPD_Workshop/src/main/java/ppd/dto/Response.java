package ppd.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private String reservationId;
    private double cost;

    public Response(boolean success, String message, String reservationId, double cost) {
        this.success = success;
        this.message = message;
        this.reservationId = reservationId;
        this.cost = cost;
    }
}
