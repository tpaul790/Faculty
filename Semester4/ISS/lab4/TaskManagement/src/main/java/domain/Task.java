package domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tasks")
public class Task implements Identity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "assignedEmployeeId")
    private Employee assignedEmployee;
    @Column(name = "createTime")
    @Convert(converter = LocalDateTimeToStringConverter.class)
    private LocalDateTime createTime;
    @Column(name = "solveTime")
    @Convert(converter = LocalDateTimeToStringConverter.class)
    private LocalDateTime solveTime;

    public Task() {}

    public Task(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Employee getAssignetEmployee() {
        return assignedEmployee;
    }

    public void setAssignetEmployee(Employee assignetEmployee) {
        this.assignedEmployee = assignetEmployee;
    }

    public LocalDateTime getSolveTime() {
        return solveTime;
    }

    public void setSolveTime(LocalDateTime solveTime) {
        this.solveTime = solveTime;
    }

    @Override
    public String toString() {
        return "Task: " + description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        id = integer;
    }
}
