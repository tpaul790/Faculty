package org.example.template;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.template.Controller.LogInController;
import org.example.template.Repository.NevoiRepository;
import org.example.template.Repository.PersoaneRepository;
import org.example.template.Service.Service;

import java.io.IOException;

public class ApplicationStart extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initView(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    public void initView(Stage stage) throws IOException {
        PersoaneRepository persoaneRepository = new PersoaneRepository("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","paul2004");
        NevoiRepository nevoiRepository = new NevoiRepository("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","paul2004");

        Service service = new Service(persoaneRepository, nevoiRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationStart.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("LogIn");
        stage.setWidth(290);
        stage.setHeight(200);

        LogInController controller = fxmlLoader.getController();


    }
}