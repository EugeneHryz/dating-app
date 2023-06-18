package com.example.datingapp.messaging;

import com.example.datingapp.messaging.dto.AbstractChatMessage;

public interface MessageMapper {

    AbstractChatMessage deserializeMessage(String json);

    String serializeMessage(Object src);
}
