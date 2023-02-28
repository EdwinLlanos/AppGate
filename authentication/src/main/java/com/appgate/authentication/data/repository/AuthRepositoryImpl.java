package com.appgate.authentication.data.repository;

import com.appgate.authentication.data.datasource.local.db.AuthenticationHandler;
import com.appgate.authentication.data.datasource.local.entity.AttemptEntity;
import com.appgate.authentication.data.datasource.remote.AuthApi;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import java.util.ArrayList;
import java.util.List;

public class AuthRepositoryImpl implements AuthRepository {

    private final AuthApi authApi;
    private final AuthenticationHandler authenticationHandler;

    public AuthRepositoryImpl(AuthApi authApi, AuthenticationHandler authenticationHandler) {
        this.authApi = authApi;
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    public void sigInAttempt(double lat, double lng, AttemptStatus status, OnRequestCompletedListener<TimeResponse> listener) {
        authApi.sigInAttempt(lat, lng, new OnRequestCompletedListener<TimeResponse>() {
            @Override
            public void onSuccess(TimeResponse response) {
                AttemptEntity attemptEntity = new AttemptEntity();
                attemptEntity.setTimeZone(response.getTimeZone());
                attemptEntity.setCurrentLocalTime(response.getCurrentLocalTime());
                attemptEntity.setStatus(status.getValue());
                authenticationHandler.addAttempt(attemptEntity);
                listener.onSuccess(response);
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onError(throwable);
            }
        });
    }

    @Override
    public void getAttempts(OnRequestCompletedListener<List<AttemptModel>> listener) {
        List<AttemptModel> attemptList = new ArrayList<>();
        for (AttemptEntity item : authenticationHandler.getAllAttempts()) {
            attemptList.add(new AttemptModel(item.getTimeZone(), item.getCurrentLocalTime(), item.getStatus()));
        }
        if (!attemptList.isEmpty()) {
            listener.onSuccess(attemptList);
        } else {
            listener.onError(new Exception("No data found"));
        }
    }
}
