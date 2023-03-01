package com.appgate.appgatetest.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;

public class LocationService extends Service implements LocationListener {
    private static final String TAG = LocationService.class.getSimpleName();

    private final OnRequestCompletedListener<Location> listener;
    private final LocationManager mLocationManager;

    public LocationService(Context context, OnRequestCompletedListener<Location> listener) {
        this.listener = listener;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
    }

    public Boolean isLocationEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(TAG, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
            listener.onSuccess(location);
        } else {
            listener.onError(new Exception("No location found"));
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
