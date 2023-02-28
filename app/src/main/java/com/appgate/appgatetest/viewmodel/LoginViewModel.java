package com.appgate.appgatetest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.LoginUseCase;
import java.util.List;

public class LoginViewModel extends ViewModel {
    private final LoginUseCase loginUseCase;
    private final GetAttemptsUseCase getAttemptsUseCase;
    private final MutableLiveData<String> message;

    public LoginViewModel(LoginUseCase loginUseCase, GetAttemptsUseCase getAttemptsUseCase) {
        this.loginUseCase = loginUseCase;
        this.getAttemptsUseCase = getAttemptsUseCase;
        message = new MutableLiveData<>();
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void sigInAttempt(double latitude, double longitude) {
        loginUseCase.getAttemptDate(latitude, longitude, AttemptStatus.SUCCESS, new OnRequestCompletedListener<TimeResponse>() {
            @Override
            public void onSuccess(TimeResponse response) {
                message.setValue(response.getTimeZone());
            }

            @Override
            public void onError(Throwable throwable) {
                message.setValue(throwable.getMessage());
            }
        });
    }

    public void getAttempts() {
        getAttemptsUseCase.getAttemptsList(new OnRequestCompletedListener<List<AttemptModel>>() {
            @Override
            public void onSuccess(List<AttemptModel> response) {
                message.setValue(response.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                message.setValue(throwable.getMessage());
            }
        });
    }
}
