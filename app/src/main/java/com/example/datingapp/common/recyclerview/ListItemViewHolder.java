package com.example.datingapp.common.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListItemViewHolder extends RecyclerView.ViewHolder {

    protected final View view;

    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public View getView() {
        return view;
    }
}
