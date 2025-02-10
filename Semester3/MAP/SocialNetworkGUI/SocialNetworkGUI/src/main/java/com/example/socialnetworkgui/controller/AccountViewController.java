package com.example.socialnetworkgui.controller;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.utils.events.FriendshipEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.FriendshipEventType;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class AccountViewController extends AbstractController implements Observer<FriendshipEntityChangeEvent> {
    private ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    public TextField searchTextField;
    @FXML
    public ListView<User> talkToList;
    @FXML
    private Label usernameLabel;

    @FXML
    void initialize() {
        talkToList.setItems(model);

        talkToList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setStyle("-fx-font-weight: bold;-fx-font-size: 24px;");
                    setText(item.getUsername());
                }
            }
        });
        searchTextField.textProperty().addListener(observable -> { handleSearch(); });

        talkToList.getSelectionModel().selectedItemProperty().addListener((observer, oldValue, newValue) -> handleSelectUser(newValue));
    }

    private void handleSelectUser(User user) {
        try{
            ConversationViewController controller = (ConversationViewController) Utils.setSceneOnStage(getStage(),"conversation-view.fxml","Conversation",253,400);
            controller.setConversationUser(user);
            Utils.setDataForController(controller,getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getFriendshipService().removeObserver(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initModel(){
       getFriendshipService().getAllFriends(getConectedUser().getId()).forEach(u -> model.add(u));
    }

    public void handleSearch() {
        Predicate<User> serch = x -> x.getUsername().startsWith(searchTextField.getText());
        Set<User> friends = (Set<User>) getFriendshipService().getAllFriends(getConectedUser().getId());
        model.setAll(friends.stream().filter(serch).collect(Collectors.toList()));
    }

    public void onFriendsButtonClick(ActionEvent actionEvent) {
        try{
            FriendsViewController controller = (FriendsViewController) Utils.setSceneOnStage(getStage(),"friends-view.fxml","Friends",400,430);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getFriendshipService().removeObserver(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void onFriendRequestButtonClick(ActionEvent actionEvent) {
        try{
            FriendRequestViewController controller = (FriendRequestViewController) Utils.setSceneOnStage(getStage(),"friendRequests-view.fxml","FriendRequests",500,430);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getFriendshipService().removeObserver(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void onAddFriendButtonClick(ActionEvent actionEvent) {
        try{
            AddFriendViewController controller = (AddFriendViewController) Utils.setSceneOnStage(getStage(),"addFriend-view.fxml","AddFriend",400,430);
            Utils.setDataForController(controller, getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getFriendshipService().removeObserver(this);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void onMyPageButtonClick(ActionEvent actionEvent) {
        try{
            MyPageViewController controller = (MyPageViewController) Utils.setSceneOnStage(getStage(),"myPage-view.fxml","Settings",525,428);
            Utils.setDataForController(controller,getStage(),getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getFriendshipService().removeObserver(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onGroupMsgButtonClick(ActionEvent actionEvent) {
        try{
            AbstractController controller = Utils.setSceneOnStage(getStage(),"group-view.fxml","SendToManyUsers",330,415);
            Utils.setDataForController(controller, getStage(), getUserService(),getFriendshipService(),getMessageService(),getConectedUser());
            getFriendshipService().removeObserver(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUsernameLabel(String text) {
        usernameLabel.setText("User: "+text);
    }

    @Override
    public void afterSetServices() {
        getFriendshipService().addObserver(this);
        setUsernameLabel(getConectedUser().getUsername());
        initModel();
    }

    @Override
    public void update(FriendshipEntityChangeEvent event) {
        if(event.getType().equals(FriendshipEventType.ACCEPT)){
            Friendship friendship = event.getData();
            User userToAdd = null;
            if(friendship.getId().getFirst().equals(getConectedUser().getId())){
                userToAdd = getUserService().findOne(friendship.getId().getSecond());
            }else if(friendship.getId().getSecond().equals(getConectedUser().getId())){
                userToAdd = getUserService().findOne(friendship.getId().getFirst());
            }
            if(userToAdd != null){}
                model.add(userToAdd);
        }
        
        if(event.getType().equals(FriendshipEventType.DELETE)){
            Friendship friendship = event.getData();
            int userId;
            if(friendship.getId().getFirst().equals(getConectedUser().getId())){
                userId = friendship.getId().getSecond();
            }else if(friendship.getId().getSecond().equals(getConectedUser().getId())){
                userId = friendship.getId().getFirst();
            } else {
                userId = -1;
            }
            if(userId != -1)
                model.removeIf(u -> u.getId().equals(userId));
        }

        if(event.getType().equals(FriendshipEventType.SEND)){
            if(event.getData().getId().getSecond().equals(getConectedUser().getId())) {
                MessageAlert.showSuccesMessage(getStage(), "You just get a friend request!");
            }
        }
    }
}
