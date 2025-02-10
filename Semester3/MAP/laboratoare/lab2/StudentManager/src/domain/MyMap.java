package domain;

import java.util.*;

public class MyMap {
    private TreeMap<Integer, List<Student>> students;

    public MyMap() {
        students = new TreeMap<>(new StudentGradeComparator());
    }

    private static class StudentGradeComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1-o2;
        }
    }

    public void add(Student student) {
        Integer great = Math.round(student.getMedia());
        List<Student> studentList = students.get(great);
        if(studentList == null) {
            studentList = new ArrayList<>();
            studentList.add(student);
            students.put(great, studentList);
        }else{
            studentList.add(student);
        }
    }

    public Set<Map.Entry<Integer, List<Student>>> getEntrys() {
        return students.entrySet();
    }

}
