package com.example.datingapp.chat.data;

import androidx.annotation.LayoutRes;

import java.time.Instant;
import java.util.Objects;

public abstract class ConversationItem {

    @LayoutRes
    private int layoutRes;
    private Long messageId;
    private Instant timestamp;
    private boolean isIncoming;

    private Status status;
    private boolean highlighted;

    public enum Status {
        SENT,
        SEEN
    }

    public ConversationItem(@LayoutRes int layoutRes, Long id,
                            Instant timestamp, boolean isIncoming) {
        this.layoutRes = layoutRes;
        messageId = id;
        this.timestamp = timestamp;
        this.isIncoming = isIncoming;
    }

    public ConversationItem(@LayoutRes int layoutRes, Instant timestamp,
                            boolean isIncoming) {
        this.layoutRes = layoutRes;
        this.timestamp = timestamp;
        this.isIncoming = isIncoming;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public void setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant receivedOrSentTime) {
        this.timestamp = receivedOrSentTime;
    }

    public boolean isIncoming() {
        return isIncoming;
    }

    public void setIncoming(boolean incoming) {
        isIncoming = incoming;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationItem that = (ConversationItem) o;
        return layoutRes == that.layoutRes
                && isIncoming == that.isIncoming
                && Objects.equals(messageId, that.messageId)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layoutRes, messageId, timestamp, isIncoming);
    }
}
