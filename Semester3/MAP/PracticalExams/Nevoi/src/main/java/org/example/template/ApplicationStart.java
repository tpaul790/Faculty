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
        PersoaneRepository persoaneRepository = new PersoaneRepository("jdbc:postgresql://localhost:5432/Ajut","postgres","paul2004");
        NevoiRepository nevoiRepository = new NevoiRepository("jdbc:postgresql://localhost:5432/Ajut","postgres","paul2004");

        Service service = new Service(persoaneRepository, nevoiRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationStart.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("LogIn");
        stage.setWidth(401);
        stage.setHeight(300);

        LogInController controller = fxmlLoader.getController();
        controller.setService(service);
        controller.setStage(stage);

        Stage stage1 = new Stage();
        FXMLLoader fxmlLoader1 = new FXMLLoader(ApplicationStart.class.getResource("login.fxml"));
        Parent root1 = fxmlLoader1.load();
        Scene scene1 = new Scene(root1);
        stage1.setScene(scene1);
        stage1.show();
        stage1.setTitle("LogIn");
        stage1.setWidth(401);
        stage1.setHeight(300);

        LogInController controller1 = fxmlLoader1.getController();
        controller1.setService(service);
        controller1.setStage(stage1);


    }
}