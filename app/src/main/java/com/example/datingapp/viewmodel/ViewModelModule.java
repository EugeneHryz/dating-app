package com.example.datingapp.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.chat.ConversationViewModel;
import com.example.datingapp.home.HomeViewModel;
import com.example.datingapp.login.StartupViewModel;
import com.example.datingapp.searchpeople.SearchPeopleViewModel;
import com.example.datingapp.signup.SignUpViewModel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    // The main idea is to add all ViewModels to the graph, so their dependencies can be satisfied.
    // And then bind all these ViewModels into a map, where Class<?> is a key and actual ViewModel
    // implementation is a value.

    @Binds
    @IntoMap
    @ClassKey(StartupViewModel.class)
    public abstract ViewModel bindStartupViewModel(StartupViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(SignUpViewModel.class)
    public abstract ViewModel bindSignUpViewModel(SignUpViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(HomeViewModel.class)
    public abstract ViewModel bindHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(SearchPeopleViewModel.class)
    public abstract ViewModel bindSearchPeopleViewModel(SearchPeopleViewModel viewModel);

    @Binds
    @IntoMap
    @ClassKey(ConversationViewModel.class)
    public abstract ViewModel bindConversationViewModel(ConversationViewModel viewModel);

    @Binds
    @Singleton
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
