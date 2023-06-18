package com.example.datingapp.chat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.R;
import com.example.datingapp.chat.data.ConversationItem;
import com.example.datingapp.chat.data.MessageConversationItem;
import com.example.datingapp.client.Constants;
import com.example.datingapp.client.auth.AuthenticatedUser;
import com.example.datingapp.client.chat.ChatMessagesInfo;
import com.example.datingapp.client.chat.ChatService;
import com.example.datingapp.client.chat.PrivateChatMessageDto;
import com.example.datingapp.event.Event;
import com.example.datingapp.event.EventBus;
import com.example.datingapp.event.EventListener;
import com.example.datingapp.home.ChatItem;
import com.example.datingapp.io.IoExecutor;
import com.example.datingapp.messaging.MessageMapper;
import com.example.datingapp.messaging.dto.AbstractChatMessage;
import com.example.datingapp.messaging.dto.AckMessage;
import com.example.datingapp.messaging.dto.MessageRead;
import com.example.datingapp.messaging.dto.MessageReadMarkDto;
import com.example.datingapp.messaging.dto.NewMessage;
import com.example.datingapp.messaging.dto.SendMessageDto;
import com.example.datingapp.messaging.dto.UserStatusChangeMessage;
import com.example.datingapp.messaging.event.ChatMessageEvent;
import com.example.datingapp.messaging.event.UserStatusChangeEvent;
import com.example.datingapp.service.MessagingService;
import com.example.datingapp.system.TimeManager;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationViewModel extends ViewModel implements EventListener {

    private static final String TAG = ConversationViewModel.class.getName();

    public static final int PAGE_SIZE = 25;

    private final Executor ioExecutor;
    private final EventBus eventBus;
    private final MessageMapper messageMapper;
    private final TimeManager timeManager;
    private final ChatService chatService;

    private AuthenticatedUser user;
    private ChatItem chat;
    private MessagingService.LocalBinder binder;

    enum State {
        INITIALIZING,
        INITIALIZED,
        NETWORK_ERROR
    }

    private final MutableLiveData<State> state = new MutableLiveData<>();

    private Long startPage;

    private Instant theirMessageLastReadTime;
    private Instant ourMessageLastReadTime;

    private final MutableLiveData<ConversationItem> incomingMessage = new MutableLiveData<>();
    private final MutableLiveData<AckMessage> ackMessage = new MutableLiveData<>();

    private final MutableLiveData<UserStatusChangeMessage> userStatusChange = new MutableLiveData<>();

    private final MutableLiveData<MessageRead> messageReadNotice = new MutableLiveData<>();

    private final Function<PrivateChatMessageDto, ConversationItem> mapper = msg -> {
        int layoutRes = msg.getIsIncoming() ? R.layout.in_msg_list_item : R.layout.out_msg_list_item;
        Instant timestamp = Instant.ofEpochMilli(msg.getTimestamp());
        MessageConversationItem messageItem = new MessageConversationItem(layoutRes, msg.getId(),
                timestamp, msg.getIsIncoming(), msg.getContent());
        if (!msg.getIsIncoming()) {
            messageItem.setStatus(getOutgoingMessageStatus(msg));
        }
        return messageItem;
    };

    @Inject
    public ConversationViewModel(@IoExecutor Executor ioExecutor,
                                 EventBus eventBus,
                                 MessageMapper messageMapper,
                                 TimeManager timeManager,
                                 ChatService chatService) {
        this.ioExecutor = ioExecutor;
        this.eventBus = eventBus;
        this.messageMapper = messageMapper;
        this.timeManager = timeManager;
        this.chatService = chatService;

        eventBus.addListener(this);
    }

    @Override
    protected void onCleared() {
        eventBus.removeListener(this);
    }

    public LiveData<ConversationItem> sendTextMessage(String text) {
        if (!isInitialized()) {
            throw new IllegalStateException("Some fields are not initialized. Unable to send a message");
        }
        MutableLiveData<ConversationItem> result = new MutableLiveData<>();

        long timestamp = timeManager.getApproximateServerTime();
        Instant timestampInstant = Instant.ofEpochMilli(timestamp);
        SendMessageDto message = new SendMessageDto(chat.getId(), user.getId(), timestamp, text);
        binder.sendSimpleMessage(messageMapper.serializeMessage(message));

        MessageConversationItem msgItem = new MessageConversationItem(R.layout.out_msg_list_item,
                Instant.ofEpochMilli(timestamp), false, text);
        result.setValue(msgItem);
        return result;
    }

    @Override
    public void onEventOccurred(Event e) {
        if (e instanceof ChatMessageEvent) {
            ChatMessageEvent event = (ChatMessageEvent) e;
            AbstractChatMessage chatMessage = event.getChatMessage();
            if (isInitialized() && Objects.equals(chatMessage.getChatId(), chat.getId())) {

                if (chatMessage instanceof NewMessage) {
                    onIncomingMessage((NewMessage) chatMessage);
                } else if (chatMessage instanceof AckMessage) {
                    onAckMessage((AckMessage) chatMessage);
                } else if (chatMessage instanceof MessageRead) {
                    onMessageRead((MessageRead) chatMessage);
                }
            }
        } else if (e instanceof UserStatusChangeEvent) {
            UserStatusChangeEvent event = (UserStatusChangeEvent) e;

            UserStatusChangeMessage statusChange = (UserStatusChangeMessage) event.getChatMessage();
            userStatusChange.setValue(statusChange);
        }
    }

    public void initialize() {
        if (chat == null) {
            throw new IllegalStateException("Chat is not initialized. Unable to get chat messages count");
        }

        ioExecutor.execute(() -> {
            state.postValue(State.INITIALIZING);

            Long timestampParam = null;
            chatService.getChatMessagesInfo(chat.getId(), timestampParam).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ChatMessagesInfo> call, Response<ChatMessagesInfo> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        ChatMessagesInfo info = response.body();
                        handleGetChatMessagesInfoResponse(info);
                        Log.d(TAG, "Received chat info: " + response.body());
                    } else {
                        Log.d(TAG, "Unable to get chat info. Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ChatMessagesInfo> call, Throwable t) {
                    Log.d(TAG, "Unable to get count of private messages", t);
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
    }

    public LiveData<List<ConversationItem>> loadPage(long pageNumber) {
        if (chat == null) {
            throw new IllegalStateException("Chat is not initialized. Unable to get chat messages");
        }

        MutableLiveData<List<ConversationItem>> messagesPage = new MutableLiveData<>();
        ioExecutor.execute(() -> {

            Map<String, String> params = new HashMap<>();
            params.put("page", Long.toString(pageNumber));
            params.put("size", Integer.toString(PAGE_SIZE));

            chatService.getPrivateChatMessages(chat.getId(), params).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<PrivateChatMessageDto>> call, Response<List<PrivateChatMessageDto>> response) {
                    if (response.code() == Constants.HTTP_SUCCESS) {
                        List<ConversationItem> messageItems = response.body().stream()
                                .map(mapper)
                                .collect(Collectors.toList());

                        messagesPage.postValue(messageItems);
                        Log.d(TAG, "Loaded page " + pageNumber + " of messages");
                    } else {
                        Log.d(TAG, "Unable to load page " + pageNumber + " of messages. " +
                                "Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<PrivateChatMessageDto>> call, Throwable t) {
                    Log.d(TAG, "Unable to load page of private messages", t);
                    state.postValue(State.NETWORK_ERROR);
                }
            });
        });
        return messagesPage;
    }

    public void markMessageAsRead(ConversationItem item) {
        if (!isInitialized()) {
            throw new IllegalStateException("Some fields are not initialized. Unable to mark message as read");
        }

        if (theirMessageLastReadTime == null || theirMessageLastReadTime.isBefore(item.getTimestamp())) {
            MessageReadMarkDto messageRead = new MessageReadMarkDto(chat.getId(), user.getId(),
                    item.getMessageId());
            binder.markMessageAsRead(messageMapper.serializeMessage(messageRead));
            theirMessageLastReadTime = item.getTimestamp();
        }
    }

    public LiveData<State> getState() {
        return state;
    }

    public MutableLiveData<ConversationItem> getIncomingMessage() {
        return incomingMessage;
    }

    public MutableLiveData<AckMessage> getAckMessage() {
        return ackMessage;
    }

    public MutableLiveData<UserStatusChangeMessage> getUserStatusChange() {
        return userStatusChange;
    }

    public MutableLiveData<MessageRead> getMessageReadNotice() {
        return messageReadNotice;
    }

    public Long getStartPage() {
        return startPage;
    }

    public Instant getTheirMessageLastReadTime() {
        return theirMessageLastReadTime;
    }

    public void setAuthenticatedUser(AuthenticatedUser user) {
        this.user = user;
    }

    public void setChat(ChatItem chat) {
        this.chat = chat;
    }

    public void setBinder(MessagingService.LocalBinder binder) {
        this.binder = binder;
    }

    private void handleGetChatMessagesInfoResponse(ChatMessagesInfo info) {
        if (info != null) {
            startPage = getStartPage(info.getNumberOfMessages(), info.getNumberOfUnreadMessages(), PAGE_SIZE);
            if (info.getOurMessageLastReadTime() != null) {
                ourMessageLastReadTime = Instant.ofEpochMilli(info.getOurMessageLastReadTime());
            }
            if (info.getTheirMessageLastReadTime() != null) {
                theirMessageLastReadTime = Instant.ofEpochMilli(info.getTheirMessageLastReadTime());
            }
            state.postValue(State.INITIALIZED);
        }
    }

    private ConversationItem.Status getOutgoingMessageStatus(PrivateChatMessageDto message) {
        Instant messageTime = Instant.ofEpochMilli(message.getTimestamp());
        if (ourMessageLastReadTime != null && !messageTime.isAfter(ourMessageLastReadTime)) {
            return ConversationItem.Status.SEEN;
        } else {
            return ConversationItem.Status.SENT;
        }
    }

    private long getStartPage(long totalMessages, long numberOfUnreadMessages, int pageSize) {
        return (totalMessages - numberOfUnreadMessages) / pageSize;
    }

    private void onAckMessage(AckMessage ack) {
        ackMessage.postValue(ack);
    }

    private void onMessageRead(MessageRead messageRead) {
        ourMessageLastReadTime = Instant.ofEpochMilli(messageRead.getLastReadMessageTime());
        messageReadNotice.postValue(messageRead);
    }

    private void onIncomingMessage(NewMessage message) {
        Instant timestamp = Instant.ofEpochMilli(message.getTimestamp());
        MessageConversationItem msgItem = new MessageConversationItem(R.layout.in_msg_list_item,
                message.getMessageId(), timestamp,true, message.getContent());

        incomingMessage.postValue(msgItem);
    }

    private boolean isInitialized() {
        return user != null && chat != null && binder != null;
    }
}
