package com.example.joseph.tlucampustour;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import static com.example.joseph.tlucampustour.Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.example.joseph.tlucampustour.Constants.UPDATE_INTERVAL_IN_MILLISECONDS;

public class TourStopInfo extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TourStop currStop;
    TourStopInfoFragment myFragment;
    private GoogleApiClient myGoogleClient;
    private LocationRequest myLocationRequest;
    private Location myLocation;
    private String currName;
    private int infoTextID;
    private int audioID;
    private int imgID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Lock orientation to portrait if device is a phone
        if(getResources().getBoolean(R.bool.portrait_only))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        currStop = getIntent().getExtras().getParcelable("TourStop");
        if (currStop != null)
        {
            currName = currStop.getName();
            infoTextID = currStop.getInfoTextID();
            imgID = currStop.getImage();
            audioID = currStop.getAudioFile();
        }
        setTitle(currName);

        setContentView(R.layout.activity_tour_stop_info);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        myFragment = new TourStopInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable("TourStop", currStop);
        myFragment.setArguments(arguments);
        ft.replace(R.id.fragmentContainer, myFragment);
        ft.commit();

        buildGoogleApiClient();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (myGoogleClient.isConnected())
                {
                    stopLocationUpdates();
                    myGoogleClient.disconnect();
                }
                setResult(RESULT_OK);
                finish();
                break;
        }
        return true;
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        if (myGoogleClient.isConnected())
        {
            stopLocationUpdates();
            myGoogleClient.disconnect();
        }
        setResult(RESULT_OK);
        finish();
    }

    /* ***************************** LOCATION METHODS START HERE ****************************** */

    // Creates the Google API Client with Location Services
    protected synchronized void buildGoogleApiClient() {
        myGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

    }

    protected void createLocationRequest() {
        myLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates
        myLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates
        myLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
                myGoogleClient, myLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleClient, this);
    }

    // Gets the users current location
    private Location getCurrentLocation() {
        try {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleClient);
            return myLocation;
        } catch (SecurityException e) {
            return null;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Need to set permissions if not done in manifest file by some versions of Android
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (myLocation == null)
        {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleClient);
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        myGoogleClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Location", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        float[] distance = new float[2];
        double myLat = myLocation.getLatitude();
        double myLon = myLocation.getLongitude();
        Location.distanceBetween(myLat, myLon, currStop.getLatitude(), currStop.getLongitude(), distance);
        AudioPlayer myAudioPlayer = myFragment.getAudioPlayer();

        if (distance[0] > currStop.getRadius() && !myAudioPlayer.isPlaying())
        {
            finish();
        }
    }
}
