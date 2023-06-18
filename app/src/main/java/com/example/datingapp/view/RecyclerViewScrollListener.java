package com.example.datingapp.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingapp.common.recyclerview.ItemReturningAdapter;

import java.util.Objects;

public class RecyclerViewScrollListener<A extends ItemReturningAdapter<I>, I> extends RecyclerView.OnScrollListener {

    private final A adapter;
    private final Callback<I> callback;

    private int previousLastVisibleItemPos = RecyclerView.NO_POSITION;
    private int previousItemCount = 0;

    public RecyclerViewScrollListener(A adapter, Callback<I> callback) {
        this.adapter = adapter;
        this.callback = callback;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager)
                Objects.requireNonNull(recyclerView.getLayoutManager());

        int lastVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = adapter.getItemCount();
        if (lastVisibleItemPos != previousLastVisibleItemPos || itemCount != previousItemCount) {
            if (lastVisibleItemPos != -1) {
                I item = adapter.getItemAt(lastVisibleItemPos);
                callback.onLastItemVisible(item);

                previousLastVisibleItemPos = lastVisibleItemPos;
                previousItemCount = itemCount;
            }
        }

        int firstVisibleItemPos = layoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPos == 0) {
            callback.onReachedTop();
        }
        if (lastVisibleItemPos == itemCount - 1) {
            callback.onReachedBottom();
        }
    }

    public interface Callback<I> {

        void onLastItemVisible(I item);

        void onReachedTop();

        void onReachedBottom();
    }
}
