package ServiceTests;

import domain.Employee;
import domain.Task;
import domain.validator.RepoException;
import domain.validator.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import repository.IEmployeeRepository;
import repository.ITaskRepository;
import service.Service;
import utils.events.ChangeEvent;
import utils.events.EventType;
import utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceTest {
    private IEmployeeRepository employeeRepo;
    private ITaskRepository taskRepo;
    private Service service;

    @BeforeEach
    void setUp() {
        employeeRepo = mock(IEmployeeRepository.class);
        taskRepo = mock(ITaskRepository.class);
        service = new Service(employeeRepo, taskRepo);
    }

    @Test
    void testFindEmployeeByUsername() {
        Employee emp = new Employee("John", "john123", "pass");
        when(employeeRepo.findByUsername("john123")).thenReturn(Optional.of(emp));

        Optional<Employee> result = service.findEmployeeByUsername("john123");
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
    }

    @Test
    void testSaveTask() throws RepoException {
        doNothing().when(taskRepo).save(any(Task.class));

        assertDoesNotThrow(() -> service.saveTask("This is a valid task", LocalDateTime.now()));
        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    void testSaveTaskThrowsServiceException() throws RepoException {
        doThrow(new RepoException("error")).when(taskRepo).save(any(Task.class));

        ServiceException ex = assertThrows(ServiceException.class, () ->
                service.saveTask("description", LocalDateTime.now()));
        assertEquals("error", ex.getMessage());
    }

    @Test
    void testLogInSuccess() throws ServiceException {
        Employee emp = new Employee("Ana", "ana123", "secret");
        when(employeeRepo.findByUsername("ana123")).thenReturn(Optional.of(emp));

        Observer<ChangeEvent> observer = mock(Observer.class);
        service.addObserver(observer);

        Employee result = service.logIn("ana123", "secret");
        assertEquals(emp, result);
        verify(observer).update(any(ChangeEvent.class));
    }

    @Test
    void testLogInWrongPasswordThrowsException() {
        Employee emp = new Employee("Ana", "ana123", "secret");
        when(employeeRepo.findByUsername("ana123")).thenReturn(Optional.of(emp));

        assertThrows(ServiceException.class, () -> service.logIn("ana123", "wrong"));
    }

    @Test
    void testLogInUserNotFoundThrowsException() {
        when(employeeRepo.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> service.logIn("ghost", "pw"));
    }

    @Test
    void testLogOut() {
        Employee emp = new Employee("Maria", "maria12", "pass");
        Observer<ChangeEvent> observer = mock(Observer.class);
        service.addObserver(observer);

        service.logOut(emp);

        verify(taskRepo).setAllAssignedIdToNull(emp);
    }

    @Test
    void testSendTaskSuccess() throws RepoException, ServiceException {
        Employee emp = new Employee("George", "george1", "pw");
        Task task = new Task("solve me");
        task.setId(1);
        when(taskRepo.update(task)).thenReturn(Optional.empty());

        Observer<ChangeEvent> observer = mock(Observer.class);
        service.addObserver(observer);

        service.sendTask(task, emp);

        assertEquals(emp, task.getAssignetEmployee());
        verify(taskRepo).update(task);
        verify(observer).update(any(ChangeEvent.class));
    }

    @Test
    void testSendTaskFails() throws RepoException {
        Employee emp = new Employee("George", "george1", "pw");
        Task task = new Task("solve me");
        task.setId(1);
        when(taskRepo.update(task)).thenReturn(Optional.of(task));

        assertThrows(ServiceException.class, () -> service.sendTask(task, emp));
    }

    @Test
    void testSolveTask() throws RepoException, ServiceException {
        Employee emp = new Employee("Gina", "gina", "pass");
        Task task = new Task("do it");
        task.setId(2);

        when(taskRepo.update(task)).thenReturn(Optional.empty());

        Observer<ChangeEvent> observer = mock(Observer.class);
        service.addObserver(observer);

        service.solveTask(task, emp);

        assertNotNull(task.getSolveTime());
        verify(taskRepo).update(task);
    }
}