package com.appgate.authentication.domain.usecase;

import com.appgate.authentication.data.datasource.remote.model.TimeModel;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;

public class LoginUseCase {
    private final AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void getAttemptDate(double lat, double lng, OnRequestCompletedListener<TimeModel> listener) {
        authRepository.sigInAttempt(lat, lng, listener);
    }
}
