package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.dto.FriendRequestDto;
import com.example.socialnetworkgui.utils.Utils;
import com.example.socialnetworkgui.utils.events.FriendshipEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.FriendshipEventType;
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
import java.time.LocalDate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestViewController extends AbstractController implements Observer<FriendshipEntityChangeEvent>{
    private ObservableList<FriendRequestDto> model = FXCollections.observableArrayList();

    @FXML
    private TableView<FriendRequestDto> tableView;
    @FXML
    private TableColumn<FriendRequestDto, String> tableColumnUsername;
    @FXML
    private TableColumn<FriendRequestDto, String> tableColumnEmail;
    @FXML
    private TableColumn<FriendRequestDto, LocalDate> tableColumnSendDate;
    @FXML
    public TextField searchTextField;

    @FXML
    public void initialize() {
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("sender"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnSendDate.setCellValueFactory(new PropertyValueFactory<>("createDateTime"));
        tableView.setItems(model);
    }

    public void initModel(){
        Iterable<FriendRequestDto> friendships = getFriendshipService().getRepository().findAllFriendRequestsDto(getConectedUser().getId());
        model.setAll(StreamSupport.stream(friendships.spliterator(), false).collect(Collectors.toList()));
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

    public void onAcceptButtonClick(ActionEvent actionEvent) {
        FriendRequestDto selectedFriendship = tableView.getSelectionModel().getSelectedItem();
        if(selectedFriendship != null) {
            getFriendshipService().acceptFriendRequest(selectedFriendship.getIdFriendship().getFirst(), selectedFriendship.getIdFriendship().getSecond());
            MessageAlert.showSuccesMessage(getStage(),"Friend request accepted");
        }else{
            MessageAlert.showErrorMessage(getStage(),"No friend request selected");
        }
    }

    public void onDeclineButtonClick(ActionEvent actionEvent) {
        FriendRequestDto selectedFriendship = tableView.getSelectionModel().getSelectedItem();
        if (selectedFriendship != null) {
            getFriendshipService().declineFriendRequest(selectedFriendship.getIdFriendship().getFirst(), selectedFriendship.getIdFriendship().getSecond());
            MessageAlert.showSuccesMessage(getStage(),"Friend request declined");
        }else{
            MessageAlert.showErrorMessage(getStage(),"No friend request selected");
        }
    }
    @Override
    public void afterSetServices() {
        initModel();
        getFriendshipService().addObserver(this);
    }

    @Override
    public void update(FriendshipEntityChangeEvent event) {
        if(event.getType().equals(FriendshipEventType.ACCEPT) || event.getType().equals(FriendshipEventType.DECLINE)) {
            Friendship friendship = event.getData();
            Predicate<FriendRequestDto> predicate = f -> f.getIdFriendship().equals(friendship.getId());
            model.removeIf(predicate);
        }
        if(event.getType().equals(FriendshipEventType.SEND)) {
            Friendship friendship = event.getData();
            if(friendship.getId().getSecond().equals(getConectedUser().getId())){
                FriendRequestDto dto = new FriendRequestDto(friendship.getId(),null,null,friendship.getCreateDate());
                User sender = getUserService().findOne(friendship.getId().getFirst());
                dto.setEmail(sender.getEmail());
                dto.setSender(sender.getUsername());
                model.add(dto);
            }
        }
    }
}
