package com.example.datingapp.chat.data;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.datingapp.common.recyclerview.ListItemViewHolder;

public abstract class ConversationViewHolder extends ListItemViewHolder {

    public ConversationViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(ConversationItem item);
}
