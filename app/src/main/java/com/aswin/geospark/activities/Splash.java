package com.aswin.geospark.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.aswin.geospark.R;
import com.aswin.geospark.base.BaseActivity;
import com.aswin.geospark.common.AppPreference;
import com.aswin.geospark.common.Constants;
import com.aswin.locationtracker.LocationTracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class Splash extends BaseActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG ="aswin";

    private String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    private void splash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppPreference.getInstance(Splash.this).getBoolean(Constants.ISLOGGEDIN)){
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(Splash.this, SigninActivity.class));
                    finish();
                }
            }
        }, 3000);

    }


    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, permissions, 1);

        }else
            EnableGPSAutoMatically();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isDenied = false;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                Log.e(TAG, "permission granted " );
                EnableGPSAutoMatically();
            }else if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                Log.e(TAG, "permission denied " );
                isDenied = true;
            }

        }

        if (isDenied)
            checkPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        splash();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(Splash.this, 1000);

                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("Failed");
    }

    private void toast(String message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
