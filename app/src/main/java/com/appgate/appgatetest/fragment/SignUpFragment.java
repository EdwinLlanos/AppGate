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
import com.appgate.appgatetest.databinding.FragmentSignUpBinding;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.viewmodel.SignUpViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SignUpUseCase;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;

public class SignUpFragment extends Fragment implements DefaultLifecycleObserver {

    private FragmentSignUpBinding binding;
    private SignUpViewModel viewModel;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
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
        SignUpUseCase signUpUseCase = new Injector().createSignUpUseCase(getContext());
        SaveAttemptUseCase getAttemptsUseCase = new Injector().createSaveAttemptUseCase(getContext());
        ViewModelFactory factory = new ViewModelFactory(signUpUseCase, getAttemptsUseCase);

        viewModel = new ViewModelProvider(this, factory).get(SignUpViewModel.class);

        viewModel.getStateUI().observe(this, stateUIModel -> {
            if (stateUIModel.navigateToSignInScreen) {
                getMainActivity().navigateToSignInScreen();
            } else if (Objects.nonNull(stateUIModel.message)) {
                showMessage(stateUIModel.message);
            } else if (stateUIModel.messageResource != 0) {
                showMessage(getResources().getString(stateUIModel.messageResource));
            }
        });
    }

    private void initListener() {
        binding.singUpBtn.setOnClickListener(view -> {
            viewModel.checkCredentials(Objects.requireNonNull(binding.emailEdt.getText()).toString(),
                    Objects.requireNonNull(binding.passwordEdt.getText()).toString(),
                    Objects.requireNonNull(binding.passwordConfirmEdt.getText()).toString());
        });
    }

    private void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(binding.loginLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getContext();
    }
}