package com.example.joseph.tlucampustour;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 9/18/2016.
 */
public class TourLocationService extends Service {

    private GoogleApiClient myGoogleClient;
    private Location myLocation;
    private LocationRequest myLocationRequest;
    private LocationListener myListener;
    private Activity currActivity;
    private GoogleApiClient.ConnectionCallbacks myCallback;
    private GoogleApiClient.OnConnectionFailedListener myFailListener;

    public TourLocationService(Activity activity, GoogleApiClient.ConnectionCallbacks callBack,
                               GoogleApiClient.OnConnectionFailedListener failListener,
                               LocationListener listener) {

        super();
        buildGoogleApiClient();
        currActivity = activity;
        myCallback = callBack;
        myFailListener = failListener;
        myListener = listener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Creates the Google API Client with Location Services
    protected void buildGoogleApiClient() {
        myGoogleClient = new GoogleApiClient.Builder(currActivity)
                .addConnectionCallbacks(myCallback)
                .addOnConnectionFailedListener(myFailListener)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void connectClient()
    {
        myGoogleClient.connect();
    }

    protected boolean isClientConnected()
    {
        return myGoogleClient.isConnected();
    }

    protected void disconnectClient()
    {
        stopLocationUpdates();
        myGoogleClient.disconnect();
    }

    // Performs initialization for needed Location data
    protected void createLocationRequest() {
        myLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates
        myLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates
        myLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Gets the users current location. Called from Activity using the service
    protected Location getCurrentLocation() {
        try {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleClient);
            return myLocation;
        } catch (SecurityException e) {
            return null;
        }
    }

    // Called once the connection to Location Service has been established
    public void onConnected(Bundle bundle) {

        // Need to set permissions if not done in manifest file by some versions of Android
        int permissionCheck = ContextCompat.checkSelfPermission(currActivity, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (myLocation == null)
        {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleClient);
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                myGoogleClient, myLocationRequest, myListener);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleClient, myListener);
    }
}
