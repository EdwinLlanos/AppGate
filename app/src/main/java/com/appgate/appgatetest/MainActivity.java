package com.appgate.appgatetest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.appgate.appgatetest.fragment.AttemptFragment;
import com.appgate.appgatetest.fragment.SignInFragment;
import com.appgate.appgatetest.fragment.SignUpFragment;
import com.appgate.appgatetest.service.LocationService;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final int NUMBER_ZERO = 0;

    LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationGrant();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocationService();
    }

    private void destroyLocationService() {
        if (Objects.nonNull(locationService)) {
            locationService.onDestroy();
        }
    }

    private void checkLocationGrant() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > NUMBER_ZERO && grantResults[NUMBER_ZERO] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                showLocationAlert(getString(R.string.message_location_permission_required));
            }
        } else {
            showLocationAlert(getString(R.string.message_location_permission_required));
        }
    }

    private void startLocationService() {
        locationService = new LocationService(this, new OnRequestCompletedListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                navigateToSignInScreen(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                destroyLocationService();
            }

            @Override
            public void onError(Throwable throwable) {
                showLocationAlert(throwable.getMessage());
            }
        });
        if (!locationService.isLocationEnabled()) {
            showLocationAlert(getString(R.string.message_gps_enable_required));
        }
    }

    private void switchToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void navigateToSignInScreen(String latitude, String longitude) {
        switchToFragment(SignInFragment.newInstance(latitude, longitude));
    }

    public void navigateToSignInScreen() {
        switchToFragment(SignInFragment.newInstance());
    }

    public void navigateToSignUpScreen() {
        switchToFragment(SignUpFragment.newInstance());
    }

    public void navigateToAttemptScreen() {
        switchToFragment(AttemptFragment.newInstance());
    }

    private void showLocationAlert(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.warning_location));
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton(getString(R.string.btn_ok), (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}