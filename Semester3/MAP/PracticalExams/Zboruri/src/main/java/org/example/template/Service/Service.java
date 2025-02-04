package org.example.template.Service;

import org.example.template.Domain.Client;
import org.example.template.Domain.Flight;
import org.example.template.Domain.Ticket;

import org.example.template.Repository.ClientRepository;
import org.example.template.Repository.FlightRepository;
import org.example.template.Repository.TicketRepository;
import org.example.template.Utils.Events.ChangeEvent;
import org.example.template.Utils.Events.EventType;
import org.example.template.Utils.Observer.Observable;
import org.example.template.Utils.Observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Service implements Observable<ChangeEvent> {
    private ClientRepository clientRepository;
    private FlightRepository flightRepository;
    private TicketRepository ticketRepository;
    private List<Observer<ChangeEvent>> observers;

    public Service(ClientRepository clientRepository, FlightRepository flightRepository, TicketRepository ticketRepository) {
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.observers = new ArrayList<>();
    }

    public Set<Client> findAllClient(){
        return clientRepository.findAll();
    }

    public Set<Flight> findAllFlights(){
        return flightRepository.findAll();
    }

    public void addTicket(String username, long flightId, LocalDateTime purchaseTime){
        Ticket ticket = new Ticket(username, flightId, purchaseTime);
        ticketRepository.save(ticket);
        ChangeEvent event = new ChangeEvent(EventType.BUY,flightId);
        notify(event);
    }

    public Client logIn(String username){
        for(Client client : clientRepository.findAll()){
            if(client.getUsername().equals(username)){
                return client;
            }
        }
        return null;
    }

    public Set<Flight> filtredFlights(String to, String from, int day){
        Set<Flight> result = new HashSet<>();
        for(Flight flight : flightRepository.findAll()){
            if(flight.getFrom().equals(from) && flight.getTo().equals(to) && flight.getDepartureTime().getDayOfMonth() == day){
                result.add(flight);
            }
        }
        return result;
    }

    public Set<String> findAllFrom(){
        return flightRepository.findAll().stream().collect(Collectors.groupingBy(Flight::getFrom)).keySet();
    }

    public Set<String> findAllTo(){
        return flightRepository.findAll().stream().collect(Collectors.groupingBy(Flight::getTo)).keySet();
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

    public Set<Flight> findAllFlightdOnPage(Set<Flight> items, int page, int pageSize) {
        Set<Flight> result = new LinkedHashSet<>();
        int cnt= -1;
        for(Flight flight : items){
            cnt++;
            if (cnt >= page*pageSize && cnt <= (page*pageSize)+pageSize-1){
                result.add(flight);
            }
        }
        return result;
    }
}
