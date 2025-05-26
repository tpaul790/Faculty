package domainTests;
import domain.Employee;
import domain.Task;
import domain.validator.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskValidatorTest {

    private TaskValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TaskValidator();
    }

    private Task createValidTask() {
        Employee emp = new Employee("Alice", "alice01", "securePass");
        emp.setId(1);
        Task task = new Task("This is a valid description");
        task.setAssignetEmployee(emp);
        task.setCreateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        task.setSolveTime(LocalDateTime.of(2024, 1, 2, 10, 0));
        return task;
    }

    @Test
    void testValidTask() {
        Task task = createValidTask();
        String errors = validator.validate(task);
        assertTrue(errors.isBlank(), "Expected no validation errors");
    }

    @Test
    void testEmptyDescription() {
        Task task = createValidTask();
        task.setDescription("");
        String errors = validator.validate(task);
        assertTrue(errors.contains("Description is empty"));
    }

    @Test
    void testShortDescription() {
        Task task = createValidTask();
        task.setDescription("Too short");
        String errors = validator.validate(task);
        assertTrue(errors.contains("Description should be more than 10 characters"));
    }

    @Test
    void testNullDescription() {
        Task task = createValidTask();
        task.setDescription(null);
        String errors = validator.validate(task);
        assertFalse(errors.contains("Description"));
    }

    @Test
    void testNullEmployee() {
        Task task = createValidTask();
        task.setAssignetEmployee(null);
        String errors = validator.validate(task);
        assertFalse(errors.contains("assigned employee"));
    }

    @Test
    void testNegativeEmployeeId() {
        Employee emp = new Employee("Bob", "bob", "pass");
        emp.setId(-5);
        Task task = createValidTask();
        task.setAssignetEmployee(emp);
        String errors = validator.validate(task);
        assertTrue(errors.contains("The id of the assigned employee can not be negative"));
    }

    @Test
    void testSolveBeforeCreate() {
        Task task = createValidTask();
        task.setCreateTime(LocalDateTime.of(2024, 1, 3, 10, 0));
        task.setSolveTime(LocalDateTime.of(2024, 1, 2, 10, 0));
        String errors = validator.validate(task);
        assertTrue(errors.contains("The date of the solve time can not be before the created time"));
    }

    @Test
    void testNullDates() {
        Task task = createValidTask();
        task.setCreateTime(null);
        task.setSolveTime(null);
        String errors = validator.validate(task);
        assertFalse(errors.contains("solve time"));
    }
}