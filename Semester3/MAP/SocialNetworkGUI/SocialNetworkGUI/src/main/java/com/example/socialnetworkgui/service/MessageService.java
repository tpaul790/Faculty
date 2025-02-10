package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.validation.ServiceException;
import com.example.socialnetworkgui.repository.database.MessageDbRepo;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.MessageEventType;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MessageService extends Service<Integer, Message, MessageDbRepo> implements Observable<MessageEntityChangeEvent> {
    private List<Observer<MessageEntityChangeEvent>> observers = new ArrayList<>();

    public MessageService(MessageDbRepo repository) {
        super(repository);
    }

    public void save(int senderId, int receiverId, String message,String sender,String receiver){
        Message mes = new Message(senderId,receiverId,sender,receiver,message,LocalDateTime.now());
        Optional<Message> optional = getRepository().save(mes);
        if(optional.isPresent()){
            throw new ServiceException("This message already exist!");
        }
        notify(new MessageEntityChangeEvent(MessageEventType.SEND,mes));
    }

    public void update(int senderId, int receiverId, String message,String sender,String receiver, LocalDateTime date, int replayId){
        Message mes = new Message(senderId,receiverId,sender,receiver,message,date);
        Optional<Message> optional = getRepository().update(mes);
        if(optional.isPresent()){
            throw new ServiceException("Message cannot be updated!");
        }
    }

    public Set<Message> findAllFor(int senderId, int receiverId){
        Set<Message> messages = (Set<Message>) getRepository().findAll();
        Predicate<Message> predicate = m -> (m.getIdSender() != senderId || m.getIdReceiver() != receiverId) && (m.getIdSender() != receiverId || m.getIdReceiver() != senderId) ;
        System.out.println(messages.size());
        messages.removeIf(predicate);
        System.out.println(messages.size());
        messages = messages.stream().sorted(Comparator.comparing(Message::getDate)).collect(Collectors.toCollection(LinkedHashSet::new));
        return messages;
    }

    @Override
    public void addObserver(Observer<MessageEntityChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<MessageEntityChangeEvent> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(MessageEntityChangeEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
