package com.example.datingapp.client.chat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserChatService {

    @POST("api/userChat/createChatForTwoUsers")
    Call<Void> createPrivateChat(@Query("secondUserId") Long userId);

    @GET("api/userChat/getChatsForUser")
    Call<List<ChatDto>> getUserChats();
}
