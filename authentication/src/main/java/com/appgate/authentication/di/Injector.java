package com.appgate.authentication.di;

import android.content.Context;
import com.appgate.authentication.data.datasource.local.db.AuthenticationHelper;
import com.appgate.authentication.data.datasource.local.keystore.KeyStoreHelper;
import com.appgate.authentication.data.datasource.remote.AutApiMapper;
import com.appgate.authentication.data.datasource.remote.AuthApi;
import com.appgate.authentication.data.repository.AuthRepositoryImpl;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.usecase.GetAttemptsUseCase;
import com.appgate.authentication.domain.usecase.SaveAttemptUseCase;
import com.appgate.authentication.domain.usecase.SaveLocationUseCase;
import com.appgate.authentication.domain.usecase.SignInUseCase;
import com.appgate.authentication.domain.usecase.SignUpUseCase;
import com.appgate.authentication.network.RestClientHandler;

public class Injector {
    public SignInUseCase createSignInUseCase(Context context) {
        AuthRepository authRepository = createAuthRepository(context);
        return new SignInUseCase(authRepository);
    }

    public SignUpUseCase createSignUpUseCase(Context context) {
        AuthRepository authRepository = createAuthRepository(context);
        return new SignUpUseCase(authRepository);
    }

    public GetAttemptsUseCase createGetAttemptsUseCase(Context context) {
        AuthRepository authRepository = createAuthRepository(context);
        return new GetAttemptsUseCase(authRepository);
    }

    public SaveAttemptUseCase createSaveAttemptUseCase(Context context) {
        AuthRepository authRepository = createAuthRepository(context);
        return new SaveAttemptUseCase(authRepository);
    }

    public SaveLocationUseCase createSaveLocationUseCase(Context context) {
        AuthRepository authRepository = createAuthRepository(context);
        return new SaveLocationUseCase(authRepository);
    }

    private AuthRepository createAuthRepository(Context context) {
        AuthenticationHelper authenticationHelper = new AuthenticationHelper(context);
        AutApiMapper autApiMapper = new AutApiMapper();
        RestClientHandler restClientHandler = new RestClientHandler();
        AuthApi authApi = new AuthApi(autApiMapper, restClientHandler);
        KeyStoreHelper keyStoreHelper = new KeyStoreHelper(context);
        return new AuthRepositoryImpl(authApi, authenticationHelper, keyStoreHelper);
    }
}
