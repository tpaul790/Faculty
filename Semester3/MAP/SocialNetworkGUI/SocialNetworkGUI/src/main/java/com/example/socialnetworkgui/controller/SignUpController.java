package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.Aplication;
import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.utils.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import com.example.socialnetworkgui.domain.validation.RepoException;
import com.example.socialnetworkgui.domain.validation.ServiceException;
import com.example.socialnetworkgui.domain.validation.ValidationException;
import com.example.socialnetworkgui.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class SignUpController extends AbstractSimpleController{
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField textFieldPassword;

    @Override
    public void afterSetServices() {

    }

    @FXML
    public void onSignUpButtonClick(ActionEvent actionEvent) {
        String username = textFieldUsername.getText();
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();
        try{
            getUserService().save(username,email,password,"member");
            MessageAlert.showSuccesMessage(getStage(),"Account successfuly created!");
            textFieldUsername.clear();
            textFieldEmail.clear();
            textFieldPassword.clear();
        }catch (ValidationException | RepoException | ServiceException e){
            MessageAlert.showErrorMessage(getStage(),e.getMessage());
            textFieldUsername.clear();
            textFieldEmail.clear();
            textFieldPassword.clear();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void onLogInButtonClick(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = new FXMLLoader(Aplication.class.getResource("login-view.fxml"));
            Parent root = loader.load();
            getStage().setTitle("LogIn");
            getStage().setScene(new Scene(root));
            getStage().setWidth(290);
            getStage().setHeight(200);
            LogInController controller = loader.getController();
            controller.setServices(getUserService(), getFriendshipService(),getMessageService());
            controller.setStage(getStage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
