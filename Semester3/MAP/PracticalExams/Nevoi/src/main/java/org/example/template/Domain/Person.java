package org.example.template.Domain;

public class Person extends Entity<Long>{
    private String nume;
    private String prenume;
    private String username;
    private String password;
    private String oras;
    private String strada;
    private String numar;
    private String telefon;

    public Person(Long id, String nume, String prenume, String username, String password, String oras, String strada, String numar, String telefon) {
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

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
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
}
