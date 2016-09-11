package com.example.joseph.tlucampustour;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;

public class TourStopList extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final static int NUM_TOUR_STOPS = 20;

    private GoogleApiClient myGoogleClient;
    private Location myLocation;
    private ListView locationListLV;
    private TourStop[] myTourStops = new TourStop[NUM_TOUR_STOPS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_stop_list);
        setTitle("Tour Stops");

        // disable back button...user should end tour to go back to begining screen
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Create the Play Services client object
        buildGoogleApiClient();

        getTourStops();

        // Create ArrayAdapter to populate list items in location list
        ArrayAdapter<TourStop> tourListAdapter = new ArrayAdapter<TourStop>(this,
                R.layout.tour_stop_list_item, myTourStops);

        // layout list of tour stops and listen for user click events
        locationListLV = (ListView) findViewById(R.id.tourStopLV);
        locationListLV.setAdapter(tourListAdapter);
        locationListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // pass selected tour stop info to new intent
                TourStop selectedStop = (TourStop) locationListLV.getAdapter().getItem(i);
                Intent myIntent = new Intent(TourStopList.this, Directions.class);
                myIntent.putExtra("Selected Stop", selectedStop);
                startActivity(myIntent);
            }
        });
    }

    // Creates the Google API Client with Location Services
    protected synchronized void buildGoogleApiClient() {
        myGoogleClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    // Retrieves all tour stops and populates list
    private void getTourStops()
    {
        myTourStops[0] = new TourStop("Martin Luther Statue");
        myTourStops[1] = new TourStop("AT&T & Moody Science Complex");
        myTourStops[2] = new TourStop("Emma Frey");
        myTourStops[3] = new TourStop("Tschope Hall");
        myTourStops[4] = new TourStop("Baldus, Clifton, Trinity Housing");
        myTourStops[5] = new TourStop("Krost and Health Sciences");
        myTourStops[6] = new TourStop("Weston Ranch");
        myTourStops[7] = new TourStop("Chapel of the Abiding Presence");
        myTourStops[8] = new TourStop("Campus Ministry/Servant Leadership");
        myTourStops[9] = new TourStop("Hein Dining Hall");
        myTourStops[10] = new TourStop("Centennial Hall Courtyard");
        myTourStops[11] = new TourStop("Jackson Auditorium");
        myTourStops[12] = new TourStop("Sports Complex/Athletic Training");
        myTourStops[13] = new TourStop("Fitness Center");
        myTourStops[14] = new TourStop("Graduation Walk");
        myTourStops[15] = new TourStop("Alumni Student Center");
        myTourStops[16] = new TourStop("Blumburg Memorial Library");
        myTourStops[17] = new TourStop("Schuech Fine Arts");
        myTourStops[18] = new TourStop("Langner Hall");
        myTourStops[19] = new TourStop("Alumni Plaza");

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
