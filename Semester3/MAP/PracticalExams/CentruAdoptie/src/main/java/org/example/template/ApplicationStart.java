package org.example.template;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.template.Controller.AdoptionCentreController;
import org.example.template.Domain.AdoptionCentre;
import org.example.template.Repository.AdoptionCentreRepository;
import org.example.template.Repository.AnimalRepository;
import org.example.template.Service.Service;

import java.io.IOException;
import java.util.Set;

public class ApplicationStart extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initView();
    }

    public static void main(String[] args) {
        launch();
    }

    public void initView() throws IOException {
        AdoptionCentreRepository adoptionCentreRepository = new AdoptionCentreRepository("jdbc:postgresql://localhost:5432/CentruAdoptie","postgres","paul2004");
        AnimalRepository animalRepository = new AnimalRepository("jdbc:postgresql://localhost:5432/CentruAdoptie","postgres","paul2004");

        Service service = new Service(adoptionCentreRepository, animalRepository);

        Set<AdoptionCentre> allAdoptionCentre = service.findAllCentre();

        for(AdoptionCentre adoptionCentre : allAdoptionCentre){
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationStart.class.getResource("centru.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Centru Adoptie");
            stage.show();
            stage.setWidth(600);
            stage.setHeight(400);
            AdoptionCentreController adoptionCentreController = fxmlLoader.getController();
            adoptionCentreController.setAdoptionCentre(adoptionCentre);
            adoptionCentreController.setService(service);
        }
    }
}