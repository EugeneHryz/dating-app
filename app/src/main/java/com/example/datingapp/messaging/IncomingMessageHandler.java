package com.example.datingapp.messaging;

import com.example.datingapp.event.EventBus;
import com.example.datingapp.messaging.dto.AbstractChatMessage;
import com.example.datingapp.messaging.dto.MessageType;
import com.example.datingapp.messaging.event.AbstractChatEvent;
import com.example.datingapp.messaging.event.ChatMessageEvent;
import com.example.datingapp.messaging.event.UserStatusChangeEvent;

import javax.inject.Inject;

public class IncomingMessageHandler {

    private final EventBus eventBus;
    private final MessageMapper messageMapper;

    @Inject
    public IncomingMessageHandler(EventBus eventBus, MessageMapper messageMapper) {
        this.eventBus = eventBus;
        this.messageMapper = messageMapper;
    }

    public void handleIncomingMessage(String payload) {
        AbstractChatMessage chatMessage = messageMapper.deserializeMessage(payload);

        MessageType type = chatMessage.getType();
        AbstractChatEvent eventToBroadcast = null;

        if (isChatMessageGroup(type)) {
            eventToBroadcast = new ChatMessageEvent(chatMessage);
        } else if (isUserStatusChangeGroup(type)) {
            eventToBroadcast = new UserStatusChangeEvent(chatMessage);
        }
        if (eventToBroadcast != null) {
            eventBus.broadcast(eventToBroadcast);
        }
    }

    private boolean isChatMessageGroup(MessageType type) {
        return type == MessageType.MESSAGE_ACK || type == MessageType.NEW_MESSAGE
                || type == MessageType.MESSAGE_READ;
    }

    private boolean isUserStatusChangeGroup(MessageType type) {
        return type == MessageType.USER_STATUS_CHANGE;
    }
}
