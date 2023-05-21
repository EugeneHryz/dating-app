package com.example.datingapp.searchpeople;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;

import javax.inject.Inject;

public class SearchPeopleActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private SearchPeopleViewModel viewModel;

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SearchPeopleViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_people_activity);

        setupActionBar();
        showInitialFragment(new PeopleNearbyFragment());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
        }
        return true;
    }

    private void setupActionBar() {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
    }
}