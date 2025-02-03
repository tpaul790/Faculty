package org.example.restaurant.Domain.Entitati.Validatori;

import org.example.restaurant.Domain.Entitati.MenuItem;
import org.example.restaurant.Domain.Entitati.Order;

public class OrderValidator {
    private static OrderValidator instance = new OrderValidator();

    private OrderValidator() {}

    public static OrderValidator getInstance() {
        if(instance == null) {
            instance = new OrderValidator();
        }
        return instance;
    }

    public boolean validate(Order order) {
        boolean valid = true;
        if(order.getId() < 1)
            valid = false;
        if(order.getTable() < 1)
            valid = false;
        if(order.getOrderStatus() == null)
            valid = false;
        return valid;
    }
}
