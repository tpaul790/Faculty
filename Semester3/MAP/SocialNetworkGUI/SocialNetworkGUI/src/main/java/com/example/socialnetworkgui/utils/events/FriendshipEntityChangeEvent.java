package com.example.socialnetworkgui.utils.events;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;

public class FriendshipEntityChangeEvent implements Event {
    private FriendshipEventType type;
    private Friendship data,oldData;

    public FriendshipEntityChangeEvent(FriendshipEventType type, Friendship data){
        this.type = type;
        this.data = data;
    }

    public FriendshipEntityChangeEvent(FriendshipEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public FriendshipEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}
