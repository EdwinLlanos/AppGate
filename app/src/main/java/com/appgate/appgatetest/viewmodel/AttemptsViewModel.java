package com.appgate.appgatetest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.appgate.appgatetest.base.viewmodel.BaseViewModel;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import java.util.List;

public class AttemptsViewModel extends BaseViewModel {
    private final GetAttemptsUseCase getAttemptsUseCase;
    private final MutableLiveData<List<AttemptModel>> getAttemptsList;

    public AttemptsViewModel(GetAttemptsUseCase getAttemptsUseCase) {
        this.getAttemptsUseCase = getAttemptsUseCase;
        getAttemptsList = new MutableLiveData<>();
    }

    public LiveData<List<AttemptModel>> getAttemptList() {
        return getAttemptsList;
    }

    public void getAttempts() {
        getAttemptsUseCase.getAttemptsList(new OnRequestCompletedListener<List<AttemptModel>>() {
            @Override
            public void onSuccess(List<AttemptModel> response) {
                handleGetAttemptsSuccess(response);
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }

    private void handleGetAttemptsSuccess(List<AttemptModel> response) {
        getAttemptsList.postValue(response);
    }
}
