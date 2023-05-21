package com.example.datingapp.searchpeople.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserListItemViewHolder extends RecyclerView.ViewHolder {

    private final View userItem;

    public UserListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        userItem = itemView;
    }

    public View getView() {
        return userItem;
    }
}
