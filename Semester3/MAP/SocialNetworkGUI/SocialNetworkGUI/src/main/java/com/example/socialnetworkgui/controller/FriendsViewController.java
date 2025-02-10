package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Page;
import com.example.socialnetworkgui.domain.Pageable;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.utils.Utils;

import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.FriendshipEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.FriendshipEventType;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsViewController extends AbstractController implements Observer<FriendshipEntityChangeEvent>{
    private ObservableList<User> model = FXCollections.observableArrayList();
    private int pageSize = 3;
    private int curentPage = 1;
    private int nrOfPages;

    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User,String> tableColumnUsername;
    @FXML
    public TableColumn<User,String> tableColumnEmail;
    @FXML
    public Label curentPageLabel;
    @FXML
    public Button nextButton;
    @FXML
    public Button previousButton;

    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableView.setItems(model);
        curentPageLabel.textProperty().addListener((observable, oldValue, newValue) -> {initModel();handleNextButton();handlePreviousButton();});
    }

    public void initializeCurentPageLabel(){
        curentPageLabel.setText("Page "+ curentPage +" of "+nrOfPages);
    }

    public void intializeNumberOfPages(){
        int nrOfFriends = getFriendshipService().numberOfFriends(getConectedUser().getId());
        nrOfPages = nrOfFriends/pageSize;
        if(nrOfPages == 0) {
            nrOfPages = 1;
        }
        else{
            if (nrOfFriends % pageSize != 0) {
                nrOfPages++;
            }
        }
    }

    public void handleNextButton(){
        if (curentPage == nrOfPages) {
            nextButton.setDisable(true);
        }else{
            nextButton.setDisable(false);
        }
    }

    public void handlePreviousButton(){
        if (curentPage == 1) {
            previousButton.setDisable(true);
        }else {
            previousButton.setDisable(false);
        }
    }

    public void initModel(){
        Pageable pageable = new Pageable(curentPage, pageSize);
        Page<User> friends = getFriendshipService().getAllFriendsOnPage(getConectedUser().getId(),pageable);
        model.setAll((Set<User>) friends.getEntities());
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

    public void onRemoveFriendButtonClick(ActionEvent actionEvent) {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if(selectedUser != null){
            getFriendshipService().delete(selectedUser.getId(), getConectedUser().getId());
            MessageAlert.showSuccesMessage(getStage(),"Friend succesfuly removed");
        }else {
            MessageAlert.showErrorMessage(getStage(),"Please select a user from the table");
        }
    }

    @Override
    public void afterSetServices() {
        initModel();
        getFriendshipService().addObserver(this);
        intializeNumberOfPages();
        initializeCurentPageLabel();
        handleNextButton();
        handlePreviousButton();
    }

    @Override
    public void update(FriendshipEntityChangeEvent event) {
        if(event.getType().equals(FriendshipEventType.DELETE)){
            Friendship removedFriendship = event.getData();
            int userToRemoveId;
            if(removedFriendship.getId().getFirst().equals(getConectedUser().getId())){
                userToRemoveId = removedFriendship.getId().getSecond();
            }else if(removedFriendship.getId().getSecond().equals(getConectedUser().getId())){
                userToRemoveId = removedFriendship.getId().getFirst();
            }else{
                userToRemoveId = -1;
            }
            if(userToRemoveId != -1) {
                Predicate<User> predicate = x -> x.getId().equals(userToRemoveId);
                model.removeIf(predicate);
            }
        }
        if(event.getType().equals(FriendshipEventType.ACCEPT)){
            User userToAdd;
            Friendship friendship = event.getData();
            if(friendship.getId().getFirst().equals(getConectedUser().getId())){
                userToAdd = getUserService().findOne(friendship.getId().getSecond());
            }else if(friendship.getId().getSecond().equals(getConectedUser().getId())){
                userToAdd = getUserService().findOne(friendship.getId().getFirst());
            }else{
                userToAdd = null;
            }
            if(userToAdd != null)
                model.add(userToAdd);
        }
    }

    public void onNextButtonClick(ActionEvent actionEvent) {
        curentPage++;
        initializeCurentPageLabel();
    }

    public void onPreviousButtonClick(ActionEvent actionEvent) {
        curentPage--;
        initializeCurentPageLabel();
    }
}
