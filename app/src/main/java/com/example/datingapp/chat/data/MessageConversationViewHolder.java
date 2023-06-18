package com.example.datingapp.chat.data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.datingapp.R;
import com.vanniktech.emoji.EmojiTextView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MessageConversationViewHolder extends ConversationViewHolder {

    private final TextView timeView;
    private final EmojiTextView textView;
    private final ImageView statusView;

    public MessageConversationViewHolder(@NonNull View itemView) {
        super(itemView);

        statusView = itemView.findViewById(R.id.message_status);
        timeView = itemView.findViewById(R.id.timestamp);
        textView = itemView.findViewById(R.id.text);
    }

    @Override
    public void bind(ConversationItem item) {
        if (!(item instanceof MessageConversationItem)) {
            throw new IllegalArgumentException("Must pass MessageConversationItem as a parameter");
        }
        MessageConversationItem messageItem = (MessageConversationItem) item;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                .withZone(ZoneId.systemDefault());
        timeView.setText(formatter.format(item.getTimestamp()));
        textView.setText(messageItem.getContent());
        if (item.isHighlighted()) {
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.highlighted_message));
        } else {
            itemView.setBackground(null);
        }

        if (!item.isIncoming()) {
            if (item.getStatus() != null) {
                int imageResource = item.getStatus() == ConversationItem.Status.SENT ?
                        R.drawable.message_sent : R.drawable.message_seen;
                statusView.setImageResource(imageResource);
            } else {
                statusView.setImageDrawable(null);
            }
        }
    }
}
