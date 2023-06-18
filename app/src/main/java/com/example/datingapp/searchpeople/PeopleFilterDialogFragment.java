package com.example.datingapp.searchpeople;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;
import com.example.datingapp.client.dictionary.CountryDto;
import com.example.datingapp.common.TextFormatter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class PeopleFilterDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = PeopleFilterDialogFragment.class.getName();

    @Inject
    TextFormatter textFormatter;
    private SearchPeopleViewModel viewModel;

    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;

    private AppBarLayout appBarLayout;
    private SeekBar distanceSeekbar;
    private TextView currentDistanceTextView;
    private int selectedDistance;

    private AutoCompleteTextView countryAutoComplete;
    private Button applyFiltersButton;

    private ArrayAdapter<String> countriesAdapter;

    private InputMethodManager imm;

    public String getUniqueTag() {
        return TAG;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ActivityComponent activityComponent = ((BaseActivity) requireActivity()).getActivityComponent();
        activityComponent.inject(this);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchPeopleViewModel.class);

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.people_filter_fragment, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        bottomSheetBehavior = ((BottomSheetDialog) dialog).getBehavior();

        setupBottomSheetBehavior();
        allowBottomSheetToExpandFullScreen(dialog);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        distanceSeekbar = view.findViewById(R.id.distance_seekbar);
        currentDistanceTextView = view.findViewById(R.id.current_distance);
        countryAutoComplete = view.findViewById(R.id.country_autocomplete);
        appBarLayout = view.findViewById(R.id.toolbar_layout);
        applyFiltersButton = view.findViewById(R.id.apply_button);

        setupToolbar(view);
        setupDistanceSeekBar();
        setupCountriesSelector();

        applyFiltersButton.setOnClickListener(v -> applyFilters());

        viewModel.getCountries().observe(getViewLifecycleOwner(), this::handleCountriesChange);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        return true;
    }

    private void applyFilters() {
        List<CountryDto> allCountries = viewModel.getCountries().getValue();
        if (allCountries != null) {
            Optional<CountryDto> selectedCountry = allCountries.stream()
                    .filter(c -> c.getName().equals(countryAutoComplete.getText().toString()))
                    .findFirst();
            selectedCountry.ifPresent(countryDto -> viewModel.setSelectedCountry(countryDto));
        }
        viewModel.setSelectedDistance(selectedDistance);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void handleCountriesChange(List<CountryDto> countries) {
        List<String> countryNames = countries.stream()
                .map(CountryDto::getName)
                .collect(Collectors.toList());
        countriesAdapter.clear();
        countriesAdapter.addAll(countryNames);
    }

    private void hideVirtualKeyboard(View focusedView) {
        imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
    }

    private void setupCountriesSelector() {
        MutableLiveData<List<CountryDto>> countries = viewModel.getCountries();
        if (countries.getValue() == null || countries.getValue().size() == 0) {
            viewModel.getAllCountries();
        }

        countriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        countryAutoComplete.setAdapter(countriesAdapter);

        countryAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            hideVirtualKeyboard(countryAutoComplete);
            countryAutoComplete.clearFocus();
        });
        if (viewModel.getSelectedCountry() != null) {
            countryAutoComplete.setText(viewModel.getSelectedCountry().getName(), false);
        }
    }

    private void setupDistanceSeekBar() {
        int[] distanceOptions = getResources().getIntArray(R.array.distance_options);

        distanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedDistance = distanceOptions[progress];
                currentDistanceTextView.setText(textFormatter.formatDistanceUnits((long) selectedDistance));
                currentDistanceTextView.measure(0, 0);
                distanceSeekbar.measure(0, 0);

                addListenerToRunAfterLayoutMeasured(distanceSeekbar, view -> {
                    float translationX = calculateDistanceTextTranslationX(progress);
                    currentDistanceTextView.setTranslationX(translationX);
                });
                viewModel.setSelectedDistance(distanceOptions[progress]);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        if (viewModel.getSelectedDistance() != 0) {
            int distanceIndex = Arrays.asList(ArrayUtils.toObject(distanceOptions)).indexOf(viewModel.getSelectedDistance());
            if (distanceIndex != -1) {
                distanceSeekbar.setProgress(distanceIndex);
            }
        } else {
            viewModel.setSelectedDistance(distanceOptions[0]);
        }
    }

    private int getActionBarAttrSizeInPixels() {
        int actionBarHeight;
        int[] attrIds = new int[] { androidx.appcompat.R.attr.actionBarSize };
        TypedArray array = requireContext().obtainStyledAttributes(attrIds);
        actionBarHeight = array.getDimensionPixelSize(0, 0);
        array.recycle();
        return actionBarHeight;
    }

    private void setupBottomSheetBehavior() {
        int actionBarHeight = getActionBarAttrSizeInPixels();

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset >= 0) {
                    LayoutParams layoutParams = appBarLayout.getLayoutParams();
                    layoutParams.height = (int)(slideOffset * actionBarHeight);
                    appBarLayout.setLayoutParams(layoutParams);
                }
            }
        });
    }

    private void allowBottomSheetToExpandFullScreen(Dialog dialog) {
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout frameLayout = d.findViewById(androidx.navigation.ui.R.id.design_bottom_sheet);
            if (frameLayout != null) {
                ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
                layoutParams.height = LayoutParams.MATCH_PARENT;
                frameLayout.setLayoutParams(layoutParams);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.round_close_24);
        toolbar.setNavigationOnClickListener(v ->
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
    }

    private void addListenerToRunAfterLayoutMeasured(View view, LayoutListener listener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                distanceSeekbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                listener.onLayoutMeasured(view);
            }
        });
    }

    private float calculateDistanceTextTranslationX(int progress) {
        int totalWidth = distanceSeekbar.getWidth() - distanceSeekbar.getPaddingLeft() - distanceSeekbar.getPaddingRight();
        float translationX;
        if (progress != distanceSeekbar.getMax()) {
            translationX = (float)(totalWidth * progress) / distanceSeekbar.getMax();
        } else {
            translationX = distanceSeekbar.getWidth() - currentDistanceTextView.getMeasuredWidth();
        }
        return translationX;
    }

    interface LayoutListener {

        void onLayoutMeasured(View view);
    }
}
