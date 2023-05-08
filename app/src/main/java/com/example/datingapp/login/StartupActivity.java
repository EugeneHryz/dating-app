package com.example.datingapp.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.datingapp.R;
import com.example.datingapp.activity.ActivityComponent;
import com.example.datingapp.activity.BaseActivity;
import com.example.datingapp.activity.RequestCode;
import com.example.datingapp.home.HomeActivity;
import com.example.datingapp.signup.SignUpActivity;

import javax.inject.Inject;

public class StartupActivity extends BaseActivity {

    private static final String TAG = StartupActivity.class.getName();

//    private ActivityResultLauncher<String[]> locationPermissionRequest =
//            registerForActivityResult(new ActivityResultContracts
//                            .RequestMultiplePermissions(), result -> {
//                        Boolean fineLocationGranted = result.getOrDefault(
//                                Manifest.permission.ACCESS_FINE_LOCATION, false);
//                        Boolean coarseLocationGranted = result.getOrDefault(
//                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
//                        if (fineLocationGranted != null && fineLocationGranted) {
//                            // Precise location access granted.
//                            startHomeActivity();
//                            supportFinishAfterTransition();
//                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
//                            // Only approximate location access granted.
//                            startHomeActivity();
//                            supportFinishAfterTransition();
//                        } else {
//                            System.out.println("WTFFFFF");
//                            startHomeActivity();
//                            supportFinishAfterTransition();
//                            // No location access granted.
//                        }
//                    }
//            );

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private StartupViewModel viewModel;

    @Override
    protected void injectActivity(ActivityComponent component) {
        component.inject(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(StartupViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.fragment_container);

        viewModel.getState().observe(this, this::handleStateChange);

        showInitialFragment(new LogInFragment());
    }

    private void handleStateChange(StartupViewModel.State state) {
        if (state == StartupViewModel.State.LOGGED_IN) {
            onSignedIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.SETUP_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                onSignedIn();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "User cancelled account creation");
                // show an error to a user
            }
        }
    }

    private void onSignedIn() {
//        viewModel.startServices();
//        locationPermissionRequest.launch(new String[]{
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION});
        startHomeActivity();
        supportFinishAfterTransition();
    }



// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.



    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
