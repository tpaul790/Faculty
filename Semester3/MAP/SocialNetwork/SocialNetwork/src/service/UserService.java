package service;

import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.ServiceException;
import repository.database.FriendshipDbRepo;
import repository.database.UserDbRepo;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class UserService extends Service<Integer, User, UserDbRepo>{

    public UserService(UserDbRepo userDbRepo) {
        super(userDbRepo);
    }

    public void save(String username, String email, String password, String role) throws ServiceException {
        User user = new User(username, email, password, role);
        Optional<User> rez = getRepository().save(user);
        Predicate<Optional<User>> present = Optional::isPresent;
        if(present.test(rez)) {
            throw new ServiceException("This user already exists");
        }
    }

    public void update(int id, String username, String email, String password, String role) throws ServiceException {
        User user = new User(id, username, email, password, role);
        Optional<User> rez = super.getRepository().update(user);
        Predicate<Optional<User>> present = Optional::isPresent;
        if(present.test(rez)) {
            throw new ServiceException("User not found");
        }
    }

}
