package com.example.datingapp.chat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.datingapp.MessengerApplication;
import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;
import com.example.datingapp.chat.data.ConversationAdapter;
import com.example.datingapp.chat.data.ConversationItem;
import com.example.datingapp.client.auth.AuthenticatedUser;
import com.example.datingapp.home.ChatItem;
import com.example.datingapp.messaging.dto.AckMessage;
import com.example.datingapp.messaging.dto.MessageRead;
import com.example.datingapp.messaging.dto.UserStatus;
import com.example.datingapp.messaging.dto.UserStatusChangeMessage;
import com.example.datingapp.service.MessagingService;
import com.example.datingapp.view.EmojiTextInputView;
import com.example.datingapp.view.MessengerRecyclerView;
import com.example.datingapp.view.RecyclerViewScrollListener;
import com.example.datingapp.view.TextSendController;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.inject.Inject;

public class ConversationActivity extends BaseActivity implements TextSendController.SendListener {

    public static final String CHAT_ITEM_KEY = "chat_item";

    private static final String TAG = ConversationActivity.class.getName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ConversationViewModel viewModel;

    private TextSendController sendController;

    private ConversationAdapter adapter;
    private MessengerRecyclerView messagesList;

    private ChatItem chat;

    private Long nextUpperPageToLoad;
    private Long nextBottomPageToLoad;
    private Integer lastLoadedPageSize;
    private boolean loadingPage;

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ConversationViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);

        EmojiTextInputView emojiTextInput = findViewById(R.id.emoji_text_input);
        AppCompatImageButton sendButton = findViewById(R.id.send_button);
        sendController = new TextSendController(sendButton, emojiTextInput, this);

        messagesList = findViewById(R.id.messages_list);
        setupMessagesList();

        Intent intent = getIntent();
        chat = (ChatItem) intent.getSerializableExtra(CHAT_ITEM_KEY);
        setupActionBar();
        setViewModelFields();

        bindToMessagingService();
        viewModel.getIncomingMessage().observe(this, this::displayMessage);
        viewModel.getAckMessage().observe(this, this::acknowledgeMessage);
        viewModel.getUserStatusChange().observe(this, this::handleUserStatusChange);
        viewModel.getMessageReadNotice().observe(this, this::handleMessageRead);
        viewModel.getState().observe(this, this::handleStateChange);
        viewModel.initialize();

        messagesList.showItems();
    }

    @Override
    public void onSendMessage(String text) {
        viewModel.sendTextMessage(text).observe(this, this::displayMessage);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleStateChange(ConversationViewModel.State state) {
        if (state == ConversationViewModel.State.INITIALIZED) {
            onViewModelInitialized();
        }
    }

    private void onViewModelInitialized() {
        long startPageToLoad = viewModel.getStartPage();
        viewModel.loadPage(startPageToLoad).observe(this, this::displayPageAndHighlightFirstUnread);
        nextUpperPageToLoad = startPageToLoad - 1;
        nextBottomPageToLoad = startPageToLoad + 1;
    }

    private void acknowledgeMessage(AckMessage ack) {
        Predicate<ConversationItem> equalTimestampTester = item ->
                item.getTimestamp().toEpochMilli() == ack.getTimestamp();

        int index = adapter.findIndexOfFirstMatching(equalTimestampTester);
        if (index != -1) {
            Consumer<ConversationItem> updater = item -> {
                item.setMessageId(ack.getMessageId());
                item.setStatus(ConversationItem.Status.SENT);
            };
            adapter.updateItemAtIndex(index, updater);
        }
    }

    private void handleUserStatusChange(UserStatusChangeMessage statusChangeMessage) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            int resId = getUserStatusRes(statusChangeMessage.getUserStatus());
            ab.setSubtitle(resId);
        }
    }

    private void handleMessageRead(MessageRead messageRead) {
        Instant readMessageTime = Instant.ofEpochMilli(messageRead.getLastReadMessageTime());
        Predicate<ConversationItem> earlierMessagesTester = item ->
                !item.getTimestamp().isAfter(readMessageTime);

        Consumer<ConversationItem> updater = item -> {
            if (!item.isIncoming()) {
                item.setStatus(ConversationItem.Status.SEEN);
            }
        };
        adapter.updateRangeOfItems(earlierMessagesTester, updater);
    }

    private void displayMessage(ConversationItem msgItem) {
        adapter.addItem(msgItem);
        scrollToBottom();

        if (adapter.getItemCount() > 0) {
            messagesList.showItems();
        }
    }

    private void bindToMessagingService() {
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                viewModel.setBinder((MessagingService.LocalBinder) service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "MessagingService disconnected");
            }
        };
        Intent intent = new Intent(this, MessagingService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void displayPageAndHighlightFirstUnread(List<ConversationItem> messages) {
        displayPageOfMessages(messages);

        Instant lastReadTime = viewModel.getTheirMessageLastReadTime();
        if (lastReadTime == null) {
            scrollToBottom();
        } else {
            int position = adapter.findIndexOfFirstMatching(item ->
                    item.getTimestamp().isAfter(lastReadTime) && item.isIncoming());
            if (position != -1) {
                messagesList.scrollToPosition(position);
                adapter.updateItemAtIndex(position, item -> item.setHighlighted(true));
            }
        }
    }

    private void displayPageOfMessages(List<ConversationItem> messages) {
        loadingPage = false;
        adapter.addAll(messages);

        int previouslyFirstItemPos = messages.size();
        adapter.notifyItemChanged(previouslyFirstItemPos);

        int itemCount = adapter.getItemCount();
        if (itemCount > 0) {
            messagesList.showItems();
        }
    }

    private void setViewModelFields() {
        viewModel.setChat(chat);

        MessengerApplication application = (MessengerApplication) getApplication();
        AuthenticatedUser user = application.getAuthenticatedUser();
        viewModel.setAuthenticatedUser(user);
    }

    private void scrollToBottom() {
        int pos = adapter.getItemCount() - 1;
        messagesList.scrollToPosition(pos);
    }

    private void loadNextUpperPage() {
        if (nextUpperPageToLoad != -1) {
            loadingPage = true;
            viewModel.loadPage(nextUpperPageToLoad).observe(this, this::displayPageOfMessages);
            nextUpperPageToLoad -= 1;
        }
    }

    private void loadNextBottomPage() {
        if (lastLoadedPageSize == null || lastLoadedPageSize != 0) {
            loadingPage = true;
            viewModel.loadPage(nextBottomPageToLoad).observe(this, page -> {
                displayPageOfMessages(page);
                lastLoadedPageSize = page.size();
            });
            nextBottomPageToLoad += 1;
        }
    }

    private void setupMessagesList() {
        messagesList.setNoItemsDescription(R.string.no_messages_description);
        messagesList.setClipToPadding(false);
        float paddingInPixels = getResources().getDimension(R.dimen.list_small_bottom_padding);
        messagesList.setPadding(0, 0, 0, (int) paddingInPixels);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesList.setLayoutManager(layoutManager);
        adapter = new ConversationAdapter();
        messagesList.setAdapter(adapter);

        messagesList.addScrollListener(new RecyclerViewScrollListener<>(adapter, new RecyclerViewScrollListener.Callback<>() {
            @Override
            public void onLastItemVisible(ConversationItem item) {
                if (item.isIncoming()) {
                    viewModel.markMessageAsRead(item);
                }
            }

            @Override
            public void onReachedTop() {
                if (!loadingPage) {
                    loadNextUpperPage();
                }
            }

            @Override
            public void onReachedBottom() {
                if (!loadingPage) {
                    loadNextBottomPage();
                }
            }
        }
        ));
    }

    private void setupActionBar() {
        setTitle(chat.getName());

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);

            int resId = getUserStatusRes(chat.getStatus());
            ab.setSubtitle(resId);
        }
    }

    private int getUserStatusRes(UserStatus status) {
        return status == UserStatus.ONLINE ? R.string.user_online : R.string.user_offline;
    }
}
