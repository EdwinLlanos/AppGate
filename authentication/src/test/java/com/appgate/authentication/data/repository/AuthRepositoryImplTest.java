package com.appgate.authentication.data.repository;

import com.appgate.authentication.data.datasource.local.db.AuthenticationHelper;
import com.appgate.authentication.data.datasource.local.entity.AttemptEntity;
import com.appgate.authentication.data.datasource.local.entity.LocationEntity;
import com.appgate.authentication.data.datasource.local.keystore.KeyStoreHelper;
import com.appgate.authentication.data.datasource.remote.AuthApi;
import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import com.appgate.authentication.domain.model.AttemptStatus;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


public class AuthRepositoryImplTest {
    @Mock
    private AuthApi authApi;

    @Mock
    private AuthenticationHelper authenticationHelper;

    @Mock
    private KeyStoreHelper keyStoreHelper;

    @Mock
    private OnRequestCompletedListener<TimeResponse> listener;

    private AuthRepositoryImpl authRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authRepository = new AuthRepositoryImpl(authApi, authenticationHelper, keyStoreHelper);
    }

    @Test
    public void testSaveAttemptSuccess() {
        // Given
        AttemptStatus status = AttemptStatus.SUCCESS;
        String latitude = "4.9612794";
        String longitude = "-73.9141911";
        String timeZone = "UTC";
        String currentLocalTime = "2023-02-28T22:43:32.5672665";
        TimeResponse response = new TimeResponse(timeZone, currentLocalTime);
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setLatitude(latitude);
        locationEntity.setLongitude(longitude);
        when(authenticationHelper.getLastLocation()).thenReturn(locationEntity);

        // When
        authRepository.saveAttempt(status, listener);

        // Then
        ArgumentCaptor<OnRequestCompletedListener<TimeResponse>> argumentCaptor = ArgumentCaptor.forClass(OnRequestCompletedListener.class);
        verify(authApi).sigInAttempt(eq(latitude), eq(longitude), argumentCaptor.capture());
        argumentCaptor.getValue().onSuccess(response);

        ArgumentCaptor<AttemptEntity> attemptEntityCaptor = ArgumentCaptor.forClass(AttemptEntity.class);
        verify(authenticationHelper).addAttempt(attemptEntityCaptor.capture());
        AttemptEntity attemptEntity = attemptEntityCaptor.getValue();
        assertEquals(status.getValue(), attemptEntity.getStatus());
        assertEquals(timeZone, attemptEntity.getTimeZone());
        assertEquals(currentLocalTime, attemptEntity.getCurrentLocalTime());

        verify(listener).onSuccess(response);
        verifyNoMoreInteractions(authApi, listener);
    }

    @Test
    public void testSaveAttemptError() {
        // Given
        AttemptStatus status = AttemptStatus.SUCCESS;
        String latitude = "4.9612794";
        String longitude = "-73.9141911";
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setLatitude(latitude);
        locationEntity.setLongitude(longitude);
        Throwable error = new RuntimeException("Something went wrong");
        when(authenticationHelper.getLastLocation()).thenReturn(locationEntity);

        // When
        authRepository.saveAttempt(status, listener);

        // Then
        ArgumentCaptor<OnRequestCompletedListener<TimeResponse>> argumentCaptor = ArgumentCaptor.forClass(OnRequestCompletedListener.class);
        verify(authApi).sigInAttempt(eq(latitude), eq(longitude), argumentCaptor.capture());
        argumentCaptor.getValue().onError(error);

        verify(listener).onError(error);
        verifyNoMoreInteractions(authApi, listener);
    }
}


