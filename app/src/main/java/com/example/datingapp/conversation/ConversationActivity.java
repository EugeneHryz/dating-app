package com.example.datingapp.conversation;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;
import com.example.datingapp.conversation.data.ConversationAdapter;
import com.example.datingapp.conversation.data.ConversationItem;
import com.example.datingapp.view.EmojiTextInputView;
import com.example.datingapp.view.MessengerRecyclerView;
import com.example.datingapp.view.TextSendController;

import java.util.List;

import javax.inject.Inject;

public class ConversationActivity extends BaseActivity implements TextSendController.SendListener {

    private static final String TAG = ConversationActivity.class.getName();

    public static final String CONTACT_USERNAME_KEY = "contact_username_key";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ConversationViewModel viewModel;

    private TextSendController sendController;

    private ConversationAdapter adapter;
    private MessengerRecyclerView messagesList;

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ConversationViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);
        setupActionBar();

        EmojiTextInputView emojiTextInput = findViewById(R.id.emoji_text_input);
        AppCompatImageButton sendButton = findViewById(R.id.send_button);
        sendController = new TextSendController(sendButton, emojiTextInput, this);

        messagesList = findViewById(R.id.messages_list);
        setupMessagesList();

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

    private void displayMessage(ConversationItem msgItem) {
        adapter.addItem(msgItem);
        scrollToEnd();

        // FIXME: ?
        if (adapter.getItemCount() > 0) {
            messagesList.showItems();
        }
    }

    private void displayMessages(List<ConversationItem> msgItems) {
        adapter.replaceAll(msgItems);
        scrollToEnd();

        messagesList.showItems();
    }

    private void scrollToEnd() {
        int pos = adapter.getItemCount() - 1;
        messagesList.scrollToPosition(pos);
    }

    private void setupMessagesList() {
        messagesList.setNoItemsDescription(R.string.no_messages_description);

        messagesList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ConversationAdapter();
        messagesList.setAdapter(adapter);
    }

    private void setupActionBar() {
        String username = getIntent().getStringExtra(CONTACT_USERNAME_KEY);
        setTitle(username);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
