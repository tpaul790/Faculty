package org.example.restaurant.Observer;


public interface Observer<E extends Event> {
    void update(E event);
}
