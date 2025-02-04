package org.example.template.Domain;

import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;

public class Person extends Entity<Long>{
    private String nume;
    private String prenume;
    private String username;
    private String password;
    private Oras oras;
    private String strada;
    private String numar;
    private String telefon;

    public Person(Long id, String nume, String prenume, String username, String password, Oras oras, String strada, String numar, String telefon) {
        super(id);
        this.nume = nume;
        this.prenume = prenume;
        this.username = username;
        this.password = password;
        this.oras = oras;
        this.strada = strada;
        this.numar = numar;
        this.telefon = telefon;
    }

    public Person(String nume, String prenume, String username, String password, Oras oras, String strada, String numar, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.username = username;
        this.password = password;
        this.oras = oras;
        this.strada = strada;
        this.numar = numar;
        this.telefon = telefon;
    }

    public static Oras orasFromString(String oras) {
        switch (oras) {
            case "Bistrita" -> {
                return Oras.Bistrita;
            }
            case "Bucuresti" -> {
                return Oras.Bucuresti;
            }
            case "Cluj" -> {
                return Oras.Cluj;
            }
            case "Bacau" -> {
                return Oras.Bacau;
            }
        }
        return null;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Oras getOras() {
        return oras;
    }

    public void setOras(Oras oras) {
        this.oras = oras;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStrada() {
        return strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public String getNumar() {
        return numar;
    }

    public void setNumar(String numar) {
        this.numar = numar;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return nume+" | "+prenume+" | "+username+" | "+oras+" | "+strada;
    }
}
