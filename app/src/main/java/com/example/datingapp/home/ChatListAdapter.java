package com.example.datingapp.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.datingapp.R;
import com.example.datingapp.common.recyclerview.AbstractListAdapter;
import com.example.datingapp.common.recyclerview.ListItemViewHolder;
import com.example.datingapp.messaging.dto.UserStatus;

public class ChatListAdapter extends AbstractListAdapter<ChatItem> {

    private static final String TAG = ChatListAdapter.class.getName();

    public ChatListAdapter(EventListener<ChatItem> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        View view = holder.getView();

        if (position >= items.size()) return;

        ChatItem item = items.get(position);
        TextView name = view.findViewById(R.id.chat_name);
        ImageView status = view.findViewById(R.id.user_status);
        int imgRes = item.getStatus() == UserStatus.ONLINE ? R.drawable.ic_contact_online
                : R.drawable.ic_contact_offline;

        status.setImageResource(imgRes);
        name.setText(item.getName());
        view.setOnClickListener(v -> {
            listener.onItemClicked(item);
        });
    }

}
