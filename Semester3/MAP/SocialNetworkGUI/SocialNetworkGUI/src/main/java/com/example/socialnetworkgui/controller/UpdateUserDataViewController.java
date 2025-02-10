package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validation.RepoException;
import com.example.socialnetworkgui.domain.validation.ServiceException;
import com.example.socialnetworkgui.domain.validation.ValidationException;
import com.example.socialnetworkgui.service.UserService;
import com.example.socialnetworkgui.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UpdateUserDataViewController extends AbstractController {
    @FXML
    private TextArea aboutMe;

    public void onSaveButtonClick(ActionEvent actionEvent) {
        String aboutMeText = aboutMe.getText();
        try {
            if (!aboutMeText.isEmpty()) {
                getConectedUser().setAboutMe(aboutMeText);
            }
            getUserService().update(getConectedUser().getId(),getConectedUser().getUsername(),getConectedUser().getEmail(),getConectedUser().getPassword(), getConectedUser().getRole(),getConectedUser().getProfilePicture(),getConectedUser().getAboutMe());
            MessageAlert.showSuccesMessage(getStage(), "User data successfully updated");
        }catch (ValidationException | RepoException | ServiceException e) {
            MessageAlert.showErrorMessage(getStage(), e.getMessage());
        }
    }

    public void onBackBottonClick(ActionEvent actionEvent) {
        try{
            AbstractController controller = Utils.setSceneOnStage(getStage(),"myPage-view.fxml","Settings",525,428);
            Utils.setDataForController(controller,getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterSetServices() {

    }
}
