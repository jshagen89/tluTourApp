package com.example.joseph.tlucampustour;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LocationsList extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient myGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_list);
        setTitle("Locations");

        // Create the Play Services client object
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        myGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    private Location getCurrentLocation()
    {
        try
        {
            Location myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleClient);
            return myLocation;
        }
        catch (SecurityException e)
        {
            Log.d("location", "location is null");
            return null;
        }
        catch (Exception e)
        {
            Log.d("location", "ERROR " + e);
            return null;
        }
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        myGoogleClient.connect();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (myGoogleClient.isConnected())
        {
            myGoogleClient.disconnect();
        }
    }

    // Called when the user has been prompted at runtime to grant permissions
    @Override
    public void onRequestPermissionsResult(int reqCode, String[] perms, int[] results)
    {
        if (reqCode == 1)
        {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Need to set permissions if not done in manifest file by some versions of Android
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            updateUILocation();
        }
    }

    private void updateUILocation()
    {
        String nullStr = "Null Location w/ Google";
        TextView locationTV = (TextView) findViewById(R.id.locationText);
        Location myLoc = getCurrentLocation();
        if (myLoc != null)
        {
            locationTV.setText(String.valueOf(myLoc.getLatitude()));
        }
        else
        {
            GoogleApiAvailability avail = GoogleApiAvailability.getInstance();
            int code = avail.isGooglePlayServicesAvailable(this);
            if (code == ConnectionResult.SUCCESS)
                locationTV.setText(nullStr);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        myGoogleClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
