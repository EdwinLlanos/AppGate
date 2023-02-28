package com.appgate.authentication.domain.model;

public class AttemptModel {
    private final String timeZone;
    private final String currentLocalTime;
    private final int status;

    public AttemptModel(String timeZone, String currentLocalTime, int status) {
        this.timeZone = timeZone;
        this.currentLocalTime = currentLocalTime;
        this.status = status;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getCurrentLocalTime() {
        return currentLocalTime;
    }

    public int getStatus() {
        return status;
    }
}
