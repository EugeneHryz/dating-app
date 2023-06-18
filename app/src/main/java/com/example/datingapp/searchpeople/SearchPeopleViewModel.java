package com.example.datingapp.searchpeople;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.client.Constants;
import com.example.datingapp.client.chat.ChatService;
import com.example.datingapp.client.chat.CreatePrivateChatDto;
import com.example.datingapp.client.dictionary.CountryDto;
import com.example.datingapp.client.dictionary.DictionaryService;
import com.example.datingapp.client.user.UserNearbyDto;
import com.example.datingapp.client.user.UserService;
import com.example.datingapp.common.TextFormatter;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.searchpeople.recyclerview.UserNearbyItem;

import java.util.ArrayList;
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

    private final TextFormatter textFormatter;
    private final Executor ioExecutor;

    private final UserService userService;
    private final DictionaryService dictionaryService;
    private final ChatService chatService;

    enum State {
        SEARCHING,
        FOUND,
        NETWORK_ERROR,
        CREATING_CHAT,
        FAILED_TO_CREATE_CHAT,
        CHAT_CREATED
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();
    private final MutableLiveData<List<CountryDto>> countries = new MutableLiveData<>(new ArrayList<>());

    private CountryDto selectedCountry;
    private int selectedDistance;
    private List<UserNearbyDto> users;

    @Inject
    public SearchPeopleViewModel(TextFormatter textFormatter,
                                 @IoExecutor Executor ioExecutor,
                                 UserService userService,
                                 DictionaryService dictionaryService,
                                 ChatService chatService) {
        this.textFormatter = textFormatter;
        this.ioExecutor = ioExecutor;
        this.userService = userService;
        this.dictionaryService = dictionaryService;
        this.chatService = chatService;
    }

    public void createChat(Long userId) {
        ioExecutor.execute(() -> {
            state.postValue(State.CREATING_CHAT);

            CreatePrivateChatDto requestBody = new CreatePrivateChatDto(userId);
            chatService.createPrivateChat(requestBody).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        state.postValue(State.CHAT_CREATED);
                    } else {
                        state.postValue(State.FAILED_TO_CREATE_CHAT);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d(TAG, "Failed to perform request for creating new chat");
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
    }

    public void getAllCountries() {
        ioExecutor.execute(() -> {
            dictionaryService.getAllCountries().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<CountryDto>> call, Response<List<CountryDto>> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        countries.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<CountryDto>> call, Throwable t) {
                    Log.d(TAG, "Failed to perform request for getting all countries");
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
    }

    public void searchForPeopleNearby() {
        ioExecutor.execute(() -> {
            state.postValue(State.SEARCHING);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String country = selectedCountry != null ? selectedCountry.getName() : null;
            userService.findUsersWithinRadius(selectedDistance, country).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<UserNearbyDto>> call, Response<List<UserNearbyDto>> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        users = response.body();
                        state.postValue(State.FOUND);
                    }
                    Log.d(TAG, "Response code: " + response.code());
                }

                @Override
                public void onFailure(Call<List<UserNearbyDto>> call, Throwable t) {
                    Log.d(TAG, "Failed to perform request for searching users nearby");
                    state.postValue(State.NETWORK_ERROR);
                }
            });

        });
    }

    public MutableLiveData<State> getState() {
        return state;
    }

    public List<UserNearbyItem> getUsers() {
        Function<UserNearbyDto, UserNearbyItem> mapper = userNearbyDto -> {
            UserNearbyItem item = new UserNearbyItem();
            item.setName(userNearbyDto.getUsername());
            item.setDistance(textFormatter.formatDistanceUnits(userNearbyDto.getDistance()));
            item.setId(userNearbyDto.getId());
            return item;
        };

        return users.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    public MutableLiveData<List<CountryDto>> getCountries() {
        return countries;
    }

    public CountryDto getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(CountryDto selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public int getSelectedDistance() {
        return selectedDistance;
    }

    public void setSelectedDistance(int selectedDistance) {
        this.selectedDistance = selectedDistance;
    }
}
