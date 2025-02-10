package com.example.socialnetworkgui.domain.validation;

import com.example.socialnetworkgui.domain.Message;

public class MessageValidator implements Validator<Message> {
    private static MessageValidator instance = null;

    public static MessageValidator getInstance() {
        if(instance == null) {
            instance = new MessageValidator();
        }
        return instance;
    }

    @Override
    public void validate(Message message) throws ValidationException {
        StringBuilder builder = new StringBuilder();
        if(message.getSender().isEmpty())
            builder.append("Sender cannot be empty");
        if(message.getReceiver().isEmpty())
            builder.append("Receiver cannot be empty");

        if(!builder.toString().isEmpty())
            throw new ValidationException(builder.toString());
    }
}
