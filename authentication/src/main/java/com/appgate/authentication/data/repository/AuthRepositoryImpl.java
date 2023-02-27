package com.appgate.authentication.data.repository;

import com.appgate.authentication.data.datasource.remote.AuthApi;
import com.appgate.authentication.data.datasource.remote.model.TimeModel;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;

public class AuthRepositoryImpl implements AuthRepository {

    private final AuthApi authApi;

    public AuthRepositoryImpl(AuthApi authApi) {
        this.authApi = authApi;
    }


    @Override
    public void sigInAttempt(double lat, double lng, OnRequestCompletedListener<TimeModel> listener) {
        authApi.sigInAttempt(lat, lng, listener);
    }
}
