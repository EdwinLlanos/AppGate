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
import com.appgate.appgatetest.MainActivity;
import com.appgate.appgatetest.databinding.FragmentSignInBinding;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.viewmodel.SignInViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SignInUseCase;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;

public class SignInFragment extends Fragment implements DefaultLifecycleObserver {

    private FragmentSignInBinding binding;
    private SignInViewModel viewModel;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
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
        SignInUseCase signInUseCase = new Injector().createSignInUseCase(getContext());
        SaveAttemptUseCase saveAttemptUseCase = new Injector().createSaveAttemptUseCase(getContext());
        ViewModelFactory factory = new ViewModelFactory(signInUseCase, saveAttemptUseCase);
        viewModel = new ViewModelProvider(this, factory).get(SignInViewModel.class);
        viewModel.getStateUI().observe(this, stateUIModel -> {
            if (stateUIModel.navigateToAttemptsScreen) {
                getMainActivity().navigateToAttemptScreen();
            } else if (Objects.nonNull(stateUIModel.message)) {
                showMessage(stateUIModel.message);
            } else if (stateUIModel.messageResource != 0) {
                showMessage(getResources().getString(stateUIModel.messageResource));
            }
        });
    }

    private void initListener() {
        binding.singUpBtn.setOnClickListener(view -> {
            if (getMainActivity() != null) {
                getMainActivity().navigateToSignUpScreen();
            }
        });
        binding.singInBtn.setOnClickListener(view -> {
            viewModel.checkCredentials(Objects.requireNonNull(binding.emailEdt.getText()).toString(), Objects.requireNonNull(binding.passwordEdt.getText()).toString());
        });
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getContext();
    }

    private void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(binding.loginLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}