package com.appgate.appgatetest.viewmodel;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.appgate.appgatetest.R;
import com.appgate.appgatetest.model.StateUIModel;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.LoginUseCase;
import java.util.List;

public class LoginViewModel extends ViewModel {
    private final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private final LoginUseCase loginUseCase;
    private final GetAttemptsUseCase getAttemptsUseCase;
    private final MutableLiveData<StateUIModel> stateUI;
    private final StateUIModel stateUIModel = new StateUIModel();

    public LoginViewModel(LoginUseCase loginUseCase, GetAttemptsUseCase getAttemptsUseCase) {
        this.loginUseCase = loginUseCase;
        this.getAttemptsUseCase = getAttemptsUseCase;
        stateUI = new MutableLiveData<>(stateUIModel);
    }

    public LiveData<StateUIModel> getStateUI() {
        return stateUI;
    }

    public void sigInAttempt(double latitude, double longitude) {
        showLoading();
        loginUseCase.getAttemptDate(latitude, longitude, AttemptStatus.SUCCESS, new OnRequestCompletedListener<TimeResponse>() {
            @Override
            public void onSuccess(TimeResponse response) {
                handleSuccess(response.getTimeZone());
                hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
                hideLoading();
            }
        });
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

    public void checkCredentials(String email, String password) {
        decryptCredentials();
        if (email.isEmpty()) {
            showMessage(R.string.warning_email_required);
            return;
        }
        if (password.isEmpty()) {
            showMessage(R.string.warning_password_required);
            return;
        }
        if (!email.matches(EMAIL_PATTERN)) {
            showMessage(R.string.warning_email_not_valid);
            return;
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            showMessage(R.string.warning_password_not_valid);
            return;
        }
        // encryptCredentials(email, password);
    }

    public void decryptCredentials(){
        loginUseCase.decryptCredentials(new OnRequestCompletedListener<Pair<String, String>>() {
            @Override
            public void onSuccess(Pair<String, String> response) {
                stateUIModel.setMessage("Decrypt=>" + response.first + response.second);
                stateUI.setValue(stateUIModel);
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }

    private void encryptCredentials(String email, String password) {
        loginUseCase.encryptCredentials(email, password, new OnRequestCompletedListener<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                stateUIModel.setMessage("Encrypt=>" + response);
                stateUI.setValue(stateUIModel);
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }

    private void showMessage(int resource) {
        stateUIModel.setMessage(resource);
        stateUI.setValue(stateUIModel);
    }

    private void showLoading() {
        stateUIModel.setLoading(true);
        showMessage(R.string.general_loading);
    }

    private void hideLoading() {
        stateUIModel.setLoading(false);
        stateUI.setValue(stateUIModel);
    }

    private void handleSuccess(String response) {
        stateUIModel.setSuccess(true);
        stateUIModel.setMessage(response);
        stateUI.setValue(stateUIModel);
    }

    private void handleFailure(Throwable throwable) {
        stateUIModel.setError(true);
        stateUIModel.setMessage(throwable.getMessage());
        stateUI.setValue(stateUIModel);
    }
}
