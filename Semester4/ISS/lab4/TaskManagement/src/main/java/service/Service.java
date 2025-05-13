package service;

import domain.Employee;
import domain.Task;
import domain.validator.RepoException;
import domain.validator.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.EmployeeRepositoryDB;
import repository.IEmployeeRepository;
import repository.ITaskRepository;
import utils.events.ChangeEvent;
import utils.events.EventType;
import utils.observer.Observable;
import utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Service implements Observable<ChangeEvent> {
    private ITaskRepository taskRepository;
    private IEmployeeRepository employeeRepository;
    private static final Logger logger = LogManager.getLogger(Service.class);
    private List<Observer<ChangeEvent>> observers;

    public Service(IEmployeeRepository employeeRepository, ITaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.observers = new ArrayList<>();
    }

    public Optional<Employee> findEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public void saveTask(String description, LocalDateTime createTime) throws ServiceException {
        Task task = new Task(description);
        task.setCreateTime(createTime);
        try{
            taskRepository.save(task);
        }catch (RepoException e){
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public void saveEmployee(String name, String username, String password) throws ServiceException {
        try {
            employeeRepository.save(new Employee(name, username, password));
        }catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Employee logIn(String username, String password) throws ServiceException {
        Optional<Employee> emp = employeeRepository.findByUsername(username);
        if(emp.isPresent()) {
            Employee employee = emp.get();
            if(employee.getPassword().equals(password)) {
                notify(new ChangeEvent(EventType.EMPLOYEE_LOGGED_IN, employee));
                return employee;
            }
        }
        throw new ServiceException("Employee not found!");
    }

    public void logOut(Employee employee) {
        taskRepository.setAllAssignedIdToNull(employee.getId());
        notify(new ChangeEvent(EventType.EMPLOYEE_LOGGED_OUT, employee));
    }

    public void sendTask(Task task, Employee employee) throws ServiceException {
        task.setAssignetEmployeeId(employee.getId());
        try {
            Optional<Task> op = updateTask(task);
            if(op.isPresent())
                throw new ServiceException("Task not found");
            notify(new ChangeEvent(EventType.SEND_TASK,task));
        }catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private Optional<Task> updateTask(Task task) throws RepoException {
        return taskRepository.update(task);
    }

    public void solveTask(Task task, Employee employee) throws ServiceException {
        task.setSolveTime(LocalDateTime.now());
        try {
            updateTask(task);
            ChangeEvent changeEvent = new ChangeEvent(EventType.SOLVE_TASK,task);
            changeEvent.setEmployee(employee);
            notify(changeEvent);
        }catch (RepoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addObserver(Observer<ChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(ChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
