package com.appgate.appgatetest.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.appgate.appgatetest.viewmodel.LoginViewModel;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.LoginUseCase;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final LoginUseCase loginUseCase;
    private final GetAttemptsUseCase getAttemptsUseCase;

    public ViewModelFactory(LoginUseCase loginUseCase, GetAttemptsUseCase getAttemptsUseCase) {
        this.loginUseCase = loginUseCase;
        this.getAttemptsUseCase = getAttemptsUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(loginUseCase, getAttemptsUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}