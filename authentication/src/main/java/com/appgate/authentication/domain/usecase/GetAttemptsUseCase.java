package com.appgate.authentication.domain.usecase;

import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import java.util.List;

public class GetAttemptsUseCase {
    private final AuthRepository authRepository;

    public GetAttemptsUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void getAttemptsList(OnRequestCompletedListener<List<AttemptModel>> listener) {
        authRepository.getAttempts(listener);
    }
}
