import domain.MyMap;
import domain.Student;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Student student1 = new Student("Dan", 4.5f);
        Student student2 = new Student("Ana", 8.5f);
        Student student3 = new Student("Dan", 4.5f);

        HashSet<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);

//        for(Student student : students) {
//            System.out.println(student.toString());
//        }

        Set<Student> treeSet = new TreeSet<>();
        treeSet.add(student1);
        treeSet.add(student2);
        treeSet.add(student3);

//        for(Student student : treeSet) {
//            System.out.println(student.toString());
//        }

        HashMap<String, Student> studentMap = new HashMap<>();
        studentMap.put("Dan", student1);
        studentMap.put("Ana", student2);
        studentMap.put("Dan", student3);

//        for(String key: studentMap.keySet()){
//            System.out.println(key);
//        }

//        Map<String,Student> treeMap = new TreeMap<>((o1,o2)->{
//            return o1.getNume().compareTo(o2.getNume());
//        });
//        treeMap.put("Dan",student1);
//        treeMap.put("Ana",student2);
//        treeMap.put("Dan",student3);
//
//        for(Map.Entry<String,Student> entry: treeMap.entrySet()){
//            System.out.println(entry);

        MyMap myMap = new MyMap();
        List<Student> studentList1 = getList();
        for(Student student : studentList1) {
            myMap.add(student);
        }

        for (Map.Entry<Integer, List<Student>> entry : myMap.getEntrys()) {
            System.out.println(entry.getKey());
            List<Student> studentList = entry.getValue();
            Collections.sort(studentList);
            for (Student student : studentList) {
                System.out.println(student);
            }
        }
    }

    public static List<Student> getList() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("1", 4.5f));
        list.add(new Student("2", 8.5f));
        list.add(new Student("3", 4.5f));
        list.add(new Student("4", 4.5f));
        list.add(new Student("5", 4.5f));
        list.add(new Student("6", 4.5f));
        return list;
    }
}