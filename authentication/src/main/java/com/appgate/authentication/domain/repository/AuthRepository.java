package com.appgate.authentication.domain.repository;

import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.model.AttemptStatus;
import java.util.List;

public interface AuthRepository {
    void sigInAttempt(double lat, double lng, AttemptStatus status, OnRequestCompletedListener<TimeResponse> listener);

    void getAttempts(OnRequestCompletedListener<List<AttemptModel>> listener);
}
