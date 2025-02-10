package org.example.seminar14.Domain;

public class Sarcina {
    private int id;
    private Dificultate dificultate;
    private int nrOreEstimate;

    public Sarcina(Dificultate dificultate, int nrOreEstimate) {
        this.dificultate = dificultate;
        this.nrOreEstimate = nrOreEstimate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dificultate getDificultate() {
        return dificultate;
    }

    public void setDificultate(Dificultate dificultate) {
        this.dificultate = dificultate;
    }

    public int getNrOreEstimate() {
        return nrOreEstimate;
    }

    public void setNrOreEstimate(int nrOreEstimate) {
        this.nrOreEstimate = nrOreEstimate;
    }
}
