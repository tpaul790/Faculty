package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.LogInController;
import com.example.socialnetworkgui.domain.validation.FriendshipValidator;
import com.example.socialnetworkgui.domain.validation.MessageValidator;
import com.example.socialnetworkgui.domain.validation.UserValidator;
import com.example.socialnetworkgui.repository.database.FriendshipDbRepo;
import com.example.socialnetworkgui.repository.database.MessageDbRepo;
import com.example.socialnetworkgui.repository.database.UserDbRepo;
import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.service.MessageService;
import com.example.socialnetworkgui.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Aplication extends Application {
    private UserService userService;
    @Override
    public void start(Stage stage) throws IOException {
        //pentru login 290,200
        initView(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {
        //validator,repo si service
        UserValidator userValidator = UserValidator.getInstance();
        FriendshipValidator friendshipValidator = FriendshipValidator.getInstance();
        MessageValidator messageValidator = MessageValidator.getInstance();

        UserDbRepo userDbRepo = new UserDbRepo("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","paul2004",userValidator);
        FriendshipDbRepo friendshipDbRepo = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","paul2004",friendshipValidator);
        MessageDbRepo messageDbRepo = new MessageDbRepo("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","paul2004", messageValidator);

        UserService userService = new UserService(userDbRepo);
        FriendshipService friendshipService = new FriendshipService(friendshipDbRepo,userDbRepo);
        MessageService messageService = new MessageService(messageDbRepo);

        //initializez 2 login-uri
        FXMLLoader fxmlLoader1 = new FXMLLoader(Aplication.class.getResource("login-view.fxml"));
        FXMLLoader fxmlLoader2 = new FXMLLoader(Aplication.class.getResource("login-view.fxml"));
        AnchorPane logIn1 = fxmlLoader1.load();
        AnchorPane logIn2 = fxmlLoader2.load();
        Scene logInScene1 = new Scene(logIn1);
        Scene logInScene2 = new Scene(logIn2);

        primaryStage.setScene(logInScene1);
        primaryStage.setWidth(290);
        primaryStage.setHeight(200);
        primaryStage.setTitle("LogIn");

        Stage primaryStage2 = new Stage();
        primaryStage2.setScene(logInScene2);
        primaryStage2.setWidth(290);
        primaryStage2.setHeight(200);
        primaryStage2.setTitle("LogIn");
        primaryStage2.show();

        LogInController controller = fxmlLoader1.getController();
        controller.setServices(userService,friendshipService, messageService);
        controller.setStage(primaryStage);

        LogInController controller2 = fxmlLoader2.getController();
        controller2.setServices(userService,friendshipService, messageService);
        controller2.setStage(primaryStage2);
    }
}