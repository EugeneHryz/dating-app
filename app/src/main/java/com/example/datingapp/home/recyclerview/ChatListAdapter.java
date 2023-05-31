package com.example.datingapp.home.recyclerview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListItemViewHolder> {

    private static final String TAG = ChatListAdapter.class.getName();

    private List<ChatItem> items = new ArrayList<>();

    private final Callback callback;

    public ChatListAdapter(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public ChatListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);

        return new ChatListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListItemViewHolder holder, int position) {
        View view = holder.getView();
        TextView name = view.findViewById(R.id.username);

        if (position >= items.size()) return;

        ChatItem item = items.get(position);
        name.setText(item.getName());
        view.setOnClickListener(v -> callback.onContactItemClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<ChatItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface Callback {

        void onContactItemClicked(ChatItem chatItem);
    }
}
