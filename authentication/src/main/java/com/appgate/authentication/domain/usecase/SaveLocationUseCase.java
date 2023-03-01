package com.appgate.authentication.domain.usecase;

import com.appgate.authentication.domain.repository.AuthRepository;

public class SaveLocationUseCase {
    private final AuthRepository authRepository;

    public SaveLocationUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void saveLocation(String latitude, String longitude) {
        authRepository.saveLocation(latitude, longitude);
    }
}
