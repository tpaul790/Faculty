package ro.mpp2025;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.gui.LogInController;
import ro.mpp2025.gui.MainPageController;
import ro.mpp2025.object_protocol.ServicesProxy;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    private static Logger logger = LogManager.getLogger(StartClient.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.debug("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            logger.info("Client properties set {} ", clientProps);
        } catch (IOException e) {
            logger.error("Cannot find client.properties " + e);
            logger.debug("Looking into folder {}", (new File(".")).getAbsolutePath());
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            logger.error("Wrong port number " + ex.getMessage());
            logger.debug("Using default port: " + defaultPort);
        }
        logger.info("Using server IP " + serverIP);
        logger.info("Using server port " + serverPort);


        IServices server = new ServicesProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();
        LogInController ctrl = loader.getController();
        ctrl.setServer(server);

        FXMLLoader mloader = new FXMLLoader(getClass().getClassLoader().getResource("joc.fxml"));
        Parent mroot=mloader.load();
        MainPageController mctrl = mloader.getController();

        ctrl.setMainPageController(mctrl);
        ctrl.setParent(mroot);

        primaryStage.setTitle("LogIn");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
