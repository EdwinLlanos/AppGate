package com.appgate.authentication.data.datasource.remote;

import com.appgate.authentication.data.datasource.remote.model.TimeResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class AutApiMapper {
    private static final String TIME_ZONE = "timeZone";
    private static final String CURRENT_LOCAL_TIME = "currentLocalTime";

    public TimeResponse createTimeModel(String result) throws JSONException {
        JSONObject object = new JSONObject(result);
        return new TimeResponse(object.getString(TIME_ZONE), object.getString(CURRENT_LOCAL_TIME));
    }
}
