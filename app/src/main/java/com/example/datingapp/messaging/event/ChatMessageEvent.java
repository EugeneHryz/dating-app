package com.example.datingapp.messaging.event;

import com.example.datingapp.messaging.dto.AbstractChatMessage;

public class ChatMessageEvent extends AbstractChatEvent {

    public ChatMessageEvent(AbstractChatMessage chatMessage) {
        super(chatMessage);
    }
}
