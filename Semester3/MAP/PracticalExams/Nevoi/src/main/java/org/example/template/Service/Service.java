package org.example.template.Service;

import javafx.collections.ObservableList;
import org.example.template.Domain.Nevoie;
import org.example.template.Domain.Oras;
import org.example.template.Domain.Person;
import org.example.template.Domain.Status;
import org.example.template.Repository.NevoiRepository;
import org.example.template.Repository.PersoaneRepository;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Events.EventType;
import org.example.template.Utils.Observer.Observable;
import org.example.template.Utils.Observer.Observer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Service implements Observable<ChangeEvent> {
    private PersoaneRepository persoaneRepository;
    private NevoiRepository nevoiRepository;
    private List<Observer<ChangeEvent>> observers;

    public Service(PersoaneRepository persoaneRepository, NevoiRepository nevoiRepository) {
        this.persoaneRepository = persoaneRepository;
        this.nevoiRepository = nevoiRepository;
        this.observers = new ArrayList<>();
    }

    public void savePerson(String name, String prenume, String username, String password, Oras oras, String strada, String numar, String telefon) {
        Person person = new Person(name,prenume,username,password,oras,strada,numar,telefon);
        persoaneRepository.save(person);
    }

    public void saveNevoie(String titlu, String descriere, LocalDateTime deadline, Long omInNevoi, Long omSalvator, Status status){
        Nevoie nevoie = new Nevoie(null, titlu, descriere, deadline, omInNevoi, omSalvator, status);
        nevoiRepository.save(nevoie);
    }

    //cand se face update se anunta toti observerii ca s-a modificat
    public void updateSalvatorNevoie(Nevoie nevoie){
        nevoiRepository.update(nevoie);
        ChangeEvent event = new ChangeEvent(EventType.AJUT,nevoie);
        notify(event);
    }

    public Set<Person> findAllPersons() {
        return persoaneRepository.findAll();
    }

    public Person findPerson(Long id) {
        return persoaneRepository.findOne(id);
    }

    public Set<Nevoie> findAllNevoiForUser(Person user) {
        Set<Nevoie> nevoi = nevoiRepository.findAll();
        Set<Nevoie> result = new HashSet<>();
        for(Nevoie n : nevoi){
            if(!Objects.equals(n.getOmInNevoi(), user.getId())){
                Person p = findPerson(n.getOmInNevoi());
                if(p != null && p.getOras().equals(user.getOras())){
                    result.add(n);
                }
            }
        }
        return result;
    }

    public Set<Nevoie> findAllFapteBuneForUser(Person user) {
        Set<Nevoie> nevoi = nevoiRepository.findAll();
        Set<Nevoie> result = new HashSet<>();
        for(Nevoie n : nevoi){
            if(n.getOmSalvator().equals(user.getId())){
                result.add(n);
            }
        }
        return result;
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
}
