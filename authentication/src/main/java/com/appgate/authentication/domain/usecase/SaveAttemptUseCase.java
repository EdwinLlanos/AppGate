package com.appgate.authentication.domain.usecase;

import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;

public class SaveAttemptUseCase {
    private final AuthRepository authRepository;

    public SaveAttemptUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void saveAttempt(double lat, double lng, AttemptStatus status, OnRequestCompletedListener<TimeResponse> listener) {
        authRepository.saveAttempt(lat, lng, status, listener);
    }
}
