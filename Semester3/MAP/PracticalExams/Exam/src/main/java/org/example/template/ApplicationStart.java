package org.example.template;

import com.sun.source.doctree.UsesTree;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.template.Controller.AdminController;
import org.example.template.Controller.UserController;
import org.example.template.Domain.User;
import org.example.template.Repository.ShowRepository;
import org.example.template.Repository.UserRepository;
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
        ShowRepository showRepository = new ShowRepository("jdbc:postgresql://localhost:5432/Spectacol","postgres","paul2004");
        UserRepository userRepository = new UserRepository("jdbc:postgresql://localhost:5432/Spectacol","postgres","paul2004");

        Service service = new Service(showRepository, userRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationStart.class.getResource("admin.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Admin");
        stage.setWidth(600);
        stage.setHeight(400);
        AdminController controller = fxmlLoader.getController();
        controller.setService(service);

        //creez cate o fereastra pentru fiecare user
        for(User u : service.findAllUsers()){
            Stage st = new Stage();
            FXMLLoader loader = new FXMLLoader(ApplicationStart.class.getResource("user.fxml"));
            Parent parent = loader.load();
            Scene s = new Scene(parent);
            st.setScene(s);
            st.show();
            st.setTitle(u.getName());
            stage.setWidth(600);
            stage.setHeight(400);
            UserController cnt = loader.getController();
            cnt.setService(service);
            cnt.setUser(u);
        }
    }
}