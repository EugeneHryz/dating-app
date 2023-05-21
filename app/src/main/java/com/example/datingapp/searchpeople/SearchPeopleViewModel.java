package com.example.datingapp.searchpeople;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.client.Constants;
import com.example.datingapp.client.user.UserDto;
import com.example.datingapp.client.user.UserService;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.searchpeople.recyclerview.UserItem;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPeopleViewModel extends ViewModel {

    private static final String TAG = SearchPeopleViewModel.class.getName();

    private final Executor ioExecutor;

    private final UserService userService;

    enum State {
        SEARCHING,
        FOUND,
        NETWORK_ERROR
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

    private List<UserDto> users;

    @Inject
    public SearchPeopleViewModel(@IoExecutor Executor ioExecutor, UserService userService) {
        this.ioExecutor = ioExecutor;
        this.userService = userService;
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

    public MutableLiveData<State> getState() {
        return state;
    }

    public List<UserItem> getUsers() {
        return users.stream()
                .map(u -> new UserItem(u.getId(), u.getUsername(), u.getDistance()))
                .collect(Collectors.toList());
    }
}
