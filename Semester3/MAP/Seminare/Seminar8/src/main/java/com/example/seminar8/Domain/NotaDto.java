package com.example.seminar8.Domain;

public class NotaDto {
    private String numeStudent;
    private String temaId;
    private double nota;
    private String profesor;

    public NotaDto(String numeStudent, String temaId, double nota, String profesor) {
        this.numeStudent = numeStudent;
        this.temaId = temaId;
        this.nota = nota;
        this.profesor = profesor;
    }
    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public String getTemaId() {
        return temaId;
    }

    public void setTemaId(String temaId) {
        this.temaId = temaId;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}
