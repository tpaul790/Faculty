package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validation.ServiceException;
import com.example.socialnetworkgui.repository.database.UserDbRepo;
import com.example.socialnetworkgui.utils.events.ChangeEventType;
import com.example.socialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class UserService extends Service<Integer, User, UserDbRepo> implements Observable<UserEntityChangeEvent> {
    private List<Observer<UserEntityChangeEvent>> observers;

    public UserService(UserDbRepo userDbRepo) {
        super(userDbRepo);
        observers = new ArrayList<>();
    }

    public void save(String username, String email, String password, String role) throws ServiceException {
        User user = new User(username, email, password, role);
        Optional<User> rez = getRepository().save(user);
        Predicate<Optional<User>> present = Optional::isPresent;
        if(present.test(rez)) {
            throw new ServiceException("This user already exists");
        }
        notify(new UserEntityChangeEvent(ChangeEventType.ADD, user));
    }

    public void delete(int id) {
        Optional<User> user = getRepository().delete(id);
        if(user.isEmpty()){
            throw new ServiceException("Entity not found");
        }
        user.ifPresent(u -> notify(new UserEntityChangeEvent(ChangeEventType.DELETE,u)));
    }

    public void update(int id, String username, String email, String password, String role, String path, String aboutMe) throws ServiceException {
        User user = new User(id, username, email, password, role);
        user.setProfilePicture(path);
        user.setAboutMe(aboutMe);
        Optional<User> oldUser = getRepository().findOne(user.getId());
        if(oldUser.isPresent()) {
            Optional<User> rez = super.getRepository().update(user);
            if(rez.isPresent()) {
                throw new ServiceException("User not found");
            }
            notify(new UserEntityChangeEvent(ChangeEventType.UPDATE, user, oldUser.get()));
        }
    }

    public void changeProfilePicure(User user, String path) throws ServiceException {
        Optional<User> oldUser = getRepository().findOne(user.getId());
        if(oldUser.isPresent()) {
            oldUser.get().setProfilePicture(path);
            Optional<User> rez = super.getRepository().update(user);
            if(rez.isPresent()) {
                throw new ServiceException("User not found");
            }
            notify(new UserEntityChangeEvent(ChangeEventType.UPDATE, user, oldUser.get()));
        }
    }

    public User findOne(String username) throws ServiceException {
        Optional<User> optionalUser = super.getRepository().findOne(username);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new ServiceException("User not found");
    }

    @Override
    public void addObserver(Observer<UserEntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(UserEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
