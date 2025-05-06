package controller;

import domain.Employee;
import domain.Task;
import domain.validator.RepoException;
import domain.validator.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import repository.Repository;
import service.Service;
import utils.events.ChangeEvent;
import utils.events.EventType;
import utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;

public class CeoMainPageController implements Observer<ChangeEvent> {
    private Service service;
    private Stage stage;
    private Employee employee;

    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    public Label nameLabel;
    public ListView<Task> unsolvedTasksListView;
    public TextArea taskDescription;
    public TableView<Employee> employeesTableView;
    public TableColumn<String,String> nameColumn;
    public TableColumn<String,String> usernameColumn;
    public TextField nameTextField;
    public TextField usernameTextField;
    public TextField passwordTextField;

    @FXML
    private void initialize() {
        unsolvedTasksListView.setItems(tasks);
        employeesTableView.setItems(employees);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    public void setService(Service service) {
        this.service = service;
        afterSetService();
    }

    public void afterSetService() {
        initTasks();
        service.addObserver(this);
    }

    public void initTasks() {
        tasks.clear();
        tasks.setAll(service.findAllTasks());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        afterSetEmployee();
    }

    public void afterSetEmployee() {
        initNameLabel();
    }

    public void initNameLabel() {
        nameLabel.setText(employee.getName() + " : " + employee.getUsername());
    }

    public void onCreateTaskButtonClick(ActionEvent actionEvent) {
        if(!taskDescription.getText().isEmpty()) {
            String description = taskDescription.getText();
            try {
                service.saveTask(description, LocalDateTime.now());
                taskDescription.clear();
                initTasks();
            }catch (RepoException e) {
                MessageAlert.showErrorMessage(stage,e.getMessage());
            }
        }else{
            MessageAlert.showErrorMessage(stage,"Please write a description for the task!");
        }
    }

    public void onAddEmployeeButtonClick(ActionEvent actionEvent) {
        if(!nameTextField.getText().isEmpty() && !usernameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            String name = nameTextField.getText();
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            try {
                service.saveEmployee(name, username, password);
                nameTextField.clear();
                usernameTextField.clear();
                passwordTextField.clear();
                MessageAlert.showSuccesMessage(stage,"Employee have been added successfully!");
            }catch (ServiceException e) {
                MessageAlert.showErrorMessage(stage, e.getMessage());
            }
        }else{
            MessageAlert.showErrorMessage(stage,"Please complete all the text fields above");
        }
    }

    public void onSendTaskButtonClick(ActionEvent actionEvent) {
        Employee employee = employeesTableView.getSelectionModel().getSelectedItem();
        Task task = unsolvedTasksListView.getSelectionModel().getSelectedItem();
        if(employee != null && task != null){
            try {
                service.sendTask(task, employee);
                tasks.remove(task);
                MessageAlert.showSuccesMessage(stage, "Task has been sent!");
            }catch (ServiceException e){
                MessageAlert.showErrorMessage(stage,e.getMessage());
            }
        }else{
            MessageAlert.showErrorMessage(stage,"Please select a task and an employee!");
        }
    }

    public void onLogOutButtonClick(ActionEvent actionEvent) throws IOException {
        service.logOut(employee);
        service.removeObserver(this);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        LogInController controller = fxmlLoader.getController();
        controller.setService(service);
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
        stage.setWidth(340);
        stage.setHeight(365);
    }

    @Override
    public void update(ChangeEvent event) {
        if(event.getEventType() == EventType.EMPLOYEE_LOGGED_IN){
            employees.add(event.getEmployee());
        }
        if(event.getEventType() == EventType.EMPLOYEE_LOGGED_OUT){
            employees.remove(event.getEmployee());
            initTasks();
        }
        if (event.getEventType() == EventType.SOLVE_TASK){
            Employee emp = event.getEmployee();
            Task task = event.getTask();
            MessageAlert.showSuccesMessage(stage,emp.getName() + " - succesfully solved the task: " + task.getDescription());
        }
    }
}
