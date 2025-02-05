package org.example.template.Service;

import org.example.template.Controller.AdoptionCentreController;
import org.example.template.Domain.AdoptionCentre;
import org.example.template.Domain.Animal;
import org.example.template.Domain.FiltruDto;
import org.example.template.Repository.AdoptionCentreRepository;
import org.example.template.Repository.AnimalRepository;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Events.EventType;
import org.example.template.Utils.Observer.Observable;
import org.example.template.Utils.Observer.Observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Service implements Observable<ChangeEvent> {
    private AdoptionCentreRepository adoptionCentreRepository;
    private AnimalRepository animalRepository;
    private List<Observer<ChangeEvent>> observers;

    public Service(AdoptionCentreRepository adoptionCentreRepository, AnimalRepository animalRepository) {
        this.adoptionCentreRepository = adoptionCentreRepository;
        this.animalRepository = animalRepository;
        this.observers = new ArrayList<>();
    }

    public Set<AdoptionCentre> findAllCentre(){
        return adoptionCentreRepository.findAll();
    }

    public Set<Animal> findAllAnimal(){
        return animalRepository.findAll();
    }

    public Set<Animal> findAllAnimalFromCentre(Integer id) {
        Set<Animal> result = new HashSet<>();
        for(Animal animal : findAllAnimal()){
            if(id == animal.getCentruId()){
                result.add(animal);
            }
        }
        return result;
    }

    public Set<Animal> findAllAnimalFromCentreByType(FiltruDto dto){
        return animalRepository.findAllFiltred(dto);
    }

    public void updateCentreToAnimal(int idAnimal, int idNewCentre){
        Animal animal = new Animal("",idNewCentre,null);
        animal.setId(idAnimal);
        animalRepository.update(animal);
    }

    public void sendRequest(Animal animal, String locatie, String numeCentru) {
        ChangeEvent event = new ChangeEvent(EventType.TRANSFER, locatie, animal);
        event.setNumeCentru(numeCentru);
        notify(event);
    }

    public void sendUpdateToAllObservers() {
        ChangeEvent event = new ChangeEvent(EventType.UPDATE,null,null);
        notify(event);
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
        //daca eventul este TRANSFER atunci notific pe toti din aceeasi locatie cu exceptia mea
        //daca eventul este UPDATE atunci notific pe toti din aceeasi locatie cu mine inclusiv pe minte
        for(Observer<ChangeEvent> observer : observers){
            AdoptionCentreController adoptionCentreController = (AdoptionCentreController)observer;
            if(adoptionCentreController.getAdoptionCentre().getLocatie().equals(event.getLocatie())) {
                if (event.getEventType().equals(EventType.TRANSFER) && !adoptionCentreController.isFull())
                    if (adoptionCentreController.getAdoptionCentre().getId() != event.getData().getCentruId())
                        observer.update(event);
            }else if(event.getEventType().equals(EventType.UPDATE))
                observer.update(event);

        }
    }
}
