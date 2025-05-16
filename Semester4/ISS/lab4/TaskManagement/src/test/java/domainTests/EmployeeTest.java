package domainTests;

import domain.Employee;
import domain.EmployeeType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    @Test
    void testConstructorAndGetters() {
        Employee employee = new Employee("Alice", "alice01", "pass123");
        assertEquals("Alice", employee.getName());
        assertEquals("alice01", employee.getUsername());
        assertEquals("pass123", employee.getPassword());
        assertEquals(EmployeeType.EMPLOYEE, employee.getType());
        assertNull(employee.getId());
    }

    @Test
    void testSettersAndGetters() {
        Employee employee = new Employee();
        employee.setId(10);
        employee.setName("Bob");
        employee.setUsername("bob123");
        employee.setPassword("securepass");
        employee.setType(EmployeeType.CEO);

        assertEquals(10, employee.getId());
        assertEquals("Bob", employee.getName());
        assertEquals("bob123", employee.getUsername());
        assertEquals("securepass", employee.getPassword());
        assertEquals(EmployeeType.CEO, employee.getType());
    }
}