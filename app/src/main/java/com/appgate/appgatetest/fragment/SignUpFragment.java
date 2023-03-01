package com.appgate.appgatetest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.appgate.appgatetest.base.fragment.BaseFragment;
import com.appgate.appgatetest.databinding.FragmentSignUpBinding;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.viewmodel.SignUpViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SignUpUseCase;
import java.util.Objects;

public class SignUpFragment extends BaseFragment {
    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        initListener();
        setProgressBar(binding.progressBar);
        setRootLayout(binding.rootLayout);
        return binding.getRoot();
    }

    @Override
    public void initUI() {
        initViewModel();
        initObservers();
    }

    private void initViewModel() {
        SignUpUseCase signUpUseCase = new Injector().createSignUpUseCase(getContext());
        SaveAttemptUseCase getAttemptsUseCase = new Injector().createSaveAttemptUseCase(getContext());
        ViewModelFactory factory = new ViewModelFactory(signUpUseCase, getAttemptsUseCase);

        viewModel = new ViewModelProvider(this, factory).get(SignUpViewModel.class);
    }

    private void initObservers() {
        viewModel.getSaveAttemptSuccess().observe(this, navigateToSignInScreen -> {
            if (navigateToSignInScreen) {
                getMainActivity().navigateToSignInScreen();
            }
        });

        viewModel.getMessageString().observe(this, this::showMessage);
        viewModel.getMessageResource().observe(this, this::showMessage);
        viewModel.getLoading().observe(this, this::showProgressBar);
    }

    private void initListener() {
        binding.singUpBtn.setOnClickListener(view -> {
            viewModel.checkCredentials(Objects.requireNonNull(binding.emailEdt.getText()).toString(),
                    Objects.requireNonNull(binding.passwordEdt.getText()).toString(),
                    Objects.requireNonNull(binding.passwordConfirmEdt.getText()).toString());
        });
    }
}