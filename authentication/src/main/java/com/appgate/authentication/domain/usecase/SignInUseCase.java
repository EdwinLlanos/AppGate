package com.appgate.authentication.domain.usecase;

import android.util.Pair;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import java.util.Objects;

public class SignInUseCase {
    private final AuthRepository authRepository;

    public SignInUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void validateCredentials(String email, String password, OnRequestCompletedListener<Boolean> listener) {
        authRepository.decryptCredentials(new OnRequestCompletedListener<Pair<String, String>>() {
            @Override
            public void onSuccess(Pair<String, String> response) {
                listener.onSuccess(Objects.equals(response.first, email) && Objects.equals(response.second, password));
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onError(throwable);
            }
        });
    }
}
