package com.example.socialnetworkgui.utils.observer;

import com.example.socialnetworkgui.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notify(E event);
}
