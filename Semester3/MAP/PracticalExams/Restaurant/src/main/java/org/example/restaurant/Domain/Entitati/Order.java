package org.example.restaurant.Domain.Entitati;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class Order implements Comparable<Order> {
    private int id;
    private int table;
    private List<MenuItem> menuItems;
    private Status orderStatus;
    private LocalDateTime orderDate;

    public Order(int id, int table, Status orderStatus, LocalDateTime orderDate) {
        this.id = id;
        this.table = table;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public static Status statusFromString(String statusString) {
        return switch (statusString) {
            case "PLACED" -> Status.PLACED;
            case "PREPARING" -> Status.PREPARING;
            case "SERVED" -> Status.SERVED;
            default -> null;
        };
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public int compareTo(@NotNull Order o) {
        return this.orderDate.compareTo(o.orderDate);
    }
}
