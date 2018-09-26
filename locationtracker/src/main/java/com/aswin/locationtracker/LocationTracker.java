package com.aswin.locationtracker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationTracker extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "aswin";
    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 30 * 1000;
    private static final float LOCATION_DISTANCE = 1f;

    private LocationListener locationListener ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();


        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        final FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        SettingsClient settingsClient = LocationServices.getSettingsClient(getApplicationContext());

        final LocationCallback locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.e(TAG, "onLocationResult: \nlat :\t"+locationResult.getLastLocation().getLatitude()+"\nlon :\t"+locationResult.getLastLocation().getLongitude() );

                RealmDB.with(getApplicationContext()).updateLocation(locationResult.getLocations());
            }
        };

        final LocationRequest locationRequest = new LocationRequest()
                .setInterval(LOCATION_INTERVAL)
                .setSmallestDisplacement(1f)
                .setFastestInterval(5 * 1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        LocationSettingsRequest locationSettingsRequest = builder.build();

        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e(TAG, "onSuccess: " );
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onFailure: " );
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " );
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind: " );
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}
