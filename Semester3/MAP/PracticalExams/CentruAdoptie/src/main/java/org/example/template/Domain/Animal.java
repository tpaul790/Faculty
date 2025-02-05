package org.example.template.Domain;

public class Animal extends Entity<Integer> {
    private String nume;
    private int centruId;
    private Type tip;

    public Animal(String nume, int centruId, Type tip) {
        this.nume = nume;
        this.centruId = centruId;
        this.tip = tip;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getCentruId() {
        return centruId;
    }

    public void setCentruId(int centruId) {
        this.centruId = centruId;
    }

    public Type getTip() {
        return tip;
    }

    public void setTip(Type tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return nume+" | "+tip;
    }
}
