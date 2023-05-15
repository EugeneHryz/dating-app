package com.example.datingapp.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.datingapp.R;

public class SearchingAnimation extends FrameLayout {

    private TextView searchingText;
    private ImageView firstCircle;
    private ImageView secondCircle;
    private ImageView thirdCircle;

    private Animation textAnimation;
    private ObjectAnimator firstCircleAnimator;
    private ObjectAnimator secondCircleAnimator;
    private ObjectAnimator thirdCircleAnimator;

    private Animator.AnimatorListener animatorListener;

    public SearchingAnimation(@NonNull Context context) {
        super(context);
        initView();
    }

    public SearchingAnimation(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchingAnimation(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SearchingAnimation(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.searching_animation, this, true);

        searchingText = rootView.findViewById(R.id.searching_text);
        firstCircle = rootView.findViewById(R.id.first_circle);
        secondCircle = rootView.findViewById(R.id.second_circle);
        thirdCircle = rootView.findViewById(R.id.third_circle);

        initAnimators();
    }

    public void startAnimation() {
        searchingText.setVisibility(View.VISIBLE);
        firstCircle.setVisibility(View.VISIBLE);
        secondCircle.setVisibility(View.VISIBLE);
        thirdCircle.setVisibility(View.VISIBLE);
        searchingText.startAnimation(textAnimation);
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
    }

    public void stopAnimation() {
        if (animatorListener != null) {
            firstCircleAnimator.removeListener(animatorListener);
        }

        firstCircleAnimator.end();
        secondCircleAnimator.end();
        thirdCircleAnimator.end();
        searchingText.clearAnimation();
        firstCircle.setVisibility(View.INVISIBLE);
        secondCircle.setVisibility(View.INVISIBLE);
        thirdCircle.setVisibility(View.INVISIBLE);
        searchingText.setVisibility(View.INVISIBLE);
    }

    private void initAnimators() {
        textAnimation = new AlphaAnimation(0f, 1f);
        textAnimation.setDuration(1500);
        textAnimation.setRepeatCount(Animation.INFINITE);
        textAnimation.setRepeatMode(Animation.REVERSE);

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
}
