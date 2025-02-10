package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.FriendshipEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.FriendshipEventType;
import com.example.socialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddFriendViewController extends AbstractController implements Observer<FriendshipEntityChangeEvent> {
    private ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User,String> tableColumnUsername;
    @FXML
    public TableColumn<User,String> tableColumnEmail;
    @FXML
    public TextField searchTextField;

    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableView.setItems(model);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> handleFilter());
    }

    private void handleFilter() {
        Predicate<User> predicate = u -> u.getUsername().startsWith(searchTextField.getText());
        Set<User> users = (Set<User>)getFriendshipService().getAllUsersThatAreNotFriends(getConectedUser().getId());
        model.setAll(users.stream().filter(predicate).collect(Collectors.toList()));
    }

    public void onBackButtonClick(ActionEvent actionEvent) {
        try {
            AccountViewController controller = (AccountViewController) Utils.setSceneOnStage(getStage(),"account-view.fxml","Account",400,430);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getFriendshipService().removeObserver(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initModel(){
        Iterable<User> usersThatAreNotFriendsWithMe = getFriendshipService().getAllUsersThatAreNotFriends(getConectedUser().getId());
        model.addAll(StreamSupport.stream(usersThatAreNotFriendsWithMe.spliterator(), false).toList());
    }

    public void onSendFriendRequestButtonClick(ActionEvent actionEvent) {
        User user = tableView.getSelectionModel().getSelectedItem();
        getFriendshipService().sendFriendRequest(getConectedUser().getId(), user.getId());
    }

    @Override
    public void afterSetServices() {
        initModel();
        getFriendshipService().addObserver(this);
    }

    @Override
    public void update(FriendshipEntityChangeEvent event) {
        if (event.getType().equals(FriendshipEventType.SEND)){
            Friendship friendship = event.getData();
            int userIdToDelete;
            if(friendship.getId().getFirst().equals(getConectedUser().getId())){
                userIdToDelete = friendship.getId().getSecond();
            }else if(friendship.getId().getSecond().equals(getConectedUser().getId())){
                userIdToDelete = friendship.getId().getFirst();
            }else{
                userIdToDelete = -1;
            }
            if(userIdToDelete != -1)
                model.removeIf(user -> user.getId().equals(userIdToDelete));
        }

        if(event.getType().equals(FriendshipEventType.DECLINE)){
            Friendship friendship = event.getData();
            int userIdToAdd;
            if(friendship.getId().getFirst().equals(getConectedUser().getId())){
                userIdToAdd = friendship.getId().getSecond();
            }else if(friendship.getId().getSecond().equals(getConectedUser().getId())){
                userIdToAdd = friendship.getId().getFirst();
            }else{
                userIdToAdd = -1;
            }
            if(userIdToAdd != -1)
                model.add(getUserService().findOne(userIdToAdd));
        }
    }
}
