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
import com.appgate.appgatetest.databinding.FragmentAttemptsBinding;
import com.appgate.appgatetest.factory.ViewModelFactory;
import com.appgate.appgatetest.viewmodel.AttemptsViewModel;
import com.appgate.authentication.di.Injector;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;

public class AttemptFragment extends Fragment implements DefaultLifecycleObserver {

    private FragmentAttemptsBinding binding;
    private AttemptsViewModel viewModel;

    public static AttemptFragment newInstance() {
        return new AttemptFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAttemptsBinding.inflate(inflater, container, false);
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
        GetAttemptsUseCase getAttemptsUseCase = new Injector().createGetAttemptsUseCase(getContext());
        ViewModelFactory factory = new ViewModelFactory(getAttemptsUseCase);

        viewModel = new ViewModelProvider(this, factory).get(AttemptsViewModel.class);

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

    }

    private void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(binding.loginLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getContext();
    }
}