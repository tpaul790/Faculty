package com.example.seminar8.Domain;

import java.time.LocalDateTime;

public class Nota {
    private Student student;
    private Tema tema;
    private String professor;
    private double value;
    private LocalDateTime dateTime;

    public Nota(Student student, Tema tema, String professor, double value) {
        this.student = student;
        this.tema = tema;
        this.professor = professor;
        this.value = value;
        this.dateTime = LocalDateTime.now();
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return student.toString() + " " + tema.toString() + " " + professor + " " + value;
    }

}
