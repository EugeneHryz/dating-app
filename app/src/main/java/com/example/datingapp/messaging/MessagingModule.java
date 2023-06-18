package com.example.datingapp.messaging;

import dagger.Module;
import dagger.Provides;

@Module
public class MessagingModule {

    @Provides
    public MessageMapper provideMessageMapper(MessageMapperImpl messageMapper) {
        return messageMapper;
    }
}
