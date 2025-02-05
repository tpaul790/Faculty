package org.example.template.Domain;

public class RequestDto {
    private int idAnimal;
    private String numeAnimal;
    private String numeCentru;
    private int idCentru;

    public RequestDto(){}

    public String getNumeCentru() {
        return numeCentru;
    }

    public void setNumeCentru(String numeCentru) {
        this.numeCentru = numeCentru;
    }

    public String getNumeAnimal() {
        return numeAnimal;
    }

    public void setNumeAnimal(String numeAnimal) {
        this.numeAnimal = numeAnimal;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public int getIdCentru() {
        return idCentru;
    }

    public void setIdCentru(int idCentru) {
        this.idCentru = idCentru;
    }
}
