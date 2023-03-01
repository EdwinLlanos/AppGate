package com.appgate.authentication.domain.repository;

import android.util.Pair;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.model.AttemptStatus;
import java.util.List;

public interface AuthRepository {
    void saveAttempt(AttemptStatus status, OnRequestCompletedListener<TimeResponse> listener);

    void getAttempts(OnRequestCompletedListener<List<AttemptModel>> listener);

    void encryptCredentials(String user, String password, OnRequestCompletedListener<Boolean> listener);

    void decryptCredentials(OnRequestCompletedListener<Pair<String, String>> listener);

    void saveLocation(String latitude, String longitude);
}
