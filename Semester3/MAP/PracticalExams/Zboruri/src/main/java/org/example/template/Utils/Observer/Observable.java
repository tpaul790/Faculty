package org.example.template.Utils.Observer;

import org.example.template.Utils.Events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notify(E event);
}
