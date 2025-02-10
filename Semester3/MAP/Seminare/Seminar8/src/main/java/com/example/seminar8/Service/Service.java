package com.example.seminar8.Service;

import com.example.seminar8.Domain.Nota;
import com.example.seminar8.Domain.NotaDto;
import com.example.seminar8.Domain.Student;
import com.example.seminar8.Domain.Tema;

import java.util.ArrayList;
import java.util.List;

public class Service {
    public List<Student> findAllStudents(){
        List<Student> students = new ArrayList<>();
        Student student1 = new Student("Paul", 222);
        Student student2 = new Student("Mary", 224);
        Student student3 = new Student("John", 217);
        Student student4 = new Student("Jane", 223);
        Student student5 = new Student("Jack", 221);
        Student student6 = new Student("Jill", 225);
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
        students.add(student6);
        return students;
    }

    public List<Tema> findAllTeme(){
        List<Tema> teme = new ArrayList<>();
        Tema tema1 = new Tema("t1", "analiza");
        Tema tema2 = new Tema("t2", "map");
        Tema tema3 = new Tema("t3", "plf");
        Tema tema4 = new Tema("t4", "so");
        Tema tema5 = new Tema("t5", "retele");
        teme.add(tema1);
        teme.add(tema2);
        teme.add(tema3);
        teme.add(tema4);
        teme.add(tema5);
        return teme;
    }

    public List<Nota> getNote(List<Student> students, List<Tema> teme){
        List<Nota> nota = new ArrayList<>();
        Nota nota1 = new Nota(students.get(0),teme.get(0), "Berinde", 6);
        Nota nota2 = new Nota(students.get(1),teme.get(1), "Berinde", 7);
        Nota nota3 = new Nota(students.get(2),teme.get(2), "Berinde", 3);
        Nota nota4 = new Nota(students.get(3),teme.get(3), "Berinde", 10);
        nota.add(nota1);
        nota.add(nota2);
        nota.add(nota3);
        nota.add(nota4);
        return nota;
    }

    public List<Nota> findAllNote(){
        return getNote(findAllStudents(), findAllTeme());
    }
}
