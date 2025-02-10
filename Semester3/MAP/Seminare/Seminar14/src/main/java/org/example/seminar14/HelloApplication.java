package org.example.seminar14;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.seminar14.Controlere.PrincipalControler;
import org.example.seminar14.Repo.AngajatRepo;
import org.example.seminar14.Service.AngajatiService;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 370, 400);
        stage.setTitle("Seminar14!");
        stage.setScene(scene);
        stage.show();

        PrincipalControler controler = fxmlLoader.getController();
        AngajatRepo angajatiRepo = new AngajatRepo("jdbc:postgresql://localhost:5432/Seminar14","postgres","paul2004");
        AngajatiService angajatiService = new AngajatiService(angajatiRepo);
        controler.setService(angajatiService);
    }

    public static void main(String[] args) {
        launch();
    }
}