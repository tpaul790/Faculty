import java.time.LocalDate;

public class Nota {
    private Student student;
    private Tema tema;
    private double value;
    private String profesor;
    private LocalDate date;

    public Nota(Student student, Tema tema, double value, LocalDate localDate, String profesor) {
        this.student = student;
        this.tema = tema;
        this.value = value;
        this.profesor = profesor;
        this.date = localDate;
    }

    public Student getStudent() {
        return student;
    }

    public Tema getTema() {
        return tema;
    }

    public double getValue() {
        return value;
    }

    public String getProfesor() {
        return profesor;
    }

    public LocalDate getDate() {
        return date;
    }
}
