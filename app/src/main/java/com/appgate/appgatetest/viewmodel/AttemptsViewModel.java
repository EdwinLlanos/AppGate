package com.appgate.appgatetest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.appgate.appgatetest.R;
import com.appgate.appgatetest.model.StateUIModel;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import java.util.List;

public class AttemptsViewModel extends ViewModel {
    private final GetAttemptsUseCase getAttemptsUseCase;
    private final MutableLiveData<StateUIModel> stateUI;
    private final StateUIModel stateUIModel = new StateUIModel();

    public AttemptsViewModel(GetAttemptsUseCase getAttemptsUseCase) {
        this.getAttemptsUseCase = getAttemptsUseCase;
        stateUI = new MutableLiveData<>(stateUIModel);
    }

    public LiveData<StateUIModel> getStateUI() {
        return stateUI;
    }

    public void getAttempts() {
        showLoading();
        getAttemptsUseCase.getAttemptsList(new OnRequestCompletedListener<List<AttemptModel>>() {
            @Override
            public void onSuccess(List<AttemptModel> response) {
                handleSuccess(response.toString());
                hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
                hideLoading();
            }
        });
    }

    private void showMessage(int resource) {
        stateUIModel.messageResource = resource;
        stateUI.setValue(stateUIModel);
    }

    private void showLoading() {
        stateUIModel.isLoading = true;
        showMessage(R.string.message_loading);
    }

    private void hideLoading() {
        stateUIModel.isLoading = false;
        stateUI.setValue(stateUIModel);
    }

    private void handleSuccess(String response) {
        stateUIModel.isSuccess = true;
        stateUIModel.message = response;
        stateUI.setValue(stateUIModel);
    }

    private void handleFailure(Throwable throwable) {
        stateUIModel.isError = true;
        stateUIModel.message = throwable.getMessage();
        stateUI.setValue(stateUIModel);
    }
}
