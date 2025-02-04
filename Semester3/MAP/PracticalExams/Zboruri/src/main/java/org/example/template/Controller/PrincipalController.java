package org.example.template.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.template.Domain.Client;
import org.example.template.Domain.Flight;

import org.example.template.Service.Service;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PrincipalController implements Observer<ChangeEvent> {
    private Service service;
    private Client client;
    private ObservableList<Flight> model = FXCollections.observableArrayList();
    private final int pageSize = 5;
    private int curentPage = 1;
    private int nrOfPages;

    @FXML
    public Button nextBtn;
    @FXML
    public Button prevBtn;
    @FXML
    public Label pageLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public TableView<Flight> tableView;
    @FXML
    public TableColumn<Flight,String> fromColumn;
    @FXML
    public TableColumn<Flight,String > toColumn;
    @FXML
    public TableColumn<Flight, LocalDateTime> departureTimeColumn;
    @FXML
    public TableColumn<Flight,LocalDateTime> landingTimeColumn;
    @FXML
    public TableColumn<Flight,Integer> seatsColumn;
    @FXML
    public ComboBox<String> fromCombo;
    @FXML
    public ComboBox<String> toCombo;
    @FXML
    public DatePicker datePicker;

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

        pageLabel.setText(curentPage+"/"+nrOfPages);
    }

    public void initCombos(){
        Set<String> from = service.findAllFrom();
        Set<String> to = service.findAllTo();

        fromCombo.getItems().addAll(from);
        toCombo.getItems().addAll(to);
    }

    public void onPrevButtonClick(ActionEvent actionEvent) {
        curentPage--;
        initModel();
    }

    public void onNextButtonClick(ActionEvent actionEvent) {
        curentPage++;
        initModel();
    }

    public void initialize() {
        tableView.setItems(model);
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        landingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("landingTime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));
    }

    public void initModel(){
        if(toCombo.getSelectionModel().getSelectedItem()!=null && fromCombo.getSelectionModel().getSelectedItem()!=null && datePicker.getValue()!=null){
            //iau datele pentru filtrare
            String from = fromCombo.getSelectionModel().getSelectedItem();
            String to = toCombo.getSelectionModel().getSelectedItem();
            int day = datePicker.getValue().getDayOfMonth();

            //iau toate zborurile filtrate si le sortez ca sa le iau mereu corect
            Set<Flight> filtred = service.filtredFlights(to,from,day).stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));

            //iau primele pageSize zboruri de la pageSize*(curentPage-1)
            Set<Flight> flightsOnPage = service.findAllFlightdOnPage(filtred, curentPage-1,pageSize);

            //le sortez sa se pastreze ordinea
            model.setAll(flightsOnPage);

            initNumberOfPages(filtred.size());
            initPageLabel();
        }
    }

    public void afterService(){
        initModel();
        initCombos();
        service.addObserver(this);
    }

    public void setService(Service service) {
        this.service = service;
        afterService();
    }

    public void setClient(Client client) {
        this.client = client;
        setNameLabel();
    }

    public void setNameLabel() {
        nameLabel.setText(client.getName());
    }

    public void onCumparaButtonClick(ActionEvent actionEvent) {
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            Flight flight = tableView.getSelectionModel().getSelectedItem();
            if(flight.getSeats()>0)
                service.addTicket(client.getUsername(),flight.getFlightId(),LocalDateTime.now());
        }
    }

    @Override
    public void update(ChangeEvent event) {
        Flight flight = model.stream().filter(f -> f.getFlightId() == event.getFlight()).findFirst().orElse(null);
        int index = model.indexOf(flight);
        flight.setSeats(flight.getSeats()-1);
        model.set(index, flight);
    }
}
