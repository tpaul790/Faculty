package org.example.template.Domain;

public class FiltruDto {
    private int centruId;
    private Type tip;

    public FiltruDto(int id, Type tip) {
        this.centruId = id;
        this.tip = tip;
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
}
