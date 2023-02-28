package com.appgate.authentication.domain.usecase;

import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;

public class SignUpUseCase {
    private final AuthRepository authRepository;

    public SignUpUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void encryptCredentials(String user, String password, OnRequestCompletedListener<Boolean> listener) {
        authRepository.encryptCredentials(user, password, listener);
    }
}
