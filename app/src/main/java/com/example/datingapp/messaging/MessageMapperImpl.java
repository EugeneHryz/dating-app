package com.example.datingapp.messaging;

import com.example.datingapp.messaging.dto.AbstractChatMessage;
import com.example.datingapp.messaging.exception.InvalidMessageFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javax.inject.Inject;

public class MessageMapperImpl implements MessageMapper {

    private final Gson gson;

    @Inject
    public MessageMapperImpl() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AbstractChatMessage.class, new ChatMessageDeserializer());
        gson = gsonBuilder.create();
    }

    @Override
    public AbstractChatMessage deserializeMessage(String json) {
        try {
            return gson.fromJson(json, AbstractChatMessage.class);
        } catch (JsonSyntaxException e) {
            throw new InvalidMessageFormat("Unable to parse chat message", e);
        }
    }

    @Override
    public String serializeMessage(Object src) {
        return gson.toJson(src);
    }
}
