package com.example.datingapp.client.chat;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ChatService {

    @POST("/api/chat/createPrivateChat")
    Call<Void> createPrivateChat(@Body CreatePrivateChatDto createChatDto);

    @GET("/api/chat")
    Call<List<ChatDto>> getUserChats();

    @GET("/api/chat/{chatId}/messages")
    Call<List<PrivateChatMessageDto>> getPrivateChatMessages(@Path("chatId") Long chatId,
                                                             @QueryMap Map<String, String> params);

    @GET("/api/chat/{chatId}/info")
    Call<ChatMessagesInfo> getChatMessagesInfo(@Path("chatId") Long chatId, @Query("timestamp") Long timestamp);

    @GET("/api/chat/getUserStatuses")
    Call<List<UserStatusDto>> getPrivateChatUserStatuses();
}
