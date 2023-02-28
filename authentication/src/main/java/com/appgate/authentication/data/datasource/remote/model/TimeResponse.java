package com.appgate.authentication.data.datasource.remote.model;

public class TimeResponse {
    private final String timeZone;
    private final String currentLocalTime;

    public TimeResponse(String timeZone, String currentLocalTime) {
        this.timeZone = timeZone;
        this.currentLocalTime = currentLocalTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getCurrentLocalTime() {
        return currentLocalTime;
    }
}
