package com.appgate.authentication.domain.usecase;


import com.appgate.authentication.domain.model.AttemptModel;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GetAttemptsUseCaseTest {
    @Mock
    private AuthRepository authRepository;

    @Mock
    private OnRequestCompletedListener<List<AttemptModel>> listener;

    private GetAttemptsUseCase getAttemptsUseCase;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        getAttemptsUseCase = new GetAttemptsUseCase(authRepository);
    }

    @Test
    public void getAttemptsList_shouldCallAuthRepository_getAttempts() {
        // When
        getAttemptsUseCase.getAttemptsList(listener);

        // Then
        verify(authRepository, times(1)).getAttempts(listener);
    }

}