package com.example.scheletseminar9.domain;

public class Page <E extends Entity> {
    private Iterable<E> entities;
    private int pageSize;

    public Page(Iterable<E> entities, int pageSize) {
        this.entities = entities;
        this.pageSize = pageSize;
    }

    public Iterable<E> elementsOnPage() {
        return entities;
    }

    public int totalNumberOfElements() {
        return this.pageSize;
    }

}
