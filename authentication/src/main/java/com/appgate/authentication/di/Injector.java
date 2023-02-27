package com.appgate.authentication.di;

import com.appgate.authentication.data.datasource.remote.AutApiMapper;
import com.appgate.authentication.data.datasource.remote.AuthApi;
import com.appgate.authentication.data.repository.AuthRepositoryImpl;
import com.appgate.authentication.domain.repository.AuthRepository;
import com.appgate.authentication.domain.usecase.LoginUseCase;
import com.appgate.authentication.network.RestClientHandler;

public class Injector {
    public LoginUseCase createLoginUseCase() {
        AutApiMapper autApiMapper = new AutApiMapper();
        RestClientHandler restClientHandler = new RestClientHandler();
        AuthApi authApi = new AuthApi(autApiMapper, restClientHandler);
        AuthRepository authRepository = new AuthRepositoryImpl(authApi);
        return new LoginUseCase(authRepository);
    }
}
