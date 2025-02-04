package org.example.template.Domain;

import java.time.LocalDateTime;

public class Nevoie extends Entity<Long>{
    private String titlu;
    private String descriere;
    private LocalDateTime deadline;
    private Long omInNevoi;
    private Long omSalvator;
    private Status status;

    public Nevoie(Long id, String titlu, String descriere, LocalDateTime deadline, Long omInNevoi) {
        super(id);
        this.titlu = titlu;
        this.descriere = descriere;
        this.deadline = deadline;
        this.omInNevoi = omInNevoi;
        this.omSalvator = null;
        this.status = Status.CAUT_EROU;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Long getOmInNevoi() {
        return omInNevoi;
    }

    public void setOmInNevoi(Long omInNevoi) {
        this.omInNevoi = omInNevoi;
    }

    public Long getOmSalvator() {
        return omSalvator;
    }

    public void setOmSalvator(Long omSalvator) {
        this.omSalvator = omSalvator;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
