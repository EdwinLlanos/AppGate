package com.appgate.appgatetest.model;

public class StateUIModel {

    private Boolean loading;
    private Boolean success;
    private Boolean error;
    private String message;
    private int messageResource;

    public StateUIModel() {
    }

    public Boolean getLoading() {
        return loading;
    }

    public void setLoading(Boolean loading) {
        this.loading = loading;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageResource() {
        return messageResource;
    }
    public void setMessage(int messageResource) {
        this.messageResource = messageResource;
    }
}
