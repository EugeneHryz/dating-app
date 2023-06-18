package com.example.datingapp.common.recyclerview;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractListAdapter<T> extends RecyclerView.Adapter<ListItemViewHolder> {

    protected List<T> items = new ArrayList<>();

    protected final EventListener<T> listener;

    public AbstractListAdapter(EventListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public int findFirst(Predicate<T> tester) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (tester.test(items.get(i))) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void updateItemAtIndex(int index, Consumer<T> updater) {
        T itemToUpdate = items.get(index);
        updater.accept(itemToUpdate);
        notifyItemChanged(index);
    }

    public interface EventListener<T> {

        void onItemClicked(T item);
    }
}
