package ppd.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private RequestType type;
    private String clientName;
    private String cnp;
    private int centerId;
    private int workshopId;
    private int hour;
    private String reservationId;
    private double amount;

    //Reserve request
    public Request(String clientName, String cnp, int centerId, int workshopId, int hour) {
        this.type = RequestType.RESERVE;
        this.clientName = clientName;
        this.cnp = cnp;
        this.centerId = centerId;
        this.workshopId = workshopId;
        this.hour = hour;
    }

    //Pay or cancel requests
    public Request(RequestType type, String reservationId, double amount) {
        this.type = type;
        this.reservationId = reservationId;
        this.amount = amount;
    }

}
