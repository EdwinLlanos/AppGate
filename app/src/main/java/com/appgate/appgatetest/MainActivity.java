package com.appgate.appgatetest;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.fragment.LoginFragment;
import com.appgate.appgatetest.viewmodel.LoginViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.LoginUseCase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginUseCase loginUseCase = new Injector().createLoginUseCase(this);
        GetAttemptsUseCase getAttemptsUseCase = new Injector().createGetAttemptUseCase(this);


        double latitude = 4.9612794;
        double longitude = -73.9141911;

        ViewModelFactory factory = new ViewModelFactory(loginUseCase, getAttemptsUseCase);
        LoginViewModel viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        // viewModel.sigInAttempt(latitude, longitude);
        //viewModel.getAttempts();

        viewModel.getStateUI().observe(this, message -> {
            // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
        showLoginScreen();
    }

    private void showLoginScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment fragment = LoginFragment.newInstance();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}