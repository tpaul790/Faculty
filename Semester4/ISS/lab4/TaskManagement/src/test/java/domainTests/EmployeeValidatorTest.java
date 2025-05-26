package domainTests;
import domain.Employee;
import domain.Task;
import domain.validator.EmployeeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeValidatorTest {

    private EmployeeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmployeeValidator();
    }

    @Test
    void testValidEmployee() {
        Employee employee = new Employee("Alice", "alice01", "securePass");
        String errors = validator.validate(employee);
        assertTrue(errors.isBlank(), "Expected no validation errors");
    }

    @Test
    void testEmptyName() {
        Employee employee = new Employee("", "alice01", "securePass");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Name cannot be empty"));
    }

    @Test
    void testShortName() {
        Employee employee = new Employee("Al", "alice01", "securePass");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Name must be between 4 and 10 characters"));
    }

    @Test
    void testLongName() {
        Employee employee = new Employee("Alexandriana", "alice01", "securePass");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Name must be between 4 and 10 characters"));
    }

    @Test
    void testEmptyUsername() {
        Employee employee = new Employee("Alice", "", "securePass");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Username cannot be empty"));
    }

    @Test
    void testShortUsername() {
        Employee employee = new Employee("Alice", "al", "securePass");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Username must be between 4 and 10 characters"));
    }

    @Test
    void testLongUsername() {
        Employee employee = new Employee("Alice", "averylongusername", "securePass");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Username must be between 4 and 10 characters"));
    }

    @Test
    void testEmptyPassword() {
        Employee employee = new Employee("Alice", "alice01", "");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Password cannot be empty"));
    }

    @Test
    void testShortPassword() {
        Employee employee = new Employee("Alice", "alice01", "123");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Password must be between 4 and 16 characters"));
    }

    @Test
    void testLongPassword() {
        Employee employee = new Employee("Alice", "alice01", "averylongpasswordthatexceedslimit");
        String errors = validator.validate(employee);
        assertTrue(errors.contains("Password must be between 4 and 16 characters"));
    }

    @Test
    void testNullFields() {
        Employee employee = new Employee(null, null, null);
        String errors = validator.validate(employee);
        assertTrue(errors.trim().isEmpty(), "Expected no errors for null fields");
    }
}