package org.example.template;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.template.Controller.LoginController;
import org.example.template.Controller.PrincipalController;
import org.example.template.Domain.Client;
import org.example.template.Domain.Flight;
import org.example.template.Domain.Ticket;
import org.example.template.Domain.Validator.ClientValidator;
import org.example.template.Domain.Validator.FlightValidator;
import org.example.template.Domain.Validator.TicketValidator;
import org.example.template.Domain.Validator.Validator;
import org.example.template.Repository.ClientRepository;
import org.example.template.Repository.FlightRepository;
import org.example.template.Repository.TicketRepository;
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
        Validator<Client> clientValidator = new ClientValidator();
        Validator<Flight> flightValidator = new FlightValidator();
        Validator<Ticket> ticketValidator = new TicketValidator();

        ClientRepository clientRepository = new ClientRepository("jdbc:postgresql://localhost:5432/Zboruri","postgres","paul2004",clientValidator);
        FlightRepository flightRepository = new FlightRepository("jdbc:postgresql://localhost:5432/Zboruri","postgres","paul2004",flightValidator);
        TicketRepository ticketRepository = new TicketRepository("jdbc:postgresql://localhost:5432/Zboruri","postgres","paul2004",ticketValidator);

        Service service = new Service(clientRepository, flightRepository, ticketRepository);

        //initilizez doua ferestre login
        FXMLLoader logInLoader1 = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginPane1 = logInLoader1.load();
        LoginController controller1 = logInLoader1.getController();
        controller1.setService(service);
        controller1.setStage(stage);
        Scene scene1 = new Scene(loginPane1);
        stage.setScene(scene1);
        stage.show();
        stage.setTitle("Zboruri");
        stage.setWidth(600);
        stage.setHeight(400);

        Stage stage2 = new Stage();
        FXMLLoader loginLoader2 = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginPane2 = loginLoader2.load();
        LoginController controller2 = loginLoader2.getController();
        Scene scene2 = new Scene(loginPane2);
        stage2.setScene(scene2);
        controller2.setService(service);
        controller2.setStage(stage2);
        stage2.show();
        stage2.setTitle("Zboruri");
        stage2.setWidth(600);
        stage2.setHeight(400);


    }
}