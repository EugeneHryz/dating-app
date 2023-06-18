package com.example.datingapp.common.recyclerview;

public interface ItemReturningAdapter<I> {

    I getItemAt(int position);

    int getItemCount();
}
