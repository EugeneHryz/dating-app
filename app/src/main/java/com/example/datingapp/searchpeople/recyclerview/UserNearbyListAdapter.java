package com.example.datingapp.searchpeople.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.datingapp.R;
import com.example.datingapp.common.recyclerview.AbstractListAdapter;
import com.example.datingapp.common.recyclerview.ListItemViewHolder;

public class UserNearbyListAdapter extends AbstractListAdapter<UserNearbyItem> {

    private static final String TAG = UserNearbyListAdapter.class.getName();

    public UserNearbyListAdapter(EventListener<UserNearbyItem> listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_nearby_list_item, parent, false);

        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        View view = holder.getView();
        if (position >= items.size()) return;

        UserNearbyItem item = items.get(position);
        TextView name = view.findViewById(R.id.username);
        TextView distance = view.findViewById(R.id.distance);
        name.setText(item.getName());
        distance.setText(item.getDistance());
        view.setOnClickListener(v -> listener.onItemClicked(item));
    }
}
