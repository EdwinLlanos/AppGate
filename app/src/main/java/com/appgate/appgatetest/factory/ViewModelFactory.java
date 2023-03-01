package com.appgate.appgatetest.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.appgate.appgatetest.viewmodel.AttemptsViewModel;
import com.appgate.appgatetest.viewmodel.SignInViewModel;
import com.appgate.appgatetest.viewmodel.SignUpViewModel;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SaveLocationUseCase;
import com.appgate.authentication.domain.usecase.SignInUseCase;
import com.appgate.authentication.domain.usecase.SignUpUseCase;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private SignInUseCase signInUseCase;
    private SignUpUseCase signUpUseCase;
    private SaveAttemptUseCase saveAttemptUseCase;
    private SaveLocationUseCase saveLocationUseCase;
    private GetAttemptsUseCase getAttemptsUseCase;

    public ViewModelFactory(SignInUseCase signInUseCase, SaveAttemptUseCase saveAttemptUseCase, SaveLocationUseCase saveLocationUseCase) {
        this.signInUseCase = signInUseCase;
        this.saveAttemptUseCase = saveAttemptUseCase;
        this.saveLocationUseCase = saveLocationUseCase;
    }

    public ViewModelFactory(SignUpUseCase signUpUseCase, SaveAttemptUseCase saveAttemptUseCase) {
        this.signUpUseCase = signUpUseCase;
        this.saveAttemptUseCase = saveAttemptUseCase;
    }

    public ViewModelFactory(GetAttemptsUseCase getAttemptsUseCase) {
        this.getAttemptsUseCase = getAttemptsUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignInViewModel.class)) {
            return (T) new SignInViewModel(signInUseCase, saveAttemptUseCase, saveLocationUseCase);
        }
        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(signUpUseCase, saveAttemptUseCase);
        }

        if (modelClass.isAssignableFrom(AttemptsViewModel.class)) {
            return (T) new AttemptsViewModel(getAttemptsUseCase);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}