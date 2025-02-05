package org.example.template.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.template.ApplicationStart;
import org.example.template.Domain.Person;
import org.example.template.Main;
import org.example.template.Service.Service;

import java.io.IOException;

public class LogInController {
    private Service service;
    private Stage stage;
    private ObservableList<Person> model = FXCollections.observableArrayList();
    public ListView<Person> listView;
    public PasswordField textFieldPassword;

    public void initialize() {
        listView.setItems(model);
    }

    public void setService(Service service) {
        this.service = service;
        initModel();
    }

    public void initModel() {
        model.setAll(service.findAllPersons());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //daca s-a facut corect login ul atunci se deschide fereastra principala
    @FXML
    public void onLogInButtonClick(ActionEvent actionEvent) {
        Person person = listView.getSelectionModel().getSelectedItem();
        if (person != null && person.getPassword().equals(textFieldPassword.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("principal.fxml"));
                Parent root = loader.load();
                stage.setTitle("Principal");
                stage.setScene(new Scene(root));
                stage.setWidth(600);
                stage.setHeight(540);
                PrincipalController controller = loader.getController();
                controller.setUser(person);
                controller.setService(service);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //se deschide fereastra pentru signUp
    @FXML
    private void onSignUpButtonClick() {
        //creez scena pentru signup
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("signup.fxml"));
            Parent root = loader.load();
            stage.setTitle("SignUp");
            stage.setScene(new Scene(root));
            stage.setWidth(300);
            stage.setHeight(380);
            SignUpController controller = loader.getController();
            controller.setService(service);
            controller.setStage(stage);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
