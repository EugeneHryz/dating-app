package com.example.datingapp.chat.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.datingapp.R;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

public class ConversationAdapter extends MessengerAdapter<ConversationItem, ConversationViewHolder> {

    private static final String TAG = ConversationAdapter.class.getName();

    public ConversationAdapter() {
        super(ConversationItem.class);
    }

    @Override
    public int getItemViewType(int position) {
        ConversationItem item = items.get(position);
        return item.getLayoutRes();
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        ConversationViewHolder viewHolder;
        if (viewType == R.layout.in_msg_list_item || viewType == R.layout.out_msg_list_item) {
            viewHolder = new MessageConversationViewHolder(view);
        } else {
            throw new IllegalArgumentException();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        ConversationItem item = items.get(position);

        View view = holder.getView();
        TextView msgDate = view.findViewById(R.id.date);

        if (needToShowMessageDate(position, item)) {
            msgDate.setVisibility(View.VISIBLE);
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(Locale.ENGLISH)
                    .withZone(ZoneId.systemDefault());
            ZonedDateTime messageDate = item.getTimestamp().atZone(ZoneId.systemDefault());
            msgDate.setText(formatter.format(messageDate));
        } else {
            msgDate.setVisibility(View.GONE);
        }

        holder.bind(item);
    }

    public void addAll(Collection<ConversationItem> newItems) {
        items.beginBatchedUpdates();
        items.addAll(newItems);
        items.endBatchedUpdates();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int compare(ConversationItem t1, ConversationItem t2) {
        return t1.getTimestamp().compareTo(t2.getTimestamp());
    }

    @Override
    public boolean areContentsTheSame(ConversationItem t1, ConversationItem t2) {
        return t1.equals(t2);
    }

    @Override
    public boolean areItemsTheSame(ConversationItem item1, ConversationItem item2) {
        return Objects.equals(item1.getMessageId(), item2.getMessageId());
    }

    private boolean needToShowMessageDate(int position, ConversationItem item) {
        return position == 0 || !isTheSameDay(item.getTimestamp(), items.get(position - 1).getTimestamp());
    }

    private boolean isTheSameDay(Instant time1, Instant time2) {
        ZonedDateTime date1Zoned = time1.atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime date2Zoned = time2.atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
        return date2Zoned.equals(date1Zoned);
    }
}
