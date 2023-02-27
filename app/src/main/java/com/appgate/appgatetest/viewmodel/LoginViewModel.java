package com.appgate.appgatetest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.appgate.authentication.data.datasource.remote.model.TimeModel;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.LoginUseCase;

public class LoginViewModel extends ViewModel {
    private final LoginUseCase loginUseCase;
    private final MutableLiveData<String> message;

    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
        message = new MutableLiveData<>();
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void sigInAttempt(double latitude, double longitude) {
        loginUseCase.getAttemptDate(latitude, longitude, new OnRequestCompletedListener<TimeModel>() {
            @Override
            public void onSuccess(TimeModel response) {
                message.setValue(response.getTimeZone());
            }

            @Override
            public void onError(Throwable throwable) {
                message.setValue(throwable.getMessage());
            }
        });
    }
}
