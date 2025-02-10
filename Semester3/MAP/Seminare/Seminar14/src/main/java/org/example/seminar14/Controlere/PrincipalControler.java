package org.example.seminar14.Controlere;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.seminar14.Domain.Angajat;
import org.example.seminar14.Service.AngajatiService;

import java.util.List;
import java.util.stream.StreamSupport;

public class PrincipalControler {
    private AngajatiService angajatiService;
    private ObservableList<Angajat> model = FXCollections.observableArrayList();
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn<Angajat,String> tableColumnNume;
    @FXML
    public TableColumn<Angajat,String> tableColumnSenioritate;
    @FXML
    public TableColumn<Angajat, Double> tableColumnVenit;

    public void initialize() {
        tableView.setItems(model);
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnSenioritate.setCellValueFactory(new PropertyValueFactory<>("senioritate"));
        tableColumnVenit.setCellValueFactory(new PropertyValueFactory<>("venitPeOra"));
    }

    public void setService(AngajatiService angajatiService) {
        this.angajatiService = angajatiService;
        initModel();
    }

    public void initModel(){
        List<Angajat> angajati = angajatiService.getAllAngajati();
        model.addAll(StreamSupport.stream(angajati.spliterator(), false).toList());
    }
}
