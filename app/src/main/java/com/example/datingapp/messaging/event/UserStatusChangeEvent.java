package com.example.datingapp.messaging.event;

import com.example.datingapp.messaging.dto.AbstractChatMessage;

public class UserStatusChangeEvent extends AbstractChatEvent {

    public UserStatusChangeEvent(AbstractChatMessage chatMessage) {
        super(chatMessage);
    }
}
