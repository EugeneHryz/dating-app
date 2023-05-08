package com.example.datingapp.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.fragment.BaseFragment;

public class TestFragment extends BaseFragment {

    private static final String TAG = TestFragment.class.getName();

    private Button toggleAnimationBtn;
    private ImageView firstCircle;
    private ImageView secondCircle;
    private ImageView thirdCircle;

    private ObjectAnimator firstCircleAnimator;
    private ObjectAnimator secondCircleAnimator;
    private ObjectAnimator thirdCircleAnimator;

    private Animator.AnimatorListener animatorListener;

    @Override
    protected void injectFragment(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment, container, false);
        toggleAnimationBtn = view.findViewById(R.id.toggle_animation_btn);
        firstCircle = view.findViewById(R.id.first_circle);
        secondCircle = view.findViewById(R.id.second_circle);
        thirdCircle = view.findViewById(R.id.third_circle);

        toggleAnimationBtn.setOnClickListener(v -> toggleAnimation());

        initAnimators();

        return view;
    }

    private void toggleAnimation() {
        if (!firstCircleAnimator.isRunning()) {
            firstCircle.setVisibility(View.VISIBLE);
            secondCircle.setVisibility(View.VISIBLE);
            thirdCircle.setVisibility(View.VISIBLE);
            firstCircleAnimator.start();
            secondCircleAnimator.start();
            thirdCircleAnimator.start();

            animatorListener = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    firstCircleAnimator.start();
                    secondCircleAnimator.start();
                    thirdCircleAnimator.start();
                }
            };
            firstCircleAnimator.addListener(animatorListener);
        } else {
            firstCircleAnimator.removeListener(animatorListener);

            firstCircleAnimator.end();
            secondCircleAnimator.end();
            thirdCircleAnimator.end();
            firstCircle.setVisibility(View.INVISIBLE);
            secondCircle.setVisibility(View.INVISIBLE);
            thirdCircle.setVisibility(View.INVISIBLE);
        }
    }

    private void initAnimators() {
        firstCircleAnimator = ObjectAnimator.ofPropertyValuesHolder(firstCircle,
                PropertyValuesHolder.ofFloat("scaleX", 0, 2f),
                PropertyValuesHolder.ofFloat("scaleY", 0, 2f),
                PropertyValuesHolder.ofFloat("alpha", 1, 0f));
        firstCircleAnimator.setDuration(2500);

        secondCircleAnimator = ObjectAnimator.ofPropertyValuesHolder(secondCircle,
                PropertyValuesHolder.ofFloat("scaleX", 0, 1.3f),
                PropertyValuesHolder.ofFloat("scaleY", 0, 1.3f),
                PropertyValuesHolder.ofFloat("alpha", 1, 0f));
        secondCircleAnimator.setDuration(2000);

        thirdCircleAnimator = ObjectAnimator.ofPropertyValuesHolder(thirdCircle,
                PropertyValuesHolder.ofFloat("scaleX", 0, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 0, 1f),
                PropertyValuesHolder.ofFloat("alpha", 1, 0f));
        thirdCircleAnimator.setDuration(1200);
    }

    @Override
    public String getUniqueTag() {
        return TAG;
    }
}
