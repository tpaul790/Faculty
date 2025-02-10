package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.io.IOException;

public class GroupViewController extends AbstractController {
    private ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    public TextField messageTextField;
    @FXML
    public ListView<User> usersList;

    @FXML
    public void initialize() {
        usersList.setItems(model);

        usersList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                }else{
                    setText(user.getUsername());
                    setStyle("-fx-font-size: 24px; -fx-font-weight: bold");
                }
            }
        });

        usersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void initModel(){
        getFriendshipService().getAllFriends(getConectedUser().getId()).forEach(u -> model.add(u));
    }

    public void onSendButtonClick(ActionEvent actionEvent) {
        ObservableList<User> selectedUsers = usersList.getSelectionModel().getSelectedItems();
        int size = selectedUsers.size();
        String msg = messageTextField.getText();
        selectedUsers.forEach(u -> { getMessageService().save(getConectedUser().getId(),u.getId(),msg,getConectedUser().getUsername(),u.getUsername());});
        messageTextField.clear();
        MessageAlert.showSuccesMessage(getStage(), "Message succesfully send to the all "+ size +" selected users");
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            AccountViewController controller = (AccountViewController) Utils.setSceneOnStage(getStage(),"account-view.fxml","Account",400,430);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void afterSetServices() {
        initModel();
    }
}
