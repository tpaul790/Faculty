package org.example.template.Utils.Events;

import org.example.template.Domain.Nevoie;

public class ChangeEvent implements Event {
    private EventType eventType;
    private Nevoie data;

    public ChangeEvent(EventType eventType, Nevoie data) {
        this.eventType = eventType;
        this.data = data;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Nevoie getData() {
        return data;
    }
}
