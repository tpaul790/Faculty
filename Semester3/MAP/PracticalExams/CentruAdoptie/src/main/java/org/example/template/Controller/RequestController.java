package org.example.template.Controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.template.Domain.Animal;
import org.example.template.Domain.RequestDto;
import org.example.template.Service.Service;

public class RequestController {
    private Service service;
    private RequestDto dto;
    public Label requestLabel;

    public void setService(Service service) {
        this.service = service;
    }

    public void setDto(RequestDto dto) {
        this.dto = dto;
        requestLabel.setText("Centrul "+dto.getNumeCentru()+" a solicitat transferul animalului "+dto.getNumeAnimal());
    }

    //se face modificarea in baza de date si se notifica toti observerii ca sa faca update
    public void onAcceptButtonClick(ActionEvent actionEvent) {
        service.updateCentreToAnimal(dto.getIdAnimal(), dto.getIdCentru());
        service.sendUpdateToAllObservers();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    //se inchide fereastra
    public void onRespingeButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
