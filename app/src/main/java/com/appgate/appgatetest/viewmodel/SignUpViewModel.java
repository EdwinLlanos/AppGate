package com.appgate.appgatetest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.appgate.appgatetest.R;
import com.appgate.appgatetest.base.viewmodel.BaseViewModel;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SignUpUseCase;

import static com.appgate.appgatetest.util.Constant.EMAIL_PATTERN;
import static com.appgate.appgatetest.util.Constant.PASSWORD_PATTERN;

public class SignUpViewModel extends BaseViewModel {
    private final SignUpUseCase signInUseCase;
    private final SaveAttemptUseCase saveAttemptUseCase;

    private final MutableLiveData<Boolean> navigateToSignInScreen;

    public SignUpViewModel(SignUpUseCase signInUseCase, SaveAttemptUseCase saveAttemptUseCase) {
        this.signInUseCase = signInUseCase;
        this.saveAttemptUseCase = saveAttemptUseCase;
        navigateToSignInScreen = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getNavigateToSignInScreen() {
        return navigateToSignInScreen;
    }

    public void checkCredentials(String email, String password, String passwordConfirm) {
        if (email.isEmpty()) {
            showMessage(R.string.warning_email_required);
            return;
        }
        if (password.isEmpty()) {
            showMessage(R.string.warning_password_required);
            return;
        }
        if (passwordConfirm.isEmpty()) {
            showMessage(R.string.warning_password_confirm_required);
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
        if (!password.equals(passwordConfirm)) {
            showMessage(R.string.warning_wrong_passwords);
            return;
        }
        encryptCredentials(email, password);
    }

    private void encryptCredentials(String email, String password) {
        signInUseCase.encryptCredentials(email, password, new OnRequestCompletedListener<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                if (response) {
                    navigateToSignInScreen.postValue(true);
                } else {
                    messageResource.postValue(R.string.error_unexpected);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                handleFailure(throwable);
            }
        });
    }
}
