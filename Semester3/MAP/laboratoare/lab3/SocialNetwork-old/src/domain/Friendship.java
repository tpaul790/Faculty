package domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Friendship {
    private User user1;
    private User user2;
    private String status;
    private final LocalDateTime createTime;
    private LocalDateTime acceptTime;

    public Friendship(User user1, User user2, String status) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.createTime = LocalDateTime.now();
        this.acceptTime = null;
    }

    public void accept() {
        this.status = "accepted";
        this.acceptTime = LocalDateTime.now();
    }

    public void decline() {
        this.status = "declined";
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "UserId1: " + user1.getId() + ", UserId2: " + user2.getId() + ", Status: " + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship other = (Friendship) o;
        return this.user1.equals(other.user1) && this.user2.equals(other.user2) && this.status.equals(other.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2, status);
    }
}
