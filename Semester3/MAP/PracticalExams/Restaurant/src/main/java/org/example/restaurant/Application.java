package org.example.restaurant;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.restaurant.Controlere.StaffController;
import org.example.restaurant.Controlere.TableController;
import org.example.restaurant.Domain.Entitati.Table;
import org.example.restaurant.Domain.Entitati.Validatori.MenuItemValidator;
import org.example.restaurant.Domain.Entitati.Validatori.OrderValidator;
import org.example.restaurant.Domain.Entitati.Validatori.TableValidator;
import org.example.restaurant.Repository.MenuItemsRepository;
import org.example.restaurant.Repository.OrdersRepository;
import org.example.restaurant.Repository.TablesRepository;
import org.example.restaurant.Service.Service;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        initView();
    }

    public static void main(String[] args) {
        launch();
    }

    public void initView() throws IOException {
        //construiesc aplicatia
        MenuItemValidator menuItemValidator = MenuItemValidator.getInstance();
        OrderValidator orderValidator = OrderValidator.getInstance();
        TableValidator tableValidator = TableValidator.getInstance();

        MenuItemsRepository menuItemsRepository = new MenuItemsRepository("jdbc:postgresql://localhost:5432/Restaurant","postgres","paul2004",menuItemValidator);
        OrdersRepository ordersRepository = new OrdersRepository("jdbc:postgresql://localhost:5432/Restaurant","postgres","paul2004",orderValidator);
        TablesRepository tablesRepository = new TablesRepository("jdbc:postgresql://localhost:5432/Restaurant","postgres","paul2004",tableValidator);

        Service service = new Service(menuItemsRepository, ordersRepository, tablesRepository);


        //afisez ferestra pentru staff
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("staff.fxml"));
        AnchorPane paneStaff = fxmlLoader.load();
        StaffController staffController = fxmlLoader.getController();
        staffController.setService(service);
        Scene staffScene = new Scene(paneStaff);
        Stage staffStage = new Stage();
        staffStage.setScene(staffScene);
        staffStage.setTitle("Staff");
        staffStage.setWidth(430);
        staffStage.setHeight(490);
        staffStage.show();

        //afisez cate o fereastra pentru fiecare masa
        for(Table table : service.findAllTables()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
            AnchorPane tablePane = loader.load();
            TableController controller = loader.getController();
            controller.setService(service);
            controller.setIdTable(table.getId());
            Scene tableScene = new Scene(tablePane);
            Stage tableStage = new Stage();
            tableStage.setScene(tableScene);
            tableStage.setTitle("Table");
            tableStage.setWidth(304);
            tableStage.setHeight(441);
            tableStage.show();

        }
    }
}