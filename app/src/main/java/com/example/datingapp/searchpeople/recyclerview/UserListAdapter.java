package com.example.datingapp.searchpeople.recyclerview;

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

public class UserListAdapter extends RecyclerView.Adapter<UserListItemViewHolder> {

    private static final String TAG = UserListAdapter.class.getName();

    private List<UserItem> items = new ArrayList<>();

    private final Callback callback;

    public UserListAdapter(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public UserListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_nearby_list_item, parent, false);

        return new UserListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListItemViewHolder holder, int position) {
        View view = holder.getView();
        TextView name = view.findViewById(R.id.username);
        TextView distance = view.findViewById(R.id.distance);

        if (position >= items.size()) return;

        UserItem item = items.get(position);
        name.setText(item.getName());
        distance.setText(item.getDistance());
        view.setOnClickListener(v -> callback.onContactItemClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<UserItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

//    public void updateItem(UserItem updated) {
//        UserItem itemToUpdate = null;
//        for (UserItem c : items) {
//            if (c.equals(updated)) {
//                itemToUpdate = c;
//                break;
//            }
//        }
//        if (itemToUpdate != null) {
//            int position = items.indexOf(itemToUpdate);
//            items.set(position, updated);
//            notifyItemChanged(position);
//        }
//    }

    public interface Callback {

        void onContactItemClicked(UserItem userItem);
    }
}
