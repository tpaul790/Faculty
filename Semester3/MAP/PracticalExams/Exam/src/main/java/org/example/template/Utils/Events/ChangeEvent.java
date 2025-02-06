package org.example.template.Utils.Events;

import org.example.template.Domain.Show;

public class ChangeEvent implements Event {
    private EventType eventType;
    private Show data;
    public ChangeEvent(EventType eventType, Show data) {
        this.eventType = eventType;
    }
    public EventType getEventType() {
        return eventType;
    }

    public Show getData() {
        return data;
    }
}
