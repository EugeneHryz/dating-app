package com.example.datingapp.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.client.Constants;
import com.example.datingapp.client.chat.ChatDto;
import com.example.datingapp.client.chat.UserChatService;
import com.example.datingapp.client.event.NotAuthenticatedEvent;
import com.example.datingapp.client.user.UserDto;
import com.example.datingapp.event.Event;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventListener;
import com.example.datingapp.home.recyclerview.ChatItem;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.searchpeople.recyclerview.UserItem;

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

    private final EventBus eventBus;
    private final Executor ioExecutor;
    private final UserChatService userChatService;

    enum State {
        NOT_AUTHENTICATED,
        GETTING_CHATS,
        RECEIVED_CHATS,
        NETWORK_ERROR
    }

    private List<ChatDto> chats;

    private final MutableLiveData<State> state = new MutableLiveData<>();

    @Inject
    public HomeViewModel(EventBus eventBus, @IoExecutor Executor executor,
                         UserChatService userChatService) {
        this.eventBus = eventBus;
        ioExecutor = executor;
        this.userChatService = userChatService;
        eventBus.addListener(this);
    }

    @Override
    protected void onCleared() {
        eventBus.removeListener(this);
    }

    public void getUserChats() {
        ioExecutor.execute(() -> {
            state.postValue(State.GETTING_CHATS);

            userChatService.getUserChats().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<ChatDto>> call, Response<List<ChatDto>> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        chats = response.body();
                        state.postValue(State.RECEIVED_CHATS);
                    }
                    Log.d(TAG, "Response code: " + response.code());
                }

                @Override
                public void onFailure(Call<List<ChatDto>> call, Throwable t) {
                    Log.d(TAG, "Failed to perform request for getting user chats");
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
    }

    public List<ChatItem> getChatItems() {
        Function<UserDto, UserItem> userMapper = userDto -> {
            UserItem item = new UserItem();
            item.setId(userDto.getId());
            item.setName(userDto.getUsername());
            return item;
        };
        Function<ChatDto, ChatItem> chatMapper = chatDto -> {
            ChatItem item = new ChatItem();
            item.setId(chatDto.getChatId());
            item.setName(chatDto.getChatName());
            item.setUser(chatDto.getUsersWithoutAuthenticatedUser()
                    .stream()
                    .map(userMapper)
                    .findFirst().orElse(null));
            return item;
        };

        return chats.stream().map(chatMapper).collect(Collectors.toList());
    }

    @Override
    public void onEventOccurred(Event e) {
        if (e instanceof NotAuthenticatedEvent) {
            state.postValue(State.NOT_AUTHENTICATED);
        }
    }

    public MutableLiveData<State> getState() {
        return state;
    }
}
