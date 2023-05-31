package com.example.datingapp.conversation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.datingapp.R;
import com.example.datingapp.conversation.data.MessageConversationItem;

import java.time.LocalDateTime;

import javax.inject.Inject;

public class ConversationViewModel extends ViewModel {

    private static final String TAG = ConversationViewModel.class.getName();

    @Inject
    public ConversationViewModel() {
    }

    public LiveData<MessageConversationItem> sendTextMessage(String text) {
        MutableLiveData<MessageConversationItem> result = new MutableLiveData<>();

        LocalDateTime currentTime = LocalDateTime.now();
        MessageConversationItem msgItem = new MessageConversationItem(R.layout.out_msg_list_item, null,
                currentTime, false, text);
        result.setValue(msgItem);

        return result;
    }
}
