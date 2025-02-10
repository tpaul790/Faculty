package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.Date;

public class Message extends Entity<Integer> {
    private int idSender;
    private int idReceiver;
    private String message;
    private String sender;
    private String receiver;
    private LocalDateTime date;
    private int replayId;

    public Message(int id,int idSender, int idReceiver, String sender, String receiver, String message, LocalDateTime date) {
        setId(id);
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
    }

    public Message(int idSender, int idReceiver, String sender, String receiver, String message, LocalDateTime date) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
    }

    public Message(int id, String sender, String receiver, String message, LocalDateTime date) {
        setId(id);
        this.idSender = 0;
        this.idReceiver = 0;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
    }


    public int getIdSender() {
        return idSender;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getReplayId() {
        return replayId;
    }

    public void setReplayId(int replayId) {
        this.replayId = replayId;
    }

    @Override
    public String toString() {
        return sender+","+receiver+":"+message+"("+date+")";
    }
}
