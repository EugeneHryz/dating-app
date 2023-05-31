package com.example.datingapp.searchpeople;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.client.Constants;
import com.example.datingapp.client.chat.UserChatService;
import com.example.datingapp.client.user.UserDto;
import com.example.datingapp.client.user.UserService;
import com.example.datingapp.common.TextFormatter;
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

public class SearchPeopleViewModel extends ViewModel {

    private static final String TAG = SearchPeopleViewModel.class.getName();

    private final Executor ioExecutor;
    private final UserService userService;
    private final UserChatService userChatService;
    private final TextFormatter textFormatter;

    enum State {
        SEARCHING,
        FOUND,
        ADDING_CONTACT,
        ADDED_CONTACT,
        NETWORK_ERROR
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

    private List<UserDto> users;

    @Inject
    public SearchPeopleViewModel(@IoExecutor Executor ioExecutor, UserService userService,
                                 UserChatService userChatService, TextFormatter textFormatter) {
        this.ioExecutor = ioExecutor;
        this.userService = userService;
        this.userChatService = userChatService;
        this.textFormatter = textFormatter;
    }

    public void searchForPeopleNearby() {
        ioExecutor.execute(() -> {
            state.postValue(State.SEARCHING);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userService.findUsersWithinRadius(6000).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        users = response.body();
                        state.postValue(State.FOUND);
                    }
                    Log.d(TAG, "Response code: " + response.code());
                }

                @Override
                public void onFailure(Call<List<UserDto>> call, Throwable t) {
                    Log.d(TAG, "Failed to perform request for searching users nearby");
                    state.postValue(State.NETWORK_ERROR);
                }
            });

        });
    }

    public void createChatWithUser(Long userId) {
        ioExecutor.execute(() -> {
            state.postValue(State.ADDING_CONTACT);

            userChatService.createPrivateChat(userId).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        state.postValue(State.ADDED_CONTACT);
                    }
                    Log.d(TAG, "Response code: " + response.code());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d(TAG, "Failed to perform request for searching users nearby");
                    state.postValue(State.NETWORK_ERROR);
                }
            });

        });
    }

    public MutableLiveData<State> getState() {
        return state;
    }

    public List<UserItem> getUsers() {
        Function<UserDto, UserItem> mapper = userDto -> {
            String distance = textFormatter.formatDistanceUnits(userDto.getDistance());
            Log.d(TAG, distance);
            return new UserItem(userDto.getId(), userDto.getUsername(), distance);
        };

        return users.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
