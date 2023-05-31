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
import com.example.datingapp.conversation.ConversationActivity;
import com.example.datingapp.fragment.BaseFragment;
import com.example.datingapp.home.recyclerview.ChatItem;
import com.example.datingapp.home.recyclerview.ChatListAdapter;
import com.example.datingapp.view.MessengerRecyclerView;

public class ChatListFragment extends BaseFragment {

    private static final String TAG = com.example.datingapp.searchpeople.PeopleNearbyFragment.class.getName();

    private HomeViewModel viewModel;

    private MessengerRecyclerView chatListView;
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

        chatListView = view.findViewById(R.id.chat_list);
        setupRecyclerView();

        viewModel.getState().observe(getViewLifecycleOwner(), this::handleStateChange);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getUserChats();
    }

    @Override
    public String getUniqueTag() {
        return TAG;
    }

    private void handleStateChange(HomeViewModel.State state) {
        if (state == HomeViewModel.State.RECEIVED_CHATS) {
            adapter.setItems(viewModel.getChatItems());
        }
    }

    private void setupRecyclerView() {
        chatListView.setNoItemsIcon(R.drawable.round_chat_24);
        chatListView.setNoItemsDescription(R.string.no_chats_added);

        chatListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ChatListAdapter(this::startConversationActivity);

        chatListView.setAdapter(adapter);
    }

    private void startConversationActivity(ChatItem item) {
        String username = item.getName();

        Intent intent = new Intent(requireActivity(), ConversationActivity.class);
        if (username != null) {
            intent.putExtra(ConversationActivity.CONTACT_USERNAME_KEY, username);
        }
        startActivity(intent);
    }
}

