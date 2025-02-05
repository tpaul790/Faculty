package org.example.template.Domain;

public class AdoptionCentre extends Entity<Integer> {
    private String nume;
    private String locatie;
    private int capacitate;

    public AdoptionCentre(String nume, String locatie, int capacitate) {
        this.nume = nume;
        this.locatie = locatie;
        this.capacitate = capacitate;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }
}
