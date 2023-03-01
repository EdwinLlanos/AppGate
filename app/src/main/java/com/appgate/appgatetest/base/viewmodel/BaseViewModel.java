package com.appgate.appgatetest.base.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {
    public final MutableLiveData<String> messageString;
    public final MutableLiveData<Integer> messageResource;
    public final MutableLiveData<Boolean> loading;

    public BaseViewModel() {
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
}
