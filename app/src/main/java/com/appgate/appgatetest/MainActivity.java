package com.appgate.appgatetest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.appgate.appgatetest.fragment.AttemptFragment;
import com.appgate.appgatetest.fragment.SignInFragment;
import com.appgate.appgatetest.fragment.SignUpFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigateToSignInScreen();
    }

    private void switchToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
}