package com.example.socialnetworkgui.domain;

public class Pageable {
    private int page, pageSize;

    public Pageable(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}
