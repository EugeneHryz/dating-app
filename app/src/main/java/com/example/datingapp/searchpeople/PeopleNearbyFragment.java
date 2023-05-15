package com.example.datingapp.searchpeople;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.fragment.BaseFragment;
import com.example.datingapp.searchpeople.recyclerview.UserListAdapter;
import com.example.datingapp.view.MessengerRecyclerView;
import com.example.datingapp.view.SearchingAnimation;

import javax.inject.Inject;

public class PeopleNearbyFragment extends BaseFragment {

    private static final String TAG = PeopleNearbyFragment.class.getName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private SearchPeopleViewModel viewModel;

    private Button startSearchButton;
    private LinearLayout introToSearchLayout;

    private SearchingAnimation searchingAnimation;
    private MessengerRecyclerView userListView;
    private UserListAdapter adapter;

    @Override
    protected void injectFragment(ActivityComponent activityComponent) {
        activityComponent.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SearchPeopleViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.people_nearby_fragment, container, false);

        startSearchButton = view.findViewById(R.id.start_search_button);
        introToSearchLayout = view.findViewById(R.id.intro_to_search_layout);
        searchingAnimation = view.findViewById(R.id.searching_animation);
        userListView = view.findViewById(R.id.people_nearby_list);
        setupRecyclerView();

        viewModel.getState().observe(getViewLifecycleOwner(), this::handleStateChange);

        startSearchButton.setOnClickListener(v -> viewModel.searchForPeopleNearby());
        return view;
    }

    @Override
    public String getUniqueTag() {
        return TAG;
    }

    private void handleStateChange(SearchPeopleViewModel.State state) {
        if (state == SearchPeopleViewModel.State.SEARCHING) {
            userListView.setVisibility(View.INVISIBLE);
            introToSearchLayout.setVisibility(View.GONE);
            searchingAnimation.startAnimation();

        } else if (state == SearchPeopleViewModel.State.FOUND) {
            searchingAnimation.stopAnimation();
            userListView.setVisibility(View.VISIBLE);
            adapter.setItems(viewModel.getUsers());
        }
    }

    private void setupRecyclerView() {
        userListView.setNoItemsIcon(R.drawable.round_not_listed_location_24);
        userListView.setNoItemsDescription(R.string.no_people_nearby);

        userListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new UserListAdapter(null);

        userListView.setAdapter(adapter);
    }
}
