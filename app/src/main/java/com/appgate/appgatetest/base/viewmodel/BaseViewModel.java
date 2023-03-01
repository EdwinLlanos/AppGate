package com.appgate.appgatetest.base.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;

public class BaseViewModel extends ViewModel {
    private final String TAG = BaseViewModel.class.getName();
    public MutableLiveData<String> messageString;
    public MutableLiveData<Integer> messageResource;
    public MutableLiveData<Boolean> loading;
    private SaveAttemptUseCase saveAttemptUseCase;

    public BaseViewModel() {
    }

    public BaseViewModel(SaveAttemptUseCase saveAttemptUseCase) {
        this.saveAttemptUseCase = saveAttemptUseCase;
        messageString = new MutableLiveData<>("");
        messageResource = new MutableLiveData<>(0);
        loading = new MutableLiveData<>(false);
    }

    public LiveData<String> getMessageString() {
        return messageString;
    }

    public LiveData<Integer> getMessageResource() {
        return messageResource;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void showMessage(int resource) {
        messageResource.postValue(resource);
    }

    public void handleFailure(Throwable throwable) {
        messageString.postValue(throwable.getMessage());
    }

    public void saveAttempt(AttemptStatus status) {
        showLoading();
        saveAttemptUseCase.saveAttempt(status, new OnRequestCompletedListener<TimeResponse>() {
            @Override
            public void onSuccess(TimeResponse response) {
                handleSaveAttemptSuccess(response.toString());
                hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
                hideLoading();
            }
        });
    }

    public void showLoading() {
        loading.postValue(true);
    }

    public void hideLoading() {
        loading.postValue(false);
    }

    private void handleSaveAttemptSuccess(String response) {
        Log.d(TAG, response);
    }
}
