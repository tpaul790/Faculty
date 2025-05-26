package utils.events;

import domain.Employee;
import domain.Task;

public class ChangeEvent implements Event {
    private EventType eventType;
    private Task task;
    private Employee employee;

    public ChangeEvent(EventType eventType, Task task) {
        this.eventType = eventType;
        this.task = task;
    }

    public ChangeEvent(EventType eventType, Employee employee) {
        this.eventType = eventType;
        this.employee = employee;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
