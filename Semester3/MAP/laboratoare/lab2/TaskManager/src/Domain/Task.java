package Domain;

import java.util.Objects;

public abstract class Task {
    private String taskId;
    private String description;

    public Task(String taskId, String description){
        this.taskId = taskId;
        this.description = description;
    }

    public abstract void execute();

    public String getTaskId(){
        return taskId;
    }

    public String getDescription(){
        return description;
    }

    public void setTaskId(String taskId){
        this.taskId = taskId;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return "Task: " + taskId + " | Description: " + description;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) {
            return true;
        }
        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return Objects.equals(taskId, task.taskId) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode(){
        return Objects.hash(taskId, description);
    }


}
