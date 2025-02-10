package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.service.MessageService;
import com.example.socialnetworkgui.service.UserService;
import javafx.stage.Stage;

public abstract class AbstractSimpleController {
    private Stage stage;
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;

    public abstract void afterSetServices();

    public Stage getStage() {
        return stage;
    }

    public UserService getUserService() {
        return userService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setServices(UserService service, FriendshipService friendshipService, MessageService messageService) {
        this.userService = service;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        afterSetServices();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
