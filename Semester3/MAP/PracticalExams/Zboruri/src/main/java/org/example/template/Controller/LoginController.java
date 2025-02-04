package org.example.template.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.template.ApplicationStart;
import org.example.template.Domain.Client;
import org.example.template.Service.Service;

import java.io.IOException;

public class LoginController {
    private Service service;
    private Stage mainStage;
    @FXML
    public TextField textField;

    public void onLogInButtonClick(ActionEvent actionEvent) {
        if(!textField.getText().isEmpty()){
            Client client = service.logIn(textField.getText());
            if(client != null){
                try {
                    FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("principal.fxml"));
                    Parent root = loader.load();
                    mainStage.setTitle("Zboruri");
                    mainStage.setScene(new Scene(root));
                    mainStage.setWidth(600);
                    mainStage.setHeight(400);
                    PrincipalController controller = loader.getController();
                    controller.setService(service);
                    controller.setClient(client);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void afterService(){

    }

    public void setService(Service service) {
        this.service = service;
        afterService();
    }

    public void setStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
