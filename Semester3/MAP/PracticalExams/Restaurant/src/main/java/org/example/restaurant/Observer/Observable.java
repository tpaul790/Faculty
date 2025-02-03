package org.example.restaurant.Observer;


public interface Observable<E extends Event> {
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notify(E event);
}
