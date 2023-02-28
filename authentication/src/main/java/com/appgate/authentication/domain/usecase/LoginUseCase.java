package com.appgate.authentication.domain.usecase;

import android.util.Pair;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;

public class LoginUseCase {
    private final AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void getAttemptDate(double lat, double lng, AttemptStatus status, OnRequestCompletedListener<TimeResponse> listener) {
        authRepository.sigInAttempt(lat, lng, status, listener);
    }

    public void encryptCredentials(String user, String password, OnRequestCompletedListener<Boolean> listener) {
        authRepository.encryptCredentials(user, password, listener);
    }

    public void decryptCredentials(OnRequestCompletedListener<Pair<String, String>> listener) {
        authRepository.decryptCredentials(listener);
    }
}
