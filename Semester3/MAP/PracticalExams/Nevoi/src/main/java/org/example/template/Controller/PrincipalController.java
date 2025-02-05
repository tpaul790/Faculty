package org.example.template.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.template.Domain.Nevoie;
import org.example.template.Domain.Person;
import org.example.template.Domain.Status;
import org.example.template.Service.Service;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Events.EventType;
import org.example.template.Utils.Observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PrincipalController implements Observer<ChangeEvent> {
    private Service service;
    private Person user;
    private ObservableList<Nevoie> modelNevoi = FXCollections.observableArrayList();
    private ObservableList<Nevoie> modelFapte = FXCollections.observableArrayList();

    public TableView<Nevoie> tableNevoi;
    public TableColumn<Nevoie,String> columnTitluNevoi;
    public TableColumn<Nevoie,String> columnDescNevoi;
    public TableColumn<Nevoie, LocalDateTime> columnDeadlineNevoi;
    public TableColumn<Nevoie,Long> columnOmNevoi;
    public TableColumn<Nevoie,Long> columnErouNevoi;
    public TableColumn<Nevoie, Status> columnStatusNevoi;

    public TableView<Nevoie> tableFapte;
    public TableColumn<Nevoie,String> columnTitluFapte;
    public TableColumn<Nevoie,String> columnDescFapte;
    public TableColumn<Nevoie, LocalDateTime> columnDeadlineFapte;
    public TableColumn<Nevoie,Long> columnErouFapte;
    public TableColumn<Nevoie,Long> columnOmFapte;
    public TableColumn<Nevoie,Status> columnStatusFapte;
    public TextField titluNevoie;
    public DatePicker deadlineNevoie;
    public TextArea descriereNevoie;


    //initializez modelele si coloanele tabelelor
    public void initialize() {
        tableFapte.setItems(modelFapte);
        tableNevoi.setItems(modelNevoi);
        columnTitluNevoi.setCellValueFactory(new PropertyValueFactory<>("titlu"));
        columnDescNevoi.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        columnDeadlineNevoi.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        columnOmNevoi.setCellValueFactory(new PropertyValueFactory<>("omInNevoi"));
        columnErouNevoi.setCellValueFactory(new PropertyValueFactory<>("omSalvator"));
        columnStatusNevoi.setCellValueFactory(new PropertyValueFactory<>("status"));

        columnTitluFapte.setCellValueFactory(new PropertyValueFactory<>("titlu"));
        columnDescFapte.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        columnDeadlineFapte.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        columnErouFapte.setCellValueFactory(new PropertyValueFactory<>("omSalvator"));
        columnOmFapte.setCellValueFactory(new PropertyValueFactory<>("omInNevoi"));
        columnStatusFapte.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void afterService(){
        initModelNevoi();
        initModelFapte();
        service.addObserver(this);
    }

    private void initModelFapte() {
        modelFapte.setAll(service.findAllFapteBuneForUser(user));
    }

    //toate nevoile persoanelor care sunt din acelasi oras cu mine fara nevoile mele
    private void initModelNevoi() {
        modelNevoi.setAll(service.findAllNevoiForUser(user));
    }

    public void setUser(Person person) {
        user = person;
    }

    public void setService(Service service) {
        this.service = service;
        afterService();
    }

    //cand ajuti, primesti un mesaj, se actualizeaza nevoia si se adauga si in tabelul de fapte bune
    //se apeleaza o functie din service care sa i notifice pe toti ca o nevoie s-a modificat
    public void onAjutButtonClick(ActionEvent actionEvent) {
        if(tableNevoi.getSelectionModel().getSelectedItem() != null) {
            Nevoie nevoie = tableNevoi.getSelectionModel().getSelectedItem();
            if(nevoie.getStatus().equals(Status.CAUT_EROU)){
                MessageAlert.showSuccesMessage(null,"Ai salvat un om!");
                nevoie.setStatus(Status.EROU_GASIT);
                nevoie.setOmSalvator(user.getId());
                service.updateSalvatorNevoie(nevoie);
            }
        }
    }

    //dupa ce ai completat formularul poti sa-ti adaugi nevoia care se salveaza din db
    public void onAdaugaNevoieButtonClick(ActionEvent actionEvent) {
        String titlu = titluNevoie.getText();
        String descriere = descriereNevoie.getText();
        LocalDate date = deadlineNevoie.getValue();
        LocalDateTime deadline = date.atTime(LocalTime.MIDNIGHT);
        Long omInNevoie = user.getId();
        Status status = Status.CAUT_EROU;
        if(!titlu.isEmpty() && !descriere.isEmpty()) {
            service.saveNevoie(titlu, descriere, deadline, omInNevoie, null, status);
            titluNevoie.clear();
            descriereNevoie.clear();
        }
    }

    //
    @Override
    public void update(ChangeEvent event) {
        initModelFapte();
        initModelNevoi();
    }

}
