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
        //fac un map unde cheia e categoria si valoare un vector cu toti itemii din meniu de la categoria aia
        Map<String,List<MenuItem>> categories = service.getMenuItemsByCategories(items);
        VBox content = new VBox();
        for(String category : categories.keySet()) {
            //label cu categoria si tabelul corespunzator
            Label label = new Label(category);
            TableView<MenuItem> table = new TableView<>();
            table.setPrefHeight(100);

            //creez coloanele pentru tabele
            TableColumn<MenuItem, String> item = new TableColumn<>("Item");
            item.setCellValueFactory(new PropertyValueFactory<>("item"));

            TableColumn<MenuItem, Float> price = new TableColumn<>("Price");
            price.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<MenuItem, String> currency = new TableColumn<>("Currency");
            currency.setCellValueFactory(new PropertyValueFactory<>("currency"));

            //adaug coloanele si datele in tabele
            table.getColumns().addAll(item, price, currency);
            table.getItems().addAll(categories.get(category));
            table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            //adaug tabelele intr un array pentru a putea face comenzile
            tableViews.add(table);

            //creez un vbox intre label si tabel pe care il adaug intr unul principal
            VBox tableContainer = new VBox(label, table);
            content.getChildren().add(tableContainer);
        }
        //adaug vbox ul principal in scroll pane ca sa fie frumos
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
