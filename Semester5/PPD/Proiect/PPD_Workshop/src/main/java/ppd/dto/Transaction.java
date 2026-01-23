package ppd.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Transaction {
    private String id;
    private String reservationId;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(String reservationId, TransactionType type, double amount) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.reservationId = reservationId;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%.2f,%s", id, reservationId, type, amount, timestamp);
    }
}

