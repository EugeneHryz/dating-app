package com.example.datingapp.searchpeople;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.client.user.UserDto;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.searchpeople.recyclerview.UserItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class SearchPeopleViewModel extends ViewModel {

    private final Executor ioExecutor;

    enum State {
        SEARCHING,
        FOUND
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

    private List<UserDto> users;

    @Inject
    public SearchPeopleViewModel(@IoExecutor Executor ioExecutor) {
        this.ioExecutor = ioExecutor;
    }

    public void searchForPeopleNearby() {
        ioExecutor.execute(() -> {
            state.postValue(State.SEARCHING);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            populateTestList();
            state.postValue(State.FOUND);
        });
    }

    public MutableLiveData<State> getState() {
        return state;
    }

    public List<UserItem> getUsers() {
        return users.stream()
                .map(u -> new UserItem(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    private void populateTestList() {
        users = new ArrayList<>();

        users.add(new UserDto(1L, "Zhenya Hryz", 0L));
        users.add(new UserDto(2L, "Maxim Ivashchenko", 0L));
        users.add(new UserDto(3L, "HHHH akalla", 0L));
        users.add(new UserDto(4L, "Dmitriy Orlov", 0L));
        users.add(new UserDto(5L, "UUUUoaa qqqq", 0L));
        users.add(new UserDto(6L, "Dmitriy Orlov", 0L));
        users.add(new UserDto(7L, "Dmitriy Orlov1", 0L));
        users.add(new UserDto(8L, "Dmitridy Orlov", 0L));
        users.add(new UserDto(9L, "Dmitriy Ordlov", 0L));
        users.add(new UserDto(10L, "Dmitriy Orlov", 0L));
        users.add(new UserDto(11L, "Dmitrdiy Orlov", 0L));
        users.add(new UserDto(12L, "Dmiffftriy Orlov", 0L));
        users.add(new UserDto(13L, "Dmitriy Orslov", 0L));
        users.add(new UserDto(14L, "Dmitriy Orlov", 0L));
        users.add(new UserDto(15L, "Dmitriy Osrlov", 0L));
        users.add(new UserDto(16L, "Dmitriy Orslov", 0L));
        users.add(new UserDto(17L, "Dmistriy Orlsov", 0L));
        users.add(new UserDto(18L, "Dmitriy Orlov", 0L));
    }
}
