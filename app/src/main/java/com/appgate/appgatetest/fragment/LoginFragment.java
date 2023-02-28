package com.appgate.appgatetest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import com.appgate.appgatetest.databinding.FragmentLoginBinding;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.viewmodel.LoginViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.LoginUseCase;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;

public class LoginFragment extends Fragment implements DefaultLifecycleObserver {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        initListener();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            getActivity().getLifecycle().addObserver(this);
        }
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
        if (getActivity() != null) {
            getActivity().getLifecycle().removeObserver(this);
        }
        LoginUseCase loginUseCase = new Injector().createLoginUseCase(getContext());
        GetAttemptsUseCase getAttemptsUseCase = new Injector().createGetAttemptUseCase(getContext());
        ViewModelFactory factory = new ViewModelFactory(loginUseCase, getAttemptsUseCase);
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
        viewModel.getStateUI().observe(this, stateUIModel -> {
            if (Objects.nonNull(stateUIModel.getMessage())) {
                showMessage(stateUIModel.getMessage());
            }
            if (stateUIModel.getMessageResource() != 0) {
                showMessage(getResources().getString(stateUIModel.getMessageResource()));
            }
        });
    }

    private void initListener() {
        binding.loginBtn.setOnClickListener(view -> {
            // Validate user and password, sending them to viewModel and then to use case
            viewModel.checkCredentials(Objects.requireNonNull(binding.emailEdt.getText()).toString(), Objects.requireNonNull(binding.passwordEdt.getText()).toString());
        });
    }

    private void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(binding.loginLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}