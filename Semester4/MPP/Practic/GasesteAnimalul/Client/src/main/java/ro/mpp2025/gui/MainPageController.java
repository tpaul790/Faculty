package ro.mpp2025.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;

import javafx.scene.control.cell.PropertyValueFactory;
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
    private IServices server;
    private Player player;
    private final ObservableList<Game> model = FXCollections.observableArrayList();
    private List<Configuration> configurationList = new ArrayList<>();
    private Configuration currentConfiguration;
    private int nrOfAttempts;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean done;
    private boolean ghicit;
    private static Logger logger = LogManager.getLogger(MainPageController.class);
    public Label directiiLabel;
    public Label incercariLabel;
    public GridPane jocGrid;
    public TableView<Game> clasament;
    public TableColumn<Game,String> nicknameColumn;
    public TableColumn<Game,String> animalColumn;
    public TableColumn<Game,Integer> attemptsColumn;
    public TableColumn<Game, Integer> durationColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTime = LocalDateTime.now();
        nrOfAttempts = 0;
        done = false;
        ghicit = false;
        setTableProperties();
    }

    public void setServer(IServices server) {
        this.server = server;
        afterSetServer();
    }

    public void setPlayer(Player currentPlayer) {
        this.player = currentPlayer;
    }

    public void setAttemptsLabel(int nrOfAttempts) {
        incercariLabel.setText("Incercari: " + nrOfAttempts);
    }

    public void setDirectionLabel(int r, int c, boolean done) {
        if(done) {
            directiiLabel.setText("The game is done");
        }
        else{
            int targetRow = currentConfiguration.getRow();
            int targetCol = currentConfiguration.getColumn();
            int dRow = targetRow - r;
            int dCol = targetCol - c;
            String coord = "";
            if (dRow < 0 && dCol == 0) coord = "N";
            if (dRow > 0 && dCol == 0) coord = "S";
            if (dRow == 0 && dCol > 0) coord = "E";
            if (dRow == 0 && dCol < 0) coord = "V";
            if (dRow < 0 && dCol > 0) coord = "NE";
            if (dRow < 0 && dCol < 0) coord = "NV";
            if (dRow > 0 && dCol > 0) coord = "SE";
            if (dRow > 0 && dCol < 0) coord = "SV";
            directiiLabel.setText(coord);
        }

    }

    //cer toate jocurile si le adaug doar pe alea caastigate sortate dupa durata
    public void initData() {
        try {
            model.setAll(Arrays.stream(server.findAllGames()).filter(Game::isWon).toList());
            model.sort((o1, o2) -> {
                long d1 = Duration.between(o1.getStartTime(),o1.getEndTime()).getSeconds();
                long d2 = Duration.between(o2.getStartTime(),o2.getEndTime()).getSeconds();
                return (int)(d1-d2);
            });
        } catch (ServiceException e) {
            logger.error(e);
        }
    }

    //initializez matricea cu butoane si adaug event uri la fiecare buton
    public void initGrid(){
        final int targetRow = currentConfiguration.getRow();
        final int targetCol = currentConfiguration.getColumn();
        final String path = currentConfiguration.getPath();
        final Boolean[] allow = {true}; //dupa ce s-a terminat, nu mai apelez metoda pentru salvare daca dau click pe alte butoane
        final List<Attempt> attempts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                Button btn = new Button();
                btn.setPrefSize(100, 100);
                btn.setStyle("-fx-background-color: green;");

                // Salvez coordonatele ca final ca să le pot folosi în lambda
                final int r = i;
                final int c = j;

                //adaug eventul
                btn.setOnAction(e -> {
                    //daca mai pot face alegeri si jocul nu i gata
                    if(nrOfAttempts < 3 && !done) {
                        //daca al ghicit atunci schimb cu imagine si marchez ca done si ghicit
                        if (r == targetRow && c == targetCol) {
                            // Afișez imaginea
                            Image img = new Image(path);
                            ImageView imageView = new ImageView(img);
                            imageView.setFitHeight(40);
                            imageView.setFitWidth(80);
                            btn.setGraphic(imageView);
                            btn.setText(""); // Elimin textul
                            btn.setStyle("-fx-background-color: transparent;"); // Scos verde
                            ghicit = true;
                            done = true;
                        } else {
                            btn.setStyle("-fx-background-color: red;");
                        }
                        nrOfAttempts++;
                        Attempt attempt = new Attempt(r,c);
                        attempts.add(attempt);
                    }

                    if(nrOfAttempts == 3)
                        done = true;

                    setAttemptsLabel(nrOfAttempts);
                    setDirectionLabel(r,c,done);

                    if(done && allow[0]){
                        newGameFinished(attempts);
                        btn.setDisable(true);
                        allow[0] = false;
                    }
                });
                jocGrid.add(btn, j, i); // col, row
            }
        }
    }


    public void afterSetServer() {
        //luam o configuratie random
        try {
            configurationList = Arrays.asList(server.findAllConfigurations());
            currentConfiguration = getRandom(configurationList);
            initGrid();
        } catch (ServiceException e) {
            logger.error(e);
            MessageAlert.showErrorMessage(null,"Nu s-a putut alege o configuratie de joc, reporniti aplicatia");
        }
        initData();
    }

    //cand se termina un joc
    public void newGameFinished(List<Attempt> attempts) {
        endTime = LocalDateTime.now();
        Game game = new Game(player, currentConfiguration, ghicit, startTime, endTime);
        for(Attempt attempt : attempts){
            game.addToAttempts(attempt);
        }

        if(ghicit) {
            directiiLabel.setText("You won! Pozition is: (" + currentConfiguration.getRow() + "," + currentConfiguration.getColumn() + ")");
        }
        else{
            directiiLabel.setText("You lost! Pozition is: -1");
        }

        try {
            server.finishGame(game);
        }catch (Exception e){
            logger.error(e);
        }
    }

    public void setTableProperties() {
        clasament.setItems(model);
        nicknameColumn.setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            return new SimpleStringProperty(
                    game.getPlayer() != null ? game.getPlayer().getNickname() : ""
            );
        });
        animalColumn.setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            return new SimpleStringProperty(
                    game.getConfiguration() != null ? game.getConfiguration().getAnimal() : ""
            );
        });
        attemptsColumn.setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            int numAttempts = game.getAttempts() != null ? game.getAttempts().size() : 0;
            return new SimpleIntegerProperty(numAttempts).asObject();
        });
        durationColumn.setCellValueFactory(cellData -> {
            Game game = cellData.getValue();
            int numAttempts = game.getStartTime() != null && game.getEndTime() != null ? (int)Duration.between(game.getStartTime(),game.getEndTime()).getSeconds() : 0;
            return new SimpleIntegerProperty(numAttempts).asObject();
        });
    }

    public Configuration getRandom(List<Configuration> configurationList) {
        int n = configurationList.size();
        int index = (int) (Math.random() * n);
        return configurationList.get(index);
    }


    //observer
    @Override
    public void gameFinished(Game game) throws ServiceException {
        Platform.runLater(this::initData);
    }
}
