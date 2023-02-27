package com.appgate.authentication.domain.repository;

import com.appgate.authentication.data.datasource.remote.model.TimeModel;

public interface AuthRepository {
    void sigInAttempt(double lat, double lng, OnRequestCompletedListener<TimeModel> listener);
}
