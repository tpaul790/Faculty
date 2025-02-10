package org.example.seminar14.Domain;

public class Angajat {
    private int id;
    private String name;
    private double venitPeOra;
    private Nivel senioritate;

    public Angajat(String name, double venitPeOra, Nivel senioritate) {
        this.name = name;
        this.venitPeOra = venitPeOra;
        this.senioritate = senioritate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVenitPeOra() {
        return venitPeOra;
    }

    public void setVenitPeOra(double venitPeOra) {
        this.venitPeOra = venitPeOra;
    }

    public Nivel getSenioritate() {
        return senioritate;
    }

    public void setSenioritate(Nivel senioritate) {
        this.senioritate = senioritate;
    }
}
