package ppd.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Reservation implements Serializable {
    private String id;
    private String clientName;
    private String cnp;
    private int centerId;
    private int workshopId;
    private int hour;
    private volatile ReservationStatus status;
    private LocalDateTime creationTimestamp;
    private double cost;

    public Reservation(String clientName, String cnp, int centerId, int workshopId, int hour, double cost) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.clientName = clientName;
        this.cnp = cnp;
        this.centerId = centerId;
        this.workshopId = workshopId;
        this.hour = hour;
        this.cost = cost;
        this.status = ReservationStatus.PENDING;
        this.creationTimestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%d,%d,%s,%s",
                id, clientName, cnp, centerId, workshopId, hour, status, creationTimestamp);
    }
}