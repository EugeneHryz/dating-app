package com.example.datingapp.system;

import com.example.datingapp.lifecycle.EventExecutor;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidSystemModule {

	@Provides
	@Singleton
	AndroidExecutor provideAndroidExecutor(AndroidExecutorImpl androidExecutor) {
		return androidExecutor;
	}

	@Provides
	@Singleton
	@EventExecutor
	Executor provideEventExecutor(AndroidExecutor androidExecutor) {
		return androidExecutor::runOnUiThread;
	}
}
