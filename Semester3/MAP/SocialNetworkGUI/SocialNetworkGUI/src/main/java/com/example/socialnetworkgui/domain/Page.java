package com.example.socialnetworkgui.domain;

public class Page <E extends Entity> {
    private Iterable<E> entities;
    private int pageSize;

    public Page(Iterable<E> entities, int pageSize) {
        this.entities = entities;
        this.pageSize = pageSize;
    }

    public Iterable<E> getEntities() {
        return entities;
    }

    public void setEntities(Iterable<E> entities) {
        this.entities = entities;
    }

    public int getPageSize() {
        return pageSize;
    }
}
