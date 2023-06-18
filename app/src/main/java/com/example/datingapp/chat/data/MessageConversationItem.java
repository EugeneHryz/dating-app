package com.example.datingapp.chat.data;

import java.time.Instant;
import java.util.Objects;

public class MessageConversationItem extends ConversationItem {

    private final String content;

    public MessageConversationItem(int layoutRes,
                                   Long id,
                                   Instant timestamp,
                                   boolean isIncoming,
                                   String content) {
        super(layoutRes, id, timestamp, isIncoming);
        this.content = content;
    }

    public MessageConversationItem(int layoutRes,
                                   Instant timestamp,
                                   boolean isIncoming,
                                   String content) {
        super(layoutRes, timestamp, isIncoming);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MessageConversationItem that = (MessageConversationItem) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content);
    }
}
