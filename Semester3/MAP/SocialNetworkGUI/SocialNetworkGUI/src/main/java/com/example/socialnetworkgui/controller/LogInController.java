package com.example.socialnetworkgui.controller;
import com.example.socialnetworkgui.Aplication;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validation.ServiceException;
import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import com.example.socialnetworkgui.service.UserService;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.jshell.execution.Util;

import java.io.IOException;

public class LogInController extends AbstractSimpleController{
    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField textFieldPassword;

    @FXML
    private void initialize() {
    }

    @Override
    public void afterSetServices() {

    }

    @FXML
    private void onSignUpButtonClick() {
        //creez scena pentru signup
        try {
            FXMLLoader loader = new FXMLLoader(Aplication.class.getResource("signup-view.fxml"));
            Parent root = loader.load();
            getStage().setTitle("SignUp");
            getStage().setScene(new Scene(root));
            getStage().setWidth(290);
            getStage().setHeight(240);
            SignUpController controller = loader.getController();
            controller.setServices(getUserService(), getFriendshipService(), getMessageService());
            controller.setStage(getStage());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogInButtonClick(ActionEvent actionEvent) {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        try{
            User user = getUserService().findOne(username);
            if(user.getPassword().equals(password)){
                AccountViewController controller = (AccountViewController) Utils.setSceneOnStage(getStage(),"account-view.fxml","Account",400,430);
                Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),user);
                MessageAlert.showSuccesMessage(getStage(),"Successfully logged in!");
            }else{
                MessageAlert.showErrorMessage(getStage(), "Wrong password!");
                textFieldPassword.clear();
            }
        }catch (ServiceException e){
            textFieldPassword.clear();
            textFieldUsername.clear();
            MessageAlert.showErrorMessage(null, e.getMessage());
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
