package domainTests;

import domain.Employee;
import domain.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testConstructorAndGetters() {
        Task task = new Task("Finish project");
        assertEquals("Finish project", task.getDescription());
        assertNull(task.getId());
        assertNull(task.getAssignetEmployee());
        assertNull(task.getCreateTime());
        assertNull(task.getSolveTime());
    }

    @Test
    void testSettersAndGetters() {
        Task task = new Task();
        task.setId(5);
        task.setDescription("New task description");

        Employee employee = new Employee("Alice", "alice01", "pass123");
        employee.setId(1);
        task.setAssignetEmployee(employee);

        LocalDateTime createTime = LocalDateTime.of(2024, 5, 15, 10, 0);
        LocalDateTime solveTime = LocalDateTime.of(2024, 5, 16, 10, 0);
        task.setCreateTime(createTime);
        task.setSolveTime(solveTime);

        assertEquals(5, task.getId());
        assertEquals("New task description", task.getDescription());
        assertEquals(employee, task.getAssignetEmployee());
        assertEquals(createTime, task.getCreateTime());
        assertEquals(solveTime, task.getSolveTime());
    }
}