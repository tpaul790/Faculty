package domain;

import java.time.LocalDateTime;

public class Task extends Entity<Integer> {
    private String description;
    private Integer assignetEmployeeId;
    private LocalDateTime createTime;
    private LocalDateTime solveTime;

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

    public Integer getAssignetEmployeeId() {
        return assignetEmployeeId;
    }

    public void setAssignetEmployeeId(Integer assignetEmployeeId) {
        this.assignetEmployeeId = assignetEmployeeId;
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
}
