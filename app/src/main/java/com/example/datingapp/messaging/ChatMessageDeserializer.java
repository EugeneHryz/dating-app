package com.example.datingapp.messaging;

import com.example.datingapp.messaging.dto.AbstractChatMessage;
import com.example.datingapp.messaging.dto.AckMessage;
import com.example.datingapp.messaging.dto.MessageRead;
import com.example.datingapp.messaging.dto.MessageType;
import com.example.datingapp.messaging.dto.NewMessage;
import com.example.datingapp.messaging.dto.UserStatusChangeMessage;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ChatMessageDeserializer implements JsonDeserializer<AbstractChatMessage> {

    private static final String TYPE_PROPERTY = "type";

    private final Gson gson;

    public ChatMessageDeserializer() {
        gson = new Gson();
    }

    @Override
    public AbstractChatMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String messageType = jsonObject.getAsJsonPrimitive(TYPE_PROPERTY).getAsString();

        MessageType type = MessageType.valueOf(messageType);
        AbstractChatMessage deserializedMessage;
        switch (type) {
            case MESSAGE_ACK:
                deserializedMessage = gson.fromJson(json, AckMessage.class);
                break;
            case NEW_MESSAGE:
                deserializedMessage = gson.fromJson(json, NewMessage.class);
                break;
            case USER_STATUS_CHANGE:
                deserializedMessage = gson.fromJson(json, UserStatusChangeMessage.class);
                break;
            case MESSAGE_READ:
                deserializedMessage = gson.fromJson(json, MessageRead.class);
                break;
            default:
                throw new JsonParseException("Unknown message type: " + type);
        }
        return deserializedMessage;
    }
}
