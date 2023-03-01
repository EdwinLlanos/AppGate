package com.appgate.appgatetest.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.appgate.appgatetest.R;
import com.appgate.appgatetest.base.viewmodel.BaseViewModel;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SignInUseCase;

import static com.appgate.appgatetest.util.Constant.EMAIL_PATTERN;
import static com.appgate.appgatetest.util.Constant.PASSWORD_PATTERN;
import static com.appgate.authentication.domain.model.AttemptStatus.FAILURE;
import static com.appgate.authentication.domain.model.AttemptStatus.SUCCESS;

public class SignInViewModel extends BaseViewModel {
    private final String TAG = SignInViewModel.class.getName();
    private final SignInUseCase signInUseCase;
    private final SaveAttemptUseCase saveAttemptUseCase;
    private final MutableLiveData<Boolean> navigateToAttemptsScreen;

    public SignInViewModel(SignInUseCase signInUseCase, SaveAttemptUseCase saveAttemptUseCase) {
        this.signInUseCase = signInUseCase;
        this.saveAttemptUseCase = saveAttemptUseCase;
        navigateToAttemptsScreen = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getNavigateToAttemptsScreen() {
        return navigateToAttemptsScreen;
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
        if (response) {
            saveAttemptSuccess();
        } else {
            saveAttemptFailure();
            messageResource.postValue(R.string.error_incorrect_credentials);
        }
    }

    private void saveAttemptFailure() {
        showLoading();
        double latitude = 4.9612794;
        double longitude = -73.9141911;
        saveAttemptUseCase.saveAttempt(latitude, longitude, FAILURE, new OnRequestCompletedListener<TimeResponse>() {
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

    private void saveAttemptSuccess() {
        showLoading();
        double latitude = 4.9612794;
        double longitude = -73.9141911;
        saveAttemptUseCase.saveAttempt(latitude, longitude, SUCCESS, new OnRequestCompletedListener<TimeResponse>() {
            @Override
            public void onSuccess(TimeResponse response) {
                navigateToAttemptsScreen.postValue(true);
                hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
                hideLoading();
            }
        });
    }

    private void showLoading() {
        loading.postValue(true);
    }

    private void hideLoading() {
        loading.postValue(false);
    }

    private void handleSaveAttemptSuccess(String response) {
        Log.d(TAG, response);
    }
}
