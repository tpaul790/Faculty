import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.FriendshipValidator;
import domain.validation.UserValidator;
import repository.database.FriendshipDbRepo;
import repository.database.UserDbRepo;
import repository.file.FriendshipInFileRepo;
import repository.file.UserInFileRepo;
import service.FriendshipService;
import service.UserService;
import ui.Ui;
import utils.Utils;
import java.util.Optional;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UserValidator userValidator = UserValidator.getInstance();
        FriendshipValidator friendshipValidator = FriendshipValidator.getInstance();

        UserDbRepo userDbRepo = new UserDbRepo("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","paul2004",userValidator);
        FriendshipDbRepo friendshipDbRepo = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","paul2004",friendshipValidator);

        UserService userService = new UserService(userDbRepo);
        FriendshipService friendshipService = new FriendshipService(friendshipDbRepo,userDbRepo);

        Ui ui = new Ui(userService,friendshipService);
        ui.run();
    }
}