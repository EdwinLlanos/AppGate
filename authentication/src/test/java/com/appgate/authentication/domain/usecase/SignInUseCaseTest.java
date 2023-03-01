package com.appgate.authentication.domain.usecase;

import android.util.Pair;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class SignInUseCaseTest {
    @Mock
    private AuthRepository authRepository;

    @Mock
    private OnRequestCompletedListener<Boolean> listener;

    private SignInUseCase signInUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        signInUseCase = new SignInUseCase(authRepository);
    }

    @Test
    public void testValidateCredentials_withInvalidCredentials_shouldReturnFalse() {
        // Given
        String email = "edwinllanosa@gmail.com";
        String password = "Asdf1234$1";
        Pair<String, String> credentials = new Pair<>("ewdin@wordbox.ia", "asdf1234$1");
        doAnswer(invocation -> {
            OnRequestCompletedListener<Pair<String, String>> callback = invocation.getArgument(0);
            callback.onSuccess(credentials);
            return null;
        }).when(authRepository).decryptCredentials(any());
        // When
        signInUseCase.validateCredentials(email, password, listener);
        // Then
        verify(listener).onSuccess(eq(false));
    }

    @Test
    public void testValidateCredentials_withError_shouldCallOnError() {
        // Given
        String email = "edwinllanosa@gmail.com";
        String password = "Asdf1234$1";
        Throwable error = new RuntimeException("Something went wrong");
        doAnswer(invocation -> {
            OnRequestCompletedListener<Pair<String, String>> callback = invocation.getArgument(0);
            callback.onError(error);
            return null;
        }).when(authRepository).decryptCredentials(any());
        // When
        signInUseCase.validateCredentials(email, password, listener);
        // Then
        verify(listener).onError(eq(error));
    }
}