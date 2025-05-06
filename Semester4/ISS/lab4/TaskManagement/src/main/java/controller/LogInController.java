package controller;

import com.sun.tools.javac.Main;
import domain.Employee;
import domain.EmployeeType;
import domain.validator.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.message.Message;
import service.Service;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class LogInController {
    private Service service;
    private Stage stage;
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;

    public void onLogInButtonClick(ActionEvent actionEvent) {
        if(!textFieldUsername.getText().isEmpty() && !textFieldPassword.getText().isEmpty()) {
            String username = textFieldUsername.getText();
            String password = textFieldPassword.getText();
            try{
                Employee employee = service.logIn(username,password);
                if (employee.getType() == EmployeeType.EMPLOYEE) {
                    openEmployeeWindow(employee);
                } else {
                    openCeoWindow(employee);
                }
            }catch (ServiceException | IOException e){
                MessageAlert.showErrorMessage(stage,e.getMessage());
            }
        }else{
            MessageAlert.showErrorMessage(stage,"Please complete the text fields above");
        }
    }

    public void openEmployeeWindow(Employee employee) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/employeeMain-view.fxml"));
        Parent parent = loader.load();
        EmployeeMainPageController controller = loader.getController();
        controller.setService(service);
        controller.setStage(stage);
        controller.setEmployee(employee);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Employee Main Page");
        stage.setWidth(600);
        stage.setHeight(420);
    }

    public void openCeoWindow(Employee employee) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ceoMain-view.fxml"));
        Parent parent = loader.load();
        CeoMainPageController controller = loader.getController();
        controller.setService(service);
        controller.setStage(stage);
        controller.setEmployee(employee);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Ceo Main Page");
        stage.setWidth(780);
        stage.setHeight(580);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
