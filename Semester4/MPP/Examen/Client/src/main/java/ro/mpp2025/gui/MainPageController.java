package ro.mpp2025.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.*;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class MainPageController implements Initializable, IObserver {
    public TableView<Game> clasament;
    public TableColumn<String,Game> nicknameColumn;
    public TableColumn<String,Game> configurationColumn;
    public GridPane gameGrid;
    public Label puncteLabel;
    public Label pozitieLabel;
    public Label dezvaluireLabel;
    public int incercari;
    public int correct;

    private IServices server;
    private Player player;
    private Configuration configuration;
    private List<String> words;
    private List<Button> buttons;
    LocalDateTime start;
    private List<Integer> revealedPositions = new ArrayList<>();
    private List<Integer> chosenPositions = new ArrayList<>();
    private static Logger logger = LogManager.getLogger(MainPageController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setProperties();
        incercari = 0;
        start = LocalDateTime.now();
        correct = 0;
    }

    public void initGrid(){
        buttons = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            Button btn = new Button("?");
            btn.setPrefSize(80, 40);
            final int index = i;

            btn.setOnAction(e -> onButtonClick(index));
            buttons.add(btn);
        }

        int rows = 2;
        int cols = 5;
        gameGrid.getChildren().clear();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int index = row * cols + col;
                gameGrid.add(buttons.get(index), col, row);
            }
        }
    }


    public void onButtonClick(int index){
        // Dacă e deja dezvăluită, ieșim
        if (revealedPositions.contains(index) || chosenPositions.contains(index)) return;
        if(incercari == 3)
            finishGame();

        // Arătăm cuvântul
        Button btn = buttons.get(index);
        btn.setText(words.get(index));
        chosenPositions.add(index);

        if (chosenPositions.size() == 2) {
            checkPair();
            incercari++;
        }
    }

    private void checkPair() {
        int pos1 = chosenPositions.get(0);
        int pos2 = chosenPositions.get(1);

        if (words.get(pos1).equals(words.get(pos2))) {
            // Potrivire => le păstrăm vizibile
            correct++;
            revealedPositions.add(pos1);
            revealedPositions.add(pos2);
            if(revealedPositions.size() == 10){
                finishGame();
            }
        } else {
            // Dacă nu se potrivesc => ascunde-le după o mică pauză
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // 1 secundă
                } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(() -> {
                    buttons.get(pos1).setText("?");
                    buttons.get(pos2).setText("?");
                });
            }).start();
        }
        chosenPositions.clear();
    }

    public void finishGame(){
        disableButtons();
        LocalDateTime end = LocalDateTime.now();
        Game game = new Game(player,configuration,start,end,correct);
//        server.saveGame(game);
    }

    public void disableButtons(){
        for(Button button : buttons){
            button.setDisable(true);
        }
    }

    public void setProperties() {

    }

    public void setServer(IServices server) {
        this.server = server;
        afterSetServer();
    }

    public void setPlayer(Player currentPlayer) {
        player = currentPlayer;
    }

    public void initData() {
        try {
            configuration = server.getConfiguration();
            words = getWordsFromConfiguration(configuration);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getWordsFromConfiguration(Configuration configuration){
        List<String> w = new ArrayList<>();
        for(Animal a : configuration.getAnimals()){
            w.add(a.getName());
            w.add(a.getName());
        }
        Collections.shuffle(w);
        return w;
    }

    public void afterSetServer() {
        initData();
        initGrid();
    }

    public void onLogOutButtonClick(ActionEvent actionEvent) {
        logOut();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    void logOut() {
        try {
            server.logOut(player, this);
        } catch (ServiceException e) {
            logger.error("Logout error " + e);
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
