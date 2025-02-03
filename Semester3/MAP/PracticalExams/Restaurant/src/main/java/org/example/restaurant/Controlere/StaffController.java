package org.example.restaurant.Controlere;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.restaurant.Domain.Entitati.Order;
import org.example.restaurant.Domain.Entitati.OrderDto;
import org.example.restaurant.Observer.Observer;
import org.example.restaurant.Observer.OrderEvent;
import org.example.restaurant.Service.Service;

import java.time.LocalDateTime;

public class StaffController implements Observer<OrderEvent> {
    private Service service;
    @FXML
    private TableView<OrderDto> tableView;
    @FXML
    private TableColumn<OrderDto,Integer> columnIdMasa;
    @FXML
    private TableColumn<OrderDto, LocalDateTime> columnData;
    @FXML
    private TableColumn<OrderDto, String> columnNume;
    @FXML
    private ObservableList<OrderDto> model = FXCollections.observableArrayList();

    public void initialize() {
        columnIdMasa.setCellValueFactory(new PropertyValueFactory<>("idMasa"));
        columnData.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnNume.setCellValueFactory(new PropertyValueFactory<>("numeProdus"));
        tableView.setItems(model);
    }

    public void setService(Service service) {
        this.service = service;
        afterSetService();
    }

    private void afterSetService() {
        initModel();
        service.addObserver(this);
    }

    private void initModel() {
        model.addAll(service.findAllOrdersDto());
    }

    @Override
    public void update(OrderEvent event) {
        model.add(event.getData());
    }
}
