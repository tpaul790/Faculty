package com.example.seminar8;

import com.example.seminar8.Controller.NotaController;
import com.example.seminar8.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestSem8 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestSem8.class.getResource("notaControler-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Sem8!");
        stage.setScene(scene);
        NotaController controller = fxmlLoader.getController();
        Service service = new Service();
        controller.setService(service);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}