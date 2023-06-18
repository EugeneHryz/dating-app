package com.example.datingapp.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.client.Constants;
import com.example.datingapp.client.chat.ChatDto;
import com.example.datingapp.client.chat.ChatService;
import com.example.datingapp.event.Event;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventListener;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.messaging.dto.UserStatusChangeMessage;
import com.example.datingapp.messaging.event.UserStatusChangeEvent;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel implements EventListener {

    private static final String TAG = HomeViewModel.class.getName();

    private final Executor ioExecutor;
    private final ChatService chatService;
    private final EventBus eventBus;

    enum State {
        LOADING_CHATS,
        CHATS_LOADED,
        NETWORK_ERROR
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

    private final MutableLiveData<UserStatusChangeMessage> userStatusChange = new MutableLiveData<>();

    private List<ChatDto> chats;

    @Inject
    public HomeViewModel(@IoExecutor Executor ioExecutor,
                         ChatService chatService,
                         EventBus eventBus) {
        this.ioExecutor = ioExecutor;
        this.chatService = chatService;
        this.eventBus = eventBus;

        eventBus.addListener(this);
    }

    public void loadChats() {
        ioExecutor.execute(() -> {
            state.postValue(State.LOADING_CHATS);

            chatService.getUserChats().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<ChatDto>> call, Response<List<ChatDto>> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        chats = response.body();
                        state.postValue(State.CHATS_LOADED);
                    }
                }

                @Override
                public void onFailure(Call<List<ChatDto>> call, Throwable t) {
                    Log.d(TAG, "Failed to perform request for getting user chats");
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
    }

    @Override
    public void onEventOccurred(Event e) {
        if (e instanceof UserStatusChangeEvent) {
            UserStatusChangeEvent event = (UserStatusChangeEvent) e;

            UserStatusChangeMessage statusChange = (UserStatusChangeMessage) event.getChatMessage();
            userStatusChange.setValue(statusChange);
        }
    }

    public MutableLiveData<State> getState() {
        return state;
    }

    public MutableLiveData<UserStatusChangeMessage> getUserStatusChange() {
        return userStatusChange;
    }

    public List<ChatItem> getChatItems() {
        Function<ChatDto, ChatItem> mapper = chatDto ->
                new ChatItem(chatDto.getId(), chatDto.getName(), chatDto.getUserStatus().getStatus());

        return chats.stream().map(mapper).collect(Collectors.toList());
    }
}
