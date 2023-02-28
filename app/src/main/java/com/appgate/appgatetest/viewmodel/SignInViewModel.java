package com.appgate.appgatetest.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.appgate.appgatetest.R;
import com.appgate.appgatetest.model.StateUIModel;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SignInUseCase;

import static com.appgate.appgatetest.util.Constant.EMAIL_PATTERN;
import static com.appgate.appgatetest.util.Constant.PASSWORD_PATTERN;
import static com.appgate.authentication.domain.model.AttemptStatus.FAILURE;
import static com.appgate.authentication.domain.model.AttemptStatus.SUCCESS;

public class SignInViewModel extends ViewModel {
    private final String TAG = SignInViewModel.class.getName();
    private final SignInUseCase signInUseCase;
    private final SaveAttemptUseCase saveAttemptUseCase;
    private final MutableLiveData<StateUIModel> stateUI;
    private StateUIModel stateUIModel = new StateUIModel();

    public SignInViewModel(SignInUseCase signInUseCase, SaveAttemptUseCase saveAttemptUseCase) {
        this.signInUseCase = signInUseCase;
        this.saveAttemptUseCase = saveAttemptUseCase;
        stateUI = new MutableLiveData<>(stateUIModel);
    }

    public LiveData<StateUIModel> getStateUI() {
        return stateUI;
    }

    public void checkCredentials(String email, String password) {
        if (email.isEmpty()) {
            showMessage(R.string.warning_email_required);
            saveAttemptFailure();
            return;
        }
        if (password.isEmpty()) {
            showMessage(R.string.warning_password_required);
            saveAttemptFailure();
            return;
        }
        if (!email.matches(EMAIL_PATTERN)) {
            showMessage(R.string.warning_email_not_valid);
            saveAttemptFailure();
            return;
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            showMessage(R.string.warning_password_not_valid);
            saveAttemptFailure();
            return;
        }
        validateCredentials(email, password);
    }

    public void validateCredentials(String email, String password) {
        signInUseCase.validateCredentials(email, password, new OnRequestCompletedListener<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                handlerValidateCredentialsSuccess(response);
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }

    private void handlerValidateCredentialsSuccess(Boolean response) {
        stateUIModel = new StateUIModel();
        if (response) {
            saveAttemptSuccess();
        } else {
            saveAttemptFailure();
            stateUIModel.messageResource = R.string.error_incorrect_credentials;
        }
        stateUI.setValue(stateUIModel);
    }

    private void saveAttemptFailure() {
        showLoading();
        double latitude = 4.9612794;
        double longitude = -73.9141911;
        saveAttemptUseCase.saveAttempt(latitude, longitude, FAILURE, new OnRequestCompletedListener<TimeResponse>() {
            @Override
            public void onSuccess(TimeResponse response) {
                hideLoading();
                handleSaveAttemptSuccess(response.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
                hideLoading();
            }
        });
    }

    private void saveAttemptSuccess() {
        showLoading();
        double latitude = 4.9612794;
        double longitude = -73.9141911;
        saveAttemptUseCase.saveAttempt(latitude, longitude, SUCCESS, new OnRequestCompletedListener<TimeResponse>() {
            @Override
            public void onSuccess(TimeResponse response) {
                stateUIModel.navigateToAttemptsScreen = true;
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

    private void handleSaveAttemptSuccess(String response) {
        Log.d(TAG, response);
        stateUIModel.isSuccess = true;
        stateUIModel.messageResource = R.string.message_attempt_save_successful;
        stateUI.setValue(stateUIModel);
    }

    private void handleFailure(Throwable throwable) {
        stateUIModel.isError = true;
        stateUIModel.message = throwable.getMessage();
        stateUI.setValue(stateUIModel);
    }
}
