package com.appgate.appgatetest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.appgate.appgatetest.base.fragment.BaseFragment;
import com.appgate.appgatetest.databinding.FragmentSignInBinding;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.viewmodel.SignInViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SaveLocationUseCase;
import com.appgate.authentication.domain.usecase.SignInUseCase;
import java.util.Objects;

public class SignInFragment extends BaseFragment {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private FragmentSignInBinding binding;
    private SignInViewModel viewModel;

    public static SignInFragment newInstance(String latitude, String longitude) {
        SignInFragment signInFragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(LATITUDE, latitude);
        args.putString(LONGITUDE, longitude);
        signInFragment.setArguments(args);
        return signInFragment;
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        initListener();
        setProgressBar(binding.progressBar);
        setRootLayout(binding.rootLayout);
        return binding.getRoot();
    }

    @Override
    public void initUI() {
        initViewModel();
        initObservers();
        setLocation();
    }

    private void setLocation() {
        Bundle args = getArguments();
        if (args == null) return;
        String latitude = args.getString(LATITUDE, null);
        String longitude = args.getString(LONGITUDE, null);
        if (latitude != null && longitude != null) {
            viewModel.setLocation(latitude, longitude);
        }
    }

    private void initViewModel() {
        SignInUseCase signInUseCase = new Injector().createSignInUseCase(getContext());
        SaveAttemptUseCase saveAttemptUseCase = new Injector().createSaveAttemptUseCase(getContext());
        SaveLocationUseCase saveLocationUseCase = new Injector().createSaveLocationUseCase(getContext());
        ViewModelFactory factory = new ViewModelFactory(signInUseCase, saveAttemptUseCase, saveLocationUseCase);
        viewModel = new ViewModelProvider(this, factory).get(SignInViewModel.class);
    }

    private void initObservers() {
        viewModel.getMessageString().observe(this, this::showMessage);
        viewModel.getMessageResource().observe(this, this::showMessage);
        viewModel.getLoading().observe(this, this::showProgressBar);
        viewModel.getSaveAttemptSuccess().observe(this, navigateToAttemptsScreen -> {
            if (navigateToAttemptsScreen) {
                getMainActivity().navigateToAttemptScreen();
            }
        });
    }

    private void initListener() {
        binding.singUpBtn.setOnClickListener(view -> {
            if (getMainActivity() != null) {
                getMainActivity().navigateToSignUpScreen();
            }
        });
        binding.singInBtn.setOnClickListener(view -> viewModel.checkCredentials(Objects.requireNonNull(binding.emailEdt.getText()).toString(),
                Objects.requireNonNull(binding.passwordEdt.getText()).toString()));
    }
}