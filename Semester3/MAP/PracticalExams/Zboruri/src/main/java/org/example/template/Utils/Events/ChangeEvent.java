package org.example.template.Utils.Events;

import org.example.template.Domain.Flight;

public class ChangeEvent implements Event {
    private EventType eventType;
    private long flight;

    public ChangeEvent(EventType eventType, long flight) {
        this.eventType = eventType;
        this.flight = flight;
    }

    public EventType getEventType() {
        return eventType;
    }

    public long getFlight() {
        return flight;
    }
}
