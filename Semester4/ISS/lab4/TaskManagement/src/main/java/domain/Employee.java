package domain;

public class Employee extends Entity<Integer> {
    private String name;
    private String username;
    private String password;
    private EmployeeType type;

    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.type = EmployeeType.EMPLOYEE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }
}
