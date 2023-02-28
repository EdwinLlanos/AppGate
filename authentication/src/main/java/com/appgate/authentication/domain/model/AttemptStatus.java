package com.appgate.authentication.domain.model;

public enum AttemptStatus {
    SUCCESS(1), FAILURE(0);
    private final int value;

    AttemptStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
