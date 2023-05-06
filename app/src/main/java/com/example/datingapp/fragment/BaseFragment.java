package com.example.datingapp.fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;

public abstract class BaseFragment extends Fragment {

    protected abstract void injectFragment(ActivityComponent activityComponent);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ActivityComponent activityComponent = ((BaseActivity) context).getActivityComponent();
        injectFragment(activityComponent);
    }

    public abstract String getUniqueTag();
}
