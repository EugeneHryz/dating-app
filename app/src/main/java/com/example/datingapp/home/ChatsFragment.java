package com.example.datingapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.chat.ConversationActivity;
import com.example.datingapp.fragment.BaseFragment;
import com.example.datingapp.messaging.dto.UserStatusChangeMessage;
import com.example.datingapp.view.MessengerRecyclerView;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChatsFragment extends BaseFragment {

    private static final String TAG = ChatsFragment.class.getName();

    private HomeViewModel viewModel;

    private MessengerRecyclerView chatList;
    private ChatListAdapter adapter;

    @Override
    protected void injectFragment(ActivityComponent activityComponent) {
        activityComponent.inject(this);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);

        chatList = view.findViewById(R.id.chat_list);
        setupRecyclerView();

        viewModel.getState().observe(getViewLifecycleOwner(), this::handleStateChange);
        viewModel.getUserStatusChange().observe(getViewLifecycleOwner(), this::handleStatusChange);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadChats();
    }

    @Override
    public String getUniqueTag() {
        return TAG;
    }

    private void handleStateChange(HomeViewModel.State state) {
        if (state == HomeViewModel.State.CHATS_LOADED) {
            adapter.setItems(viewModel.getChatItems());
        }
    }

    private void handleStatusChange(UserStatusChangeMessage statusChangeMessage) {
        Predicate<ChatItem> equalChatId = chatItem ->
                Objects.equals(chatItem.getId(), statusChangeMessage.getChatId());

        int index = adapter.findFirst(equalChatId);
        Consumer<ChatItem> updater = chatItem ->
                chatItem.setStatus(statusChangeMessage.getUserStatus());
        adapter.updateItemAtIndex(index, updater);
    }

    private void setupRecyclerView() {
        chatList.setNoItemsIcon(R.drawable.round_chat_24);
        chatList.setNoItemsDescription(R.string.no_chats_description);
        chatList.setClipToPadding(false);
        float paddingInPixels = getResources().getDimension(R.dimen.list_large_bottom_padding);
        chatList.setPadding(0, 0, 0, (int) paddingInPixels);

        chatList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ChatListAdapter(this::startConversationActivity);

        chatList.setAdapter(adapter);
    }

    private void startConversationActivity(ChatItem chatItem) {
        Intent intent = new Intent(requireContext(), ConversationActivity.class);
        intent.putExtra(ConversationActivity.CHAT_ITEM_KEY, chatItem);

        requireActivity().startActivity(intent);
    }
}
