package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.MessageService;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.MessageEventType;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;

public class ConversationViewController extends AbstractController  implements Observer<MessageEntityChangeEvent> {
    private User conversationUser;

    private ObservableList<Message> model = FXCollections.observableArrayList();
    @FXML
    public Label usernameLabel;
    @FXML
    public ListView<Message> messgesList;
    @FXML
    public TextField messageTextField;

    @FXML
    public void initialize() {
        messgesList.setItems(model);

        messgesList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                }else{
                    if(item.getIdSender() == getConectedUser().getId()){
                        setText(item.getMessage()+" :You");
                        setStyle("-fx-alignment: CENTER-RIGHT; -fx-background-color: lightgreen;-fx-font-weight: bold;-fx-font-size: 15px;");
                    }else {
                        setText(conversationUser.getUsername() + ": " + item.getMessage());
                        setStyle("-fx-alignment: CENTER-LEFT; -fx-background-color: lightblue;-fx-font-weight: bold;-fx-font-size: 15px;");
                    }
                }
            }
        });
    }

    public void initModel(){
        getMessageService().findAllFor(getConectedUser().getId(), conversationUser.getId()).forEach(m -> model.addLast(m));
    }

    public void setConversationUser(User conversationUser) {
        this.conversationUser = conversationUser;
    }

    @Override
    public void afterSetServices() {
        usernameLabel.setText(conversationUser.getUsername());
        initModel();
        getMessageService().addObserver(this);
    }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            AccountViewController controller = (AccountViewController) Utils.setSceneOnStage(getStage(),"account-view.fxml","Account",400,430);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getMessageService().removeObserver(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onSendButtonClick(ActionEvent actionEvent) {
        String value = messageTextField.getText();
        getMessageService().save(getConectedUser().getId(),conversationUser.getId(),value,getConectedUser().getUsername(),conversationUser.getUsername());
        messageTextField.clear();
    }

    @Override
    public void update(MessageEntityChangeEvent event) {
        if(event.getType().equals(MessageEventType.SEND)){
           if(event.getData().getIdSender() == getConectedUser().getId() || event.getData().getIdReceiver() == getConectedUser().getId()){
               model.addLast(event.getData());
           }
        }
    }
}
