import jdk.jshell.execution.Util;

import java.util.*;

public class Main {
    public static List<Student> getList(){
        List<Student> l=new ArrayList<Student>();
        l.add(new Student("1",9.7f));
        l.add(new Student("2",7.3f));
        l.add(new Student("3",6f));
        l.add(new Student("4",6.9f));
        l.add(new Student("5",9.5f));
        l.add(new Student("6",9.9f));
        return l;
    }

    public static void main(String[] args) {
        Student s1 = new Student("Dan", 4.5f);
        Student s2 = new Student("Ana", 8.5f);
        Student s3 = new Student("Dan", 4.5f);

        Set<Student> students = new HashSet<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        for (Student s : students) {
            System.out.println(s);
        }

        System.out.println(s1.equals(s3));
        System.out.println(students.contains(new Student("Dan", 4.5f)));

        Set<Student> students2 = new TreeSet<>();
        students2.add(s1);
        students2.add(s2);
        students2.add(s3);
        for (Student s : students2) {
            System.out.println(s);
        }

        Set<Student> students3 = new TreeSet<>((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });

        students3.add(s1);
        students3.add(s2);
        students3.add(s3);
        for (Student s : students3) {
            System.out.println(s);
        }

        // TreeMap sorts all its keys according to their natural ordering
        // Map<String, Student> studentMap = new HashMap<>();
        Map<String, Student> studentMap = new TreeMap<>();
        studentMap.put(s1.getName(), s1);
        studentMap.put(s2.getName(), s2);
        studentMap.put(s3.getName(), s3);
        for (Map.Entry<String, Student> s : studentMap.entrySet()) {
            System.out.println(s);
        }


        // MyMapInheritance map = new MyMapInheritance();
        MyMap map = new MyMap();
        List<Student> l = getList();
        for (Student s : l) {
            map.addStudent(s);
        }
        for (Map.Entry<Integer, List<Student>> studentListEntry : map.getEntries()) {
            System.out.println("Students with grade : " + studentListEntry.getKey());
            List<Student> studentsList = studentListEntry.getValue();
            // For ArrayList, you need to call Collections.sort() explicitly if you want the elements to be sorted, since elements are not automatically sorted as you insert them. They are sorted in their insertion order.
            // For TreeSet, the sorting is handled automatically, and you don't need to explicitly call a sorting method. The TreeSet maintains the sorted order as elements are added, since it's a collection that implements the SortedSet interface. When you add elements to a TreeSet, they are automatically sorted based on their natural order (if the elements implement the Comparable interface) or using a custom comparator if one is provided during the TreeSet instantiation.
            Collections.sort(studentsList);
            for (Student s : studentsList) {
                System.out.println(s);
            }
        }


        Utilizator u1=new Utilizator("u1FirstName", "u1LastName");
        u1.setId(2l);
        Utilizator u2=new Utilizator("u2FirstName", "u2LastName"); u2.setId(3l);
        Utilizator u3=new Utilizator("u3FirstName", "u3LastName"); u3.setId(4l);
        Utilizator u4=new Utilizator("u4FirstName", "u4LastName"); u4.setId(5l);
        Utilizator u5=new Utilizator("u5FirstName", "u5LastName"); u5.setId(6l);
        Utilizator u6=new Utilizator("u6FirstName", "u6LastName"); u6.setId(7l);
        Utilizator u7=new Utilizator("u7FirstName", "u7LastName"); u7.setId(8l);

        InMemoryRepository<Long, Utilizator> repo=new InMemoryRepository<>();
        repo.save(u1);
        repo.save(u2);
        repo.save(u3);
        repo.save(u4);
        repo.save(u5);
        repo.save(u6);
        repo.save(u7);

        Iterable<Utilizator> allElements = repo.findAll();
        for (Utilizator element : allElements) {
            System.out.println(element);
        }
    }
}