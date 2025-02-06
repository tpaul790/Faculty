package org.example.template.Service;

import org.example.template.Domain.Show;
import org.example.template.Domain.User;
import org.example.template.Repository.ShowRepository;
import org.example.template.Repository.UserRepository;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Events.Event;
import org.example.template.Utils.Events.EventType;
import org.example.template.Utils.Observer.Observable;
import org.example.template.Utils.Observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Service implements Observable<ChangeEvent>{
    private ShowRepository showRepository;
    private UserRepository userRepository;
    private List<Observer<ChangeEvent>> observers;

    public Service(ShowRepository showRepository, UserRepository userRepository) {
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        this.observers = new ArrayList<>();
    }

    //users
    public Set<User> findAllUsers(){
        return userRepository.findAll();
    }

    //shows
    public void saveShow(String name, int durationMinutes, LocalDateTime startDate, int nrOfSeats){
        Show show = new Show(name,durationMinutes,startDate,nrOfSeats,null);
        showRepository.save(show);
        ChangeEvent event = new ChangeEvent(EventType.ADD_SPECTACOL,show);
        notify(event);
    }

    public Set<Show> findAllShows(){
        return showRepository.findAll();
    }

    public Show validate(LocalDateTime start, int duration) {
        LocalDateTime end = start.plusMinutes(duration);
        for(Show show : findAllShows() ){
            LocalDateTime showEnd = show.getStartDate().plusMinutes(show.getDurationMinutes());
            if (start.isAfter(show.getStartDate()) && end.isBefore(showEnd)) {
                return show;
            }
        }
        return null;
    }

    public Set<Show> findAllShowsOnPage(int curentPage, int pageSize) {
        return showRepository.findAllOnPage(curentPage,pageSize);
    }

    @Override
    public void addObserver(Observer<ChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(ChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }

    public void rezervaLoc(Show show) {
        ChangeEvent event = new ChangeEvent(EventType.RESERVED,show);
        notify(event);
    }
}
