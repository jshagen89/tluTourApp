package com.example.joseph.tlucampustour;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class TourStopList extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient myGoogleClient;
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_stop_list);
        setTitle("Locations");

        // Create the Play Services client object
        buildGoogleApiClient();

        // Create sample tour stops
        TourStop[] myTourStops = new TourStop[3];
        myTourStops[0] = new TourStop("Martin Luther Statue");
        myTourStops[1] = new TourStop("Tschope Hall");
        myTourStops[2] = new TourStop("Emma Frey");

        // Create ArrayAdapter to populate list items in location list
        ArrayAdapter<TourStop> tourListAdapter = new ArrayAdapter<TourStop>(this,
                android.R.layout.simple_list_item_1, myTourStops);
        ListView locationListLV = (ListView) findViewById(R.id.tourStopLV);
        locationListLV.setAdapter(tourListAdapter);
    }

    // Creates the Google API Client with Location Services
    protected synchronized void buildGoogleApiClient() {
        myGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    // Gets the users current location
    private Location getCurrentLocation()
    {
        try
        {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleClient);
            Log.d("location", "location is good");
            return myLocation;
        }
        catch (SecurityException e)
        {
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
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] perms, @NonNull int[] results)
    {
        if (reqCode == 1)
        {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
        }
    }

    // Called once the connection to Location Service has been established
    @Override
    public void onConnected(Bundle bundle) {

        // Need to set permissions if not done in manifest file by some versions of Android
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    // If connection is suspended, attempt to reconnect
    @Override
    public void onConnectionSuspended(int i) {
        myGoogleClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Location", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    // Called by End Tour button on tour stop list view
    public void endTour(View view)
    {
        myGoogleClient.disconnect();
        finish();
    }
}
