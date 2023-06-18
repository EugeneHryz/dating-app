package com.example.datingapp.messaging.event;

import com.example.datingapp.event.Event;
import com.example.datingapp.messaging.dto.AbstractChatMessage;

public abstract class AbstractChatEvent extends Event {

    protected AbstractChatMessage chatMessage;

    public AbstractChatEvent(AbstractChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public AbstractChatMessage getChatMessage() {
        return chatMessage;
    }
}
