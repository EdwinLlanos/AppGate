package com.appgate.authentication.data.datasource.remote;

import com.appgate.authentication.data.datasource.remote.model.TimeModel;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import com.appgate.authentication.network.RestClientHandler;
import org.json.JSONException;

public class AuthApi {
    private static final String TIME_ZONE_END_POINT = "https://www.timeapi.io/api/TimeZone/coordinate?";
    private static final String LATITUDE_PARAMETER = "latitude";
    private static final String LONGITUDE_PARAMETER = "longitude";
    private final AutApiMapper autApiMapper;
    private final RestClientHandler restClientHandler;

    public AuthApi(AutApiMapper autApiMapper, RestClientHandler restClientHandler) {
        this.autApiMapper = autApiMapper;
        this.restClientHandler = restClientHandler;
    }

    public void sigInAttempt(double lat, double lng, OnRequestCompletedListener<TimeModel> callable) {
        String timeZoneEndPoint = TIME_ZONE_END_POINT + LATITUDE_PARAMETER + "=" + lat + "&" + LONGITUDE_PARAMETER + "+=" + lng + "";

        restClientHandler.execute(timeZoneEndPoint, new OnRequestCompletedListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    callable.onSuccess(autApiMapper.createTimeModel(response));
                } catch (JSONException e) {
                    callable.onError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                callable.onError(throwable);
            }
        });
    }
}
