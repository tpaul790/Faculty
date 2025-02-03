package org.example.restaurant.Observer;

import org.example.restaurant.Domain.Entitati.OrderDto;

public class OrderEvent implements Event {
    private OrderEventType eventType;
    private OrderDto data;

    public OrderEvent(OrderEventType eventType, OrderDto data) {
        this.eventType = eventType;
        this.data = data;
    }

    public OrderEventType getEventType() {
        return eventType;
    }

    public void setEventType(OrderEventType eventType) {
        this.eventType = eventType;
    }

   public OrderDto getData() {
        return data;
   }

   public void setData(OrderDto data) {
        this.data = data;
   }
}
