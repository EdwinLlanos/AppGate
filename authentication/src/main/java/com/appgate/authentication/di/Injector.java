package com.appgate.authentication.di;

import android.content.Context;
import androidx.annotation.NonNull;
import com.appgate.authentication.data.datasource.local.db.AuthenticationHandler;
import com.appgate.authentication.data.datasource.remote.AutApiMapper;
import com.appgate.authentication.data.datasource.remote.AuthApi;
import com.appgate.authentication.data.repository.AuthRepositoryImpl;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.LoginUseCase;
import com.appgate.authentication.network.RestClientHandler;

public class Injector {
    public LoginUseCase createLoginUseCase(Context context) {
        AuthRepository authRepository = createAuthRepository(context);
        return new LoginUseCase(authRepository);
    }

    public GetAttemptsUseCase createGetAttemptUseCase(Context context) {
        AuthRepository authRepository = createAuthRepository(context);
        return new GetAttemptsUseCase(authRepository);
    }

    @NonNull
    private AuthRepository createAuthRepository(Context context) {
        AuthenticationHandler authenticationHandler = new AuthenticationHandler(context);
        AutApiMapper autApiMapper = new AutApiMapper();
        RestClientHandler restClientHandler = new RestClientHandler();
        AuthApi authApi = new AuthApi(autApiMapper, restClientHandler);
        return new AuthRepositoryImpl(authApi, authenticationHandler);
    }
}
