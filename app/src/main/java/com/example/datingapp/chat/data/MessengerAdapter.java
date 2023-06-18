package com.example.datingapp.chat.data;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.SortedList;

import com.example.datingapp.common.recyclerview.ItemReturningAdapter;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class MessengerAdapter<T, V extends ViewHolder> extends RecyclerView.Adapter<V>
        implements ItemReturningAdapter<T> {

    protected final SortedList<T> items;

    public MessengerAdapter(Class<T> clazz) {

        items = new SortedList<T>(clazz, new SortedList.Callback<T>() {
            @Override
            public int compare(T o1, T o2) {
                return MessengerAdapter.this.compare(o1, o2);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return MessengerAdapter.this.areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return MessengerAdapter.this.areItemsTheSame(item1, item2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        }, 0);
    }

    @Override
    public T getItemAt(int position) {
        return items.get(position);
    }

    public int findIndexOfFirstMatching(Predicate<T> predicate) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (predicate.test(items.get(i))) {
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

    public void updateRangeOfItems(Predicate<T> condition, Consumer<T> updater) {
        int firstIndex = -1;
        int count = 0;
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            boolean satisfies = condition.test(item);
            if (satisfies) {
                if (firstIndex == -1) {
                    firstIndex = i;
                }
                count++;
                updater.accept(item);
            }
            if (firstIndex != -1 && !satisfies) {
                break;
            }
        }
        if (firstIndex != -1) {
            notifyItemRangeChanged(firstIndex, count);
        }
    }

    public abstract int compare(T t1, T t2);

    public abstract boolean areContentsTheSame(T t1, T t2);

    public abstract boolean areItemsTheSame(T item1, T item2);

    public void replaceAll(Collection<T> newItems) {
        items.replaceAll(newItems);
    }

    public void addItem(T item) {
        items.add(item);
    }
}
