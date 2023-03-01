package com.appgate.authentication.domain.usecase;

import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SignUpUseCaseTest {
    @Test
    public void testEncryptCredentials() {
        // Given
        String user = "edwinllanosa@gmail.com";
        String password = "Asdf1234$1";
        AuthRepository mockAuthRepository = mock(AuthRepository.class);
        OnRequestCompletedListener<Boolean> mockListener = mock(OnRequestCompletedListener.class);

        SignUpUseCase useCase = new SignUpUseCase(mockAuthRepository);

        // When
        useCase.encryptCredentials(user, password, mockListener);

        // Then
        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<OnRequestCompletedListener<Boolean>> listenerCaptor = ArgumentCaptor.forClass(OnRequestCompletedListener.class);
        verify(mockAuthRepository).encryptCredentials(userCaptor.capture(), passCaptor.capture(), listenerCaptor.capture());
        assertEquals(user, userCaptor.getValue());
        assertEquals(password, passCaptor.getValue());
        assertEquals(mockListener, listenerCaptor.getValue());
    }
}
