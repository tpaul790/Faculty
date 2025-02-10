package com.example.socialnetworkgui.dto;

import com.example.socialnetworkgui.domain.Tuple;

import java.time.LocalDate;

public class FriendRequestDto {
    private Tuple<Integer,Integer> idFriendship;
    private String sender;
    private String email;
    private LocalDate createDateTime;

    public FriendRequestDto(Tuple<Integer,Integer> idFriendship, String sender, String email, LocalDate createDateTime) {
        this.idFriendship = idFriendship;
        this.sender = sender;
        this.email = email;
        this.createDateTime = createDateTime;
    }

    public Tuple<Integer,Integer> getIdFriendship() {
        return idFriendship;
    }

    public void setIdFriendship(Tuple<Integer,Integer> idFriendship) {
        this.idFriendship = idFriendship;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDate createDateTime) {
        this.createDateTime = createDateTime;
    }
}
