package com.appgate.authentication.domain.repository;

public interface OnRequestCompletedListener<T> {
    void onSuccess(T response);

    void onError(Throwable throwable);
}
