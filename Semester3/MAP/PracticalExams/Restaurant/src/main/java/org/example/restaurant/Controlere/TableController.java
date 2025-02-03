package org.example.restaurant.Controlere;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.restaurant.Domain.Entitati.MenuItem;
import org.example.restaurant.Service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableController {
    private int idTable;
    private Service service;
    private List<TableView> tableViews = new ArrayList<>();
    @FXML
    public Label LabelTable;
    public ScrollPane container;

    public void setService(Service service) {
        this.service = service;
        afterSetService();
    }

    public void setLabel() {
        LabelTable.setText("Table "+idTable);
    }

    public void setIdTable(int id) {
        idTable = id;
        setLabel();
    }
    
    public void createTables(ScrollPane container, Set<MenuItem> items) {
        Map<String,List<MenuItem>> categories = service.getMenuItemsByCategories(items);
        VBox content = new VBox();
        for(String category : categories.keySet()) {
            Label label = new Label(category);
            TableView<MenuItem> table = new TableView<>();
            table.setPrefHeight(100);

            TableColumn<MenuItem, String> item = new TableColumn<>("Item");
            item.setCellValueFactory(new PropertyValueFactory<>("item"));

            TableColumn<MenuItem, Float> price = new TableColumn<>("Price");
            price.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<MenuItem, String> currency = new TableColumn<>("Currency");
            currency.setCellValueFactory(new PropertyValueFactory<>("currency"));

            table.getColumns().addAll(item, price, currency);
            table.getItems().addAll(categories.get(category));
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableViews.add(table);

            VBox tableContainer = new VBox(label, table);
            content.getChildren().add(tableContainer);
        }
        container.setContent(content);
    }

    public void afterSetService() {
        createTables(container,service.findAllMenuItems());
    }

    public void onPlaceOrderButtonClick(ActionEvent actionEvent) {
        List<Integer> itemIds = new ArrayList<>();
        for(TableView tableView : tableViews) {
            List<MenuItem> items = tableView.getSelectionModel().getSelectedItems();
            itemIds.addAll(items.stream().map(MenuItem::getId).toList());
        }
        service.addOrder(idTable,itemIds);
    }
}
