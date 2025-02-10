package com.example.seminar8.Domain;

public class Tema extends Entity<String> {
    private String desc;

    public Tema(String id, String desc) {
        setId(id);
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
