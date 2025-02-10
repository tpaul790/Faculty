import domain.User;
import exception.RepoException;
import repo.Network;
import service.NetworkService;
import ui.Ui;

public class Main {
    public static void main(String[] args) throws RepoException {
        Network network = new Network("src/files/users.csv");
        NetworkService networkService = new NetworkService(network);
        Ui ui = new Ui(networkService);
        ui.run();
    }
}