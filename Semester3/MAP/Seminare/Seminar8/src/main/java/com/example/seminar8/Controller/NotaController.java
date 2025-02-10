package com.example.seminar8.Controller;

import com.example.seminar8.Domain.NotaDto;
import com.example.seminar8.Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NotaController {
    private Service service;

    ObservableList<NotaDto> modelNote = FXCollections.observableArrayList();

    @FXML
    private TableView<NotaDto> notaTable;
    @FXML
    private TableColumn<NotaDto, String> studentName;
    @FXML
    private TableColumn<NotaDto, String> idTema;
    @FXML
    private TableColumn<NotaDto, Integer> nota;
    @FXML
    public TextField textFieldNume;
    @FXML
    public TextField textFieldTema;
    @FXML
    public TextField textFieldNota;


    private List<NotaDto> getNotaDTOList() {
        return service.findAllNote()
                .stream()
                .map(n -> new NotaDto(n.getStudent().getName(), n.getTema().getId(), n.getValue(), n.getProfessor()))
                .collect(Collectors.toList());
    }

    @FXML
    public void initialize() {
        studentName.setCellValueFactory(new PropertyValueFactory<>("numeStudent"));
        idTema.setCellValueFactory(new PropertyValueFactory<>("temaId"));
        nota.setCellValueFactory(new PropertyValueFactory<>("nota"));
        notaTable.setItems(modelNote);

        textFieldNume.textProperty().addListener(o -> handleFilter());
        textFieldNota.textProperty().addListener(o -> handleFilter());
        textFieldTema.textProperty().addListener(o -> handleFilter());
    }

    public void initModel(){
        modelNote.setAll(getNotaDTOList());
    }

    public void setService(Service service) {
        this.service = service;
        initModel();
    }

    public List<NotaDto> getNotaDtoList(){
        List<NotaDto> noteDto = new ArrayList<>();
        return noteDto;
    }

    @FXML
    public void handleFilter(){
        Predicate<NotaDto> filtruName = x -> x.getNumeStudent().startsWith(textFieldNume.getText());
        Predicate<NotaDto> filtruNota = x -> x.getNota()>Integer.parseInt(textFieldNota.toString());
        Predicate<NotaDto> filtruTema = x -> x.getTemaId().startsWith(textFieldTema.getText());

        modelNote.setAll(getNotaDTOList().stream()
                .filter(filtruName.and(filtruTema))
                .collect(Collectors.toList()));
    }

}
