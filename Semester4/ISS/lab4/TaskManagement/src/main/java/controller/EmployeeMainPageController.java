package controller;

import domain.Employee;
import domain.Task;
import domain.validator.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import service.Service;

import utils.events.EventType;
import utils.observer.Observer;
import utils.events.ChangeEvent;

import java.io.IOException;

public class EmployeeMainPageController implements Observer<ChangeEvent>{
    private Service service;
    private Stage stage;
    private Employee employee;

    public ObservableList<Task> model = FXCollections.observableArrayList();
    public ListView<Task> listView;
    public Label nameLabel;

    public void initialize() {
        listView.setItems(model);
    }

    public void setService(Service service) {
        this.service = service;
        afterSetService();
    }

    public void afterSetService() {
        service.addObserver(this);
    }

    public void setStage(Stage stage) {this.stage = stage;}

    public void setEmployee(Employee employee) {
        this.employee = employee;
        afterSetEmployee();
    }

    private void afterSetEmployee() {initNameLabel();}

    public void initNameLabel() {
        nameLabel.setText(employee.getName());
    }

    public void onSolveButtonClick(ActionEvent actionEvent) {
        Task task = listView.getSelectionModel().getSelectedItem();
        if(task != null) {
            try{
                service.solveTask(task,employee);
                model.remove(task);
                MessageAlert.showSuccesMessage(stage,"Task solved");
            }catch (ServiceException e){
                MessageAlert.showErrorMessage(stage,e.getMessage());
            }
        }else
            MessageAlert.showErrorMessage(stage,"Please select a task");
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
        if(event.getEventType() == EventType.SEND_TASK){
            if(employee.getId().equals(event.getTask().getAssignetEmployeeId())){
                MessageAlert.showSuccesMessage(stage,"You have received a task");
                model.add(event.getTask());
            }
        }
    }
}
