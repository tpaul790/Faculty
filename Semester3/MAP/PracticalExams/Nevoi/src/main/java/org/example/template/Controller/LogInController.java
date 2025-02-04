package org.example.template.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.template.ApplicationStart;
import org.example.template.Main;
import org.example.template.Service.Service;

import java.io.IOException;

public class LogInController {
    private Service service;
    private Stage stage;
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;

    public void setService(Service service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onLogInButtonClick(ActionEvent actionEvent) {

    }

    @FXML
    private void onSignUpButtonClick() {
        //creez scena pentru signup
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("signup.fxml"));
            Parent root = loader.load();
            stage.setTitle("SignUp");
            stage.setScene(new Scene(root));
            stage.setWidth(290);
            stage.setHeight(350);
            SignUpController controller = loader.getController();
            controller.setService(service);
            controller.setStage(stage);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
