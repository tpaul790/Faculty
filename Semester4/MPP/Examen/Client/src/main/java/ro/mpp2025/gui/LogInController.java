package ro.mpp2025.gui;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.IServices;
import ro.mpp2025.Player;
import ro.mpp2025.ServiceException;

public class LogInController {
    private IServices server;
    private MainPageController mainPageController;
    private Player currentPlayer;
    private Parent mainParent;
    private static Logger logger = LogManager.getLogger(LogInController.class);
    @FXML
    public TextField nicknameTextField;

    public void setServer(IServices server) {
        this.server = server;
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    public void setParent(Parent parent) {
        this.mainParent = parent;
    }

    public void onLogInButtonClick(ActionEvent actionEvent) {
        logger.info("Trying to login");
        if(!nicknameTextField.getText().isEmpty()) {
            String nickname = nicknameTextField.getText();
            currentPlayer = new Player(nickname);
            try {
                currentPlayer = server.logIn(currentPlayer, mainPageController);
                logger.info("Successfully logged in");

                Stage stage = new Stage();
                stage.setScene(new Scene(mainParent));
                stage.setTitle("Game Page");
                stage.setWidth(600);
                stage.setHeight(400);
                stage.show();

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        mainPageController.logOut();
                        logger.debug("Closing application");
                        System.exit(0);
                    }
                });

                mainPageController.setPlayer(currentPlayer);
                mainPageController.setServer(server);
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            }catch (ServiceException e) {
                MessageAlert.showErrorMessage(null ,e.getMessage());
            }
        }
    }
}
