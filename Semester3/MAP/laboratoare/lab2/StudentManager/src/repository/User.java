package repository;

public class User {
    private String nume,prenume;

    public User(String nume, String prenume) {
        this.nume = nume;
        this.prenume = prenume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getNume() {
        return nume;
    }

    @Override
    public String toString() {
        return "User{" + "nume='" + nume + '\'' + ", prenume='" + prenume + '\'' + '}';
    }
}
