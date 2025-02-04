package org.example.template.Utils.Observer;


import org.example.template.Utils.Events.Event;

public interface Observer<E extends Event> {
    void update(E event);
}
