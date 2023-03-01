package com.appgate.appgatetest.base.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import java.util.concurrent.Executors;

public class BaseViewModel extends ViewModel {
    private final String TAG = BaseViewModel.class.getName();
    public MutableLiveData<String> messageString;
    public MutableLiveData<Integer> messageResource;
    public MutableLiveData<Boolean> loading;
    public MutableLiveData<Boolean> saveAttemptSuccess;
    private SaveAttemptUseCase saveAttemptUseCase;

    public BaseViewModel() {
    }

    public BaseViewModel(SaveAttemptUseCase saveAttemptUseCase) {
        this.saveAttemptUseCase = saveAttemptUseCase;
        messageString = new MutableLiveData<>("");
        messageResource = new MutableLiveData<>(0);
        loading = new MutableLiveData<>(false);
        saveAttemptSuccess = new MutableLiveData<>(false);
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

    public LiveData<Boolean> getSaveAttemptSuccess() {
        return saveAttemptSuccess;
    }

    public void showMessage(int resource) {
        messageResource.setValue(resource);
    }

    public void handleFailure(Throwable throwable) {
        messageString.postValue(throwable.getMessage());
    }

    public void saveAttempt(AttemptStatus status) {
        showLoading();
        Executors.newSingleThreadExecutor().execute(() ->
                saveAttemptUseCase.saveAttempt(status, new OnRequestCompletedListener<TimeResponse>() {
                    @Override
                    public void onSuccess(TimeResponse response) {
                        handleSaveAttemptSuccess(status, response.toString());
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        handleFailure(throwable);
                        hideLoading();
                    }
                }));
    }

    public void showLoading() {
        loading.postValue(true);
    }

    public void hideLoading() {
        loading.postValue(false);
    }

    private void handleSaveAttemptSuccess(AttemptStatus status, String response) {
        if (status.equals(AttemptStatus.SUCCESS)) {
            saveAttemptSuccess.postValue(true);
        }
        Log.d(TAG, response);
    }
}
