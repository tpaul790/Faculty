package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.Aplication;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class MyPageViewController extends AbstractController implements Observer<UserEntityChangeEvent> {
    @FXML
    private ImageView profilePicture;
    @FXML
    private Label username;
    @FXML
    private Label nrOfFriends;
    @FXML
    private Label nrOfFriendRequests;
    @FXML
    private Label aboutMe;

    @Override
    public void afterSetServices() {
        System.out.println(getConectedUser().getProfilePicture());
        setAllViews(getConectedUser());
        getUserService().addObserver(this);
    }

    public void setAllViews(User user){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(getConectedUser().getProfilePicture())));
        profilePicture.setImage(image);
        profilePicture.setFitWidth(300);
        profilePicture.setFitHeight(120);
        aboutMe.setText(user.getAboutMe());
        username.setText(user.getUsername());
        Set<User> friends = (Set<User>)getFriendshipService().getAllFriends(user.getId());
        nrOfFriends.setText(String.valueOf(friends.size()));
        Set<Friendship> friendRequest = (Set<Friendship>) getFriendshipService().getAllFriendRequests(user.getId());
        nrOfFriendRequests.setText(String.valueOf(friendRequest.size()));
    }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            AccountViewController controller = (AccountViewController) Utils.setSceneOnStage(getStage(),"account-view.fxml","Account",400,430);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getUserService().removeObserver(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogOutButtonClick(ActionEvent actionEvent) {
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
            getUserService().removeObserver(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUpdateDataButtonClick(ActionEvent actionEvent) {
        try {
            AbstractController controller = Utils.setSceneOnStage(getStage(), "updateUserData-view.fxml", "UpdateUserData", 400, 528);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getUserService().removeObserver(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UserEntityChangeEvent event) {
        if(event.getType().equals(ChangeEventType.UPDATE)){
            if(event.getData().getId().equals(getConectedUser().getId())) {
                setAllViews(event.getData());
                setConectedUser(event.getData());
            }
        }
    }
}
