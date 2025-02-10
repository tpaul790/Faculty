package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Integer, Integer>> {
    private LocalDate createDate;
    private LocalDate acceptDate;
    private String status;

    public Friendship(Tuple<Integer, Integer> id, LocalDate createDate, LocalDate acceptDate, String status) {
        super.setId(id);
        this.status = status;
        this.createDate = createDate;
        this.acceptDate = acceptDate;
    }

    public Friendship(Tuple<Integer, Integer> id, String status) {
        super.setId(id);
        this.createDate = LocalDate.now();
        this.acceptDate = null;
        this.status = status;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(LocalDate acceptDate) {
        this.acceptDate = acceptDate;
    }

    @Override
    public String toString() {
        return "UserId1= " + getId().getFirst() + " - UserId2= " +getId().getSecond();
    }
}
