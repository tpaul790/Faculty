package org.example.template.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.template.Domain.Show;
import org.example.template.Domain.User;
import org.example.template.Service.Service;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Events.EventType;
import org.example.template.Utils.Observer.Observer;

import java.util.Set;

public class UserController implements Observer<ChangeEvent> {
    private Service service;
    private User user;
    private ObservableList<HBox> model = FXCollections.observableArrayList();
    private final int pageSize = 3;
    private int curentPage = 1;
    private int nrOfPages;

    public Label emailLabel;
    public ListView<HBox> listView;
    public Button prevBtn;
    public Button nextBtn;
    public Label curentPageLabel;

    //paginare
    public void initNumberOfPages(int size){
        if(size > 0) {
            nrOfPages = size / pageSize;
            if (nrOfPages == 0) {
                nrOfPages = 1;
            } else {
                if (size % pageSize != 0) {
                    nrOfPages++;
                }
            }
        }
    }

    public void initPageLabel(){
        prevBtn.setDisable(curentPage == 1);
        nextBtn.setDisable(curentPage == nrOfPages);
        curentPageLabel.setText(curentPage+"/"+nrOfPages);
    }

    public void onPrevButtonClick(ActionEvent actionEvent) {
        curentPage--;
        initModel();
    }

    public void onNextButtonClick(ActionEvent actionEvent) {
        curentPage++;
        initModel();
    }

    //restul
    public void initialize() {
        listView.setItems(model);
    }


    public void initModel(){
        model.clear();
        for(Show show : service.findAllShowsOnPage(curentPage,pageSize)){
            model.add(initHBox(show));
        }
        initNumberOfPages(service.findAllShows().size());
        initPageLabel();
    }

    public HBox initHBox(Show show){
        HBox hBox = new HBox();
        Button button = new Button("Rezerva");
        Label label = new Label(show.toString());
        hBox.setSpacing(20);
        hBox.getChildren().add(label);
        hBox.getChildren().add(button);
        button.setOnAction(event -> handleRezerva(hBox,show));
        return hBox;
    }

    private void handleRezerva(HBox hbox, Show show) {
        int index = model.indexOf(hbox);
        model.remove(index);

        Button btn = new Button("Rezerva");
        HBox newHbox = new HBox();
        newHbox.setSpacing(20);
        btn.setOnAction(event -> handleRezerva(newHbox,show));
        show.setNrOfSeats(show.getNrOfSeats()-1);
        newHbox.getChildren().add(new Label(show.toString()));
        newHbox.getChildren().add(btn);
        model.add(index, newHbox);
        service.rezervaLoc(show);
    }

    public void setService(Service service) {
        this.service = service;
        afterService();
    }

    public void setUser(User user) {
        this.user = user;
        initEmailLabel();
    }

    public void initEmailLabel() {
        emailLabel.setText(user.getEmail());
    }

    public void afterService() {
        initModel();
        service.addObserver(this);
    }

    @Override
    public void update(ChangeEvent event) {
        if(event.getEventType().equals(EventType.ADD_SPECTACOL)){
            Stage stage = (Stage) prevBtn.getScene().getWindow();
            MessageAlert.showSuccesMessage(stage,"New shows have been scheduled. Do you want to see them?");
            initModel();
        }
    }
}
