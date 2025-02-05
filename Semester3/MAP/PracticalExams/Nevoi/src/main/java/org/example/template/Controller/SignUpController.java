package org.example.template.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.template.ApplicationStart;
import org.example.template.Domain.Oras;
import org.example.template.Domain.Person;
import org.example.template.Service.Service;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class SignUpController {
    private Service service;
    private Stage stage;
    public ComboBox<Oras> orasCombo;
    public TextField textFieldName;
    public TextField textFieldPrenume;
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;
    public TextField textFiledStrada;
    public TextField textFieldNumar;
    public TextField textFieldTelefon;

    public void initialize() {
        List<Oras> orase = List.of(Oras.Bistrita,Oras.Cluj,Oras.Bucuresti,Oras.Bacau);
        orasCombo.getItems().addAll(orase);
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //iei datele, creezi persoana si o adaugi
    public void onSignUpButtonClick(ActionEvent actionEvent) {
        Predicate<String> empty = String::isEmpty;
        String name = textFieldName.getText();
        String prenume = textFieldPrenume.getText();
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        String strada = textFiledStrada.getText();
        String numar = textFieldNumar.getText();
        String telefon = textFieldTelefon.getText();
        Oras oras = orasCombo.getValue();
        if(!empty.test(name) && !empty.test(prenume) && !empty.test(username) && !empty.test(password) && !empty.test(strada) && !empty.test(numar) && !empty.test(telefon) && oras != null) {
            service.savePerson(name,prenume,username,password,oras,strada,numar,telefon);
            textFiledStrada.clear();
            textFieldNumar.clear();
            textFieldTelefon.clear();
            textFieldUsername.clear();
            textFieldPassword.clear();
            textFieldPrenume.clear();
            textFieldName.clear();
            orasCombo.getSelectionModel().clearSelection();
        }
    }

    //te intorci pe pagina de login
    public void onLogInButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("login.fxml"));
            Parent root = loader.load();
            stage.setTitle("LogIn");
            stage.setScene(new Scene(root));
            stage.setWidth(401);
            stage.setHeight(300);
            LogInController controller = loader.getController();
            controller.setService(service);
            controller.setStage(stage);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
