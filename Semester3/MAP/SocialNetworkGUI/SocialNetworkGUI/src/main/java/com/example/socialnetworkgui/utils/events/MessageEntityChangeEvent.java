package com.example.socialnetworkgui.utils.events;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Message;

public class MessageEntityChangeEvent implements Event{
    private MessageEventType type;
    private Message data,oldData;

    public MessageEntityChangeEvent(MessageEventType type, Message data){
        this.type = type;
        this.data = data;
    }

    public MessageEntityChangeEvent(MessageEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public MessageEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }
}
