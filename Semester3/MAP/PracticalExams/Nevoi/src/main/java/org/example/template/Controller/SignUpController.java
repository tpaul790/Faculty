package org.example.template.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.template.ApplicationStart;
import org.example.template.Service.Service;

import java.io.IOException;

public class SignUpController {
    private Service service;
    private Stage stage;
    public TextField textFieldName;
    public TextField textFieldPrenume;
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;
    public TextField textFiedStrada;
    public TextField textFieldNumar;
    public TextField textFieldTelefon;

    public void setService(Service service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onSignUpButtonClick(ActionEvent actionEvent) {
    }

    public void onLogInButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("/login.fxml"));
            Parent root = loader.load();
            stage.setTitle("LogIn");
            stage.setScene(new Scene(root));
            stage.setWidth(290);
            stage.setHeight(200);
            LogInController controller = loader.getController();
            controller.setService(service);
            controller.setStage(stage);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
