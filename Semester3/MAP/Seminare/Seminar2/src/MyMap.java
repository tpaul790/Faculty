import java.util.*;

public class MyMap {
    private final Map<Integer, List<Student>> map;

    public MyMap (){
        map = new TreeMap<>(new StudentGradeComparator());
    }

    public static class StudentGradeComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public void addStudent(Student s)
    {
        int grade = Math.round(s.getGrade());
        List<Student> list = map.get(grade);
        if (list == null)
        {
            list = new ArrayList<>();
            map.put(grade, list);
        }
        list.add(s);
    }

    public Set<Map.Entry<Integer, List<Student>>> getEntries() {
        return  map.entrySet();
    }
}
