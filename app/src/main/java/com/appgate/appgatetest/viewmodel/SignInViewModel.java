package com.appgate.appgatetest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.appgate.appgatetest.R;
import com.appgate.appgatetest.base.viewmodel.BaseViewModel;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SaveLocationUseCase;
import com.appgate.authentication.domain.usecase.SignInUseCase;

import static com.appgate.appgatetest.util.Constant.EMAIL_PATTERN;
import static com.appgate.appgatetest.util.Constant.PASSWORD_PATTERN;
import static com.appgate.authentication.domain.model.AttemptStatus.FAILURE;
import static com.appgate.authentication.domain.model.AttemptStatus.SUCCESS;

public class SignInViewModel extends BaseViewModel {
    private final SignInUseCase signInUseCase;
    private final SaveLocationUseCase saveLocationUseCase;

    private final MutableLiveData<Boolean> navigateToAttemptsScreen;

    public SignInViewModel(SignInUseCase signInUseCase, SaveAttemptUseCase saveAttemptUseCase, SaveLocationUseCase saveLocationUseCase) {
        super(saveAttemptUseCase);
        this.signInUseCase = signInUseCase;
        this.saveLocationUseCase = saveLocationUseCase;
        navigateToAttemptsScreen = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getNavigateToAttemptsScreen() {
        return navigateToAttemptsScreen;
    }

    public void checkCredentials(String email, String password) {
        if (email.isEmpty()) {
            showMessage(R.string.warning_email_required);
            saveAttempt(FAILURE);
            return;
        }
        if (password.isEmpty()) {
            showMessage(R.string.warning_password_required);
            saveAttempt(FAILURE);
            return;
        }
        if (!email.matches(EMAIL_PATTERN)) {
            showMessage(R.string.warning_email_not_valid);
            saveAttempt(FAILURE);
            return;
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            showMessage(R.string.warning_password_not_valid);
            saveAttempt(FAILURE);
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

    public void setLocation(String latitude, String longitude) {
        saveLocationUseCase.saveLocation(latitude, longitude);
    }

    private void handlerValidateCredentialsSuccess(Boolean response) {
        if (response) {
            navigateToAttemptsScreen.postValue(true);
            saveAttempt(SUCCESS);
        } else {
            saveAttempt(FAILURE);
            messageResource.postValue(R.string.error_incorrect_credentials);
        }
    }
}
