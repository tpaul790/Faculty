package org.example.seminar14.Domain;

import java.time.LocalDateTime;

public class Pontaj {
    private int idAngajat;
    private int idSarcina;
    private LocalDateTime data;

    public Pontaj(int idAngajat, int idSarcina, LocalDateTime data) {
        this.idAngajat = idAngajat;
        this.idSarcina = idSarcina;
        this.data = data;
    }

    public int getIdAngajat() {
        return idAngajat;
    }

    public void setIdAngajat(int idAngajat) {
        this.idAngajat = idAngajat;
    }

    public int getIdSarcina() {
        return idSarcina;
    }

    public void setIdSarcina(int idSarcina) {
        this.idSarcina = idSarcina;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
