package com.example.socialnetworkgui.domain;

import java.util.Objects;

public class Tuple<E1, E2> {
    private E1 first;
    private E2 second;

    public Tuple(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

    public E1 getFirst() {
        return first;
    }

    public E2 getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Tuple tuple = (Tuple) o;
        return tuple.first.equals(first) && tuple.second.equals(second) || tuple.first.equals(second) && tuple.second.equals(first);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return first.toString()+","+second.toString();
    }
}
