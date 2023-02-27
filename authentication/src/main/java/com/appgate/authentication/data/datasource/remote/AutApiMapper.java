package com.appgate.authentication.data.datasource.remote;

import com.appgate.authentication.data.datasource.remote.model.TimeModel;
import org.json.JSONException;
import org.json.JSONObject;

public class AutApiMapper {
    private static final String TIME_ZONE = "timeZone";
    private static final String CURRENT_LOCAL_TIME = "currentLocalTime";

    public TimeModel createTimeModel(String result) throws JSONException {
        TimeModel timeModel = new TimeModel();
        JSONObject object = new JSONObject(result);
        timeModel.setTimeZone(object.getString(TIME_ZONE));
        timeModel.setCurrentLocalTime(object.getString(CURRENT_LOCAL_TIME));
        return timeModel;
    }
}
