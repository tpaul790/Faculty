import controller.LogInController;
import domain.Employee;
import domain.Task;
import domain.validator.EmployeeValidator;
import domain.validator.TaskValidator;
import domain.validator.Validator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.EmployeeRepositoryDB;
import repository.TaskRepositoryDB;
import service.Service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ApplicationStart extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        initView();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initView() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getResourceAsStream("./app.config"));
        }catch (Exception e){
            e.printStackTrace();
        }
        Validator<Employee> employeeValidator = new EmployeeValidator();
        Validator<Task> taskValidator = new TaskValidator();

        EmployeeRepositoryDB empRepo = new EmployeeRepositoryDB(properties,employeeValidator);
        TaskRepositoryDB taskRepo = new TaskRepositoryDB(properties,taskValidator);

        Service service = new Service(empRepo, taskRepo);

        List<Employee> employees = service.findAllEmployees();
        System.out.println(employees.size());
        for(int i = 0 ; i < employees.size() + 1; i++){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("login-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            LogInController controller = fxmlLoader.getController();
            controller.setService(service);
            Stage stage = new Stage();
            controller.setStage(stage);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
            stage.setWidth(340);
            stage.setHeight(365);
        }
    }
}
