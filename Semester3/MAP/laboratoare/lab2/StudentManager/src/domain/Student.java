package domain;

import java.util.Objects;

public class Student implements Comparable<Student> {
    private String nume;
    private float media;

    public Student(String nume, float media) {
        this.nume = nume;
        this.media = media;
    }

    public String getNume() {
        return nume;
    }

    public float getMedia() {
        return media;
    }

    @Override
    public String toString(){
        return "Nume: " + nume + " Media: " + media;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        Student student = (Student) obj;
        return Float.compare(student.media, this.media) == 0 && Objects.equals(this.nume, student.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume, media);
    }

    @Override
    public int compareTo(Student o) {
        return this.nume.compareTo(o.nume);
    }
}
