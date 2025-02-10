package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.service.UserService;
import javafx.application.Application;
import javafx.stage.Stage;

public abstract class AbstractController extends AbstractSimpleController{
    private User conectedUser;

    public User getConectedUser() {
        return conectedUser;
    }

    public void setConectedUser(User conectedUser) {
        this.conectedUser = conectedUser;
    }
}
