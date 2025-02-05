package org.example.template.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.template.ApplicationStart;
import org.example.template.Domain.*;
import org.example.template.Service.Service;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Events.EventType;
import org.example.template.Utils.Observer.Observer;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class AdoptionCentreController implements Observer<ChangeEvent> {
    private Service service;
    private AdoptionCentre adoptionCentre; //centrul curent
    private ObservableList<HBox> model = FXCollections.observableArrayList();
    public Label numeLabel;
    public Label capacitateLabel;
    public ProgressBar ocupare;
    public Label ocupareLabel;
    public ListView<HBox> listView;
    public ComboBox<Type> tipCombo;

    public void initialize() {
        listView.setItems(model);
        ocupare.setProgress(0);
        //adaug un listener pentru comboBox, de fiecare data cand se modifica sa actualizeze modelul
        tipCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(Type.ALL_ANIMALS)) {
                initModel();
            }else
                handleChangeComboValue(newValue);
        });
    }

    //actualizez modelul cu toate animalele de tipul selectat in combo
    private void handleChangeComboValue(Type newValue) {
        model.clear();
        FiltruDto dto = new FiltruDto(adoptionCentre.getId(),newValue);
        for(Animal animal : service.findAllAnimalFromCentreByType(dto)){
            model.add(initHBox(animal));
        }
    }

    //adaug toate animalalele de la un anumit centru
    public void initModel(){
        model.clear();
        for(Animal animal : service.findAllAnimalFromCentre(adoptionCentre.getId())){
            model.add(initHBox(animal));
        }
    }

    //creez un HBox in care pun animalul si butonul pentru transfer
    public HBox initHBox(Animal animal){
        HBox hBox = new HBox();
        Button button = new Button("SolicitaTransfer");
        button.setOnAction(event -> handleSolicitaTranfer(animal));
        Label label = new Label(animal.toString());
        hBox.setSpacing(200-label.getText().length());
        hBox.getChildren().add(label);
        hBox.getChildren().add(button);
        return hBox;
    }

    //butonul pentru solicitare transfer
    private void handleSolicitaTranfer(Animal animal) {
        service.sendRequest(animal,adoptionCentre.getLocatie(),adoptionCentre.getNume());
    }

    //initializez procentul de ocupare al centrului
    public void initOcupare(){
        int capacitate = adoptionCentre.getCapacitate();
        int size = model.size();
        ocupare.setProgress((double) size /capacitate);
        ocupareLabel.setText("Ocupat: " + (int)(ocupare.getProgress()*100) + "%");
    }

    public boolean isFull() {
        return ocupare.getProgress() == 1;
    }
    //adaug in cmboBox doar tipurile curente prezente in model + unul pentru toate animalele

    public void initTipCombo(){
        Set<Type> tipuri = service.findAllAnimalFromCentre(adoptionCentre.getId()).stream().collect(Collectors.groupingBy(Animal::getTip)).keySet();
        tipCombo.getItems().setAll(tipuri);
        tipCombo.getItems().add(Type.ALL_ANIMALS);
    }
    //metodele care se apeleaza dupa ce am setat service-ul

    public void afterService(){
        initModel();
        initOcupare();
        initTipCombo();
        service.addObserver(this);
    }

    public void setService(Service service) {
        this.service = service;
        afterService();
    }

    public void setAdoptionCentre(AdoptionCentre adoptionCentre) {
        this.adoptionCentre = adoptionCentre;
        initLabels();
    }
    //initializez labelul de nume si cel pentru capacitate imediat dupa ce am setat centrul curent

    public void initLabels(){
        numeLabel.setText(adoptionCentre.getNume());
        capacitateLabel.setText("Capacitate: "+adoptionCentre.getCapacitate());
    }

    public AdoptionCentre getAdoptionCentre() {
        return adoptionCentre;
    }

    /**
     *Observer
     * Daca eventul primit este TRANSFER atunci creez o fereastra noua adresata ferestrei notificate
     * cum ca i-a fost solicitat un transfer, adica asa primeste notificarea
     * Daca eventul este UPDATE atunci initializez diar tot ce trebuie pentru a se vedea transferurile
     * in timp real
     */
    @Override
    public void update(ChangeEvent event) {
        if(event.getEventType().equals(EventType.TRANSFER)) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("request.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (root != null) {
                Scene scene = new Scene(root);
                stage.setScene(scene);
                //titlul imi spune cui ii este adresata notificarea
                stage.setTitle(adoptionCentre.getNume());
                stage.show();
                stage.setWidth(465);
                stage.setHeight(240);
                RequestController controller = loader.getController();
                controller.setService(service);
                //creez dto ul pentru requst
                //fereastra Request are nevoie de id-ul centrului si al animalului pentru update
                //are nevoie si de numele acestora pentru Label
                RequestDto requestDto = new RequestDto();
                requestDto.setIdAnimal(event.getData().getId());
                requestDto.setIdCentru(adoptionCentre.getId());
                requestDto.setNumeAnimal(event.getData().getNume());
                requestDto.setNumeCentru(event.getNumeCentru());
                controller.setDto(requestDto);
            }
        }else{
            initModel();
            initOcupare();
            initTipCombo();
        }
    }
}
