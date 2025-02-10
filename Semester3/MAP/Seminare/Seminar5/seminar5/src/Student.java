public class Student extends Entity<Long>{
    private String name;
    private int group;

    public Student(Long id,String name, int group) {
        super.setId(id);
        this.name = name;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public int getGroup() {
        return group;
    }
}
