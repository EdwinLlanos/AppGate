package com.appgate.authentication.data.datasource.remote.model;

public class TimeModel {
    private String timeZone;
    private String currentLocalTime;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCurrentLocalTime() {
        return currentLocalTime;
    }

    public void setCurrentLocalTime(String currentLocalTime) {
        this.currentLocalTime = currentLocalTime;
    }
}