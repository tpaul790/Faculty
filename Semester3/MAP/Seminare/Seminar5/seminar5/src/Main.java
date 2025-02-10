import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private List<Student> studentList = new ArrayList<>();
    private List<Tema> temaList = new ArrayList<>();

    public static void main(String[] args) {
        List<Nota> note = getNote(getStudents(), getTeme());
//        report1(note,"");
//        report2(note,"");
        report3(note,221);
    }


    public static void report1(List<Nota> note, String string) {
        Map<Student, List<Nota>> studentsGrades = note
                .stream()
                .collect(Collectors.groupingBy(Nota::getStudent));
        studentsGrades.entrySet()
                .stream()
                .filter(x -> x.getKey().getName().contains(string))
                .sorted((o1, o2) -> {
                    double avg1 = average(o1.getValue());
                    double avg2 = average(o2.getValue());
                    if (avg1 > avg2) return -1;
                    else return 0;
                })
                .forEach(x -> System.out.println(x.getKey().getName() + "; media: " + average(x.getValue())));
    }

    public static void report2(List<Nota> note, String string) {
        Map<String, List<Nota>> profersorGrades = note
                .stream()
                .collect(Collectors.groupingBy(Nota::getProfesor));
        profersorGrades.entrySet()
                .stream()
                .filter(x-> x.getKey().contains(string))
                .sorted((o1,o2) ->{
                    double avg1  = average(o1.getValue());
                    double avg2 = average(o2.getValue());
                    return Double.compare(avg2,avg1);
                })
                .forEach(x -> System.out.println(x.getKey()+ "; media: " + average(x.getValue())));

    }

    public static void report3(List<Nota> note, int group) {
        Map<Tema,List<Nota>> noteGrup = note.stream()
                .filter(x -> x.getStudent().getGroup() == group)
                .collect(Collectors.groupingBy(Nota::getTema));
        noteGrup.entrySet()
                .stream()
                .sorted((o1,o2) -> {
                    int s1 = o1.getValue().size();
                    int s2 = o2.getValue().size();
                    return Integer.compare(s2,s1);
                })
                .forEach(x -> System.out.println(x.getKey().getId()+ "; nrNote: " + x.getValue().size()));
    }

    public static double average(List<Nota> notaList) {
        double sum = notaList .stream()
                .map(Nota::getValue)
                .reduce(0d, Double::sum);
        return sum/notaList.size();
    }


    private static List<Student> getStudents() {
        Student s1 = new Student(1L,"andrei", 221);
        Student s2 = new Student(2L,"dan", 222);
        Student s3 = new Student(3L,"gigi", 221);
        Student s4 = new Student(4L,"costel", 222);
        return List.of(s1, s2, s3, s4);
    }

    private static List<Tema> getTeme() {
        return List.of(
                new Tema("t1", "desc1"),
                new Tema("t2", "desc2"),
                new Tema("t3", "desc3"),
                new Tema("t4", "desc4")
        );
    }

    private static List<Nota> getNote(List<Student> stud, List<Tema> teme) {
        return List.of(
                new Nota(stud.get(0), teme.get(0), 10d, LocalDate.of(2019, 11, 2), "profesor1"),
                new Nota(stud.get(1), teme.get(0), 9d, LocalDate.of(2019, 11, 2).minusWeeks(1), "profesor1"),
                new Nota(stud.get(1), teme.get(1), 10d, LocalDate.of(2019, 10, 20), "profesor2"),
                new Nota(stud.get(1), teme.get(2), 10d, LocalDate.of(2019, 10, 20), "profesor2"),
                new Nota(stud.get(2), teme.get(3), 7d, LocalDate.of(2019, 10, 28), "profesor1"),
                new Nota(stud.get(2), teme.get(3), 9d, LocalDate.of(2019, 10, 27), "profesor2"),
                new Nota(stud.get(1), teme.get(3), 10d, LocalDate.of(2019, 10, 29), "profesor2")
        );
    }
}