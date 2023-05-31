package com.example.datingapp.home.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListItemViewHolder extends RecyclerView.ViewHolder {

    private final View chatItem;

    public ChatListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        chatItem = itemView;
    }

    public View getView() {
        return chatItem;
    }
}
