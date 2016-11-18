package com.example.joseph.tlucampustour;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import java.util.ArrayList;

import static com.example.joseph.tlucampustour.Constants.*;

public class TourStopList extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private ArrayList<TourStop> allTourStops;
    private GoogleApiClient myGoogleClient;
    private Location myLocation;
    private ListView locationListLV;
    private LocationRequest myLocationRequest;
    private boolean infoDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock orientation to portrait if device is a phone
        if(getResources().getBoolean(R.bool.portrait_only))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_tour_stop_list);
        setTitle(getString(R.string.tour_stops_list_title));

        // disable back button...user should end tour to go back to beginning screen
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Get all Tour Stops from database
        getTourStops();

        // Create the Play Services client object
        buildGoogleApiClient();

        // layout list of tour stops and listen for user click events
        locationListLV = (ListView) findViewById(R.id.tourStopLV);
        TourArrayAdapter myAdapter = new TourArrayAdapter(this, allTourStops);
        locationListLV.setAdapter(myAdapter);
        ListClickListener myClickListener = new ListClickListener();
        locationListLV.setOnItemClickListener(myClickListener);

        // Used to only allow one tour stop info activity to be displayed at a time
        infoDisplayed = false;
    }

    // Retrieves all TourStops from the database and populates arrayList
    private void getTourStops()
    {
        TourStopDataSource myDataSource = new TourStopDataSource(this);
        myDataSource.open();
        allTourStops = myDataSource.getAllTourStops(this);
        myDataSource.close();
    }

    // Called by End Tour button on tour stop list view
    public void endTour(View view)
    {
        if (myGoogleClient.isConnected())
        {
            stopLocationUpdates();
            myGoogleClient.disconnect();
        }
        finish();
    }

    /* ***************************** NAVIGATION METHODS START HERE ****************************** */

    // Called if user presses the back button
    @Override
    public void onBackPressed() {
        stopLocationUpdates();
        if (myGoogleClient.isConnected())
        {
            myGoogleClient.disconnect();
        }
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        myGoogleClient.connect();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!myGoogleClient.isConnected())
        {
            myGoogleClient.connect();
        }
    }

    /* ***************************** NAVIGATION METHODS END HERE ****************************** */
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


    // Called when the user has been prompted at runtime to grant permissions
    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] perms, @NonNull int[] results) {
        if (reqCode == 1) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    // Called once the connection to Location Service has been established
    @Override
    public void onConnected(Bundle bundle) {

        // Need to set permissions if not done in manifest file by some versions of Android
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (myLocation == null)
        {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleClient);
            startLocationUpdates();
        }
    }

    protected void createLocationRequest() {
        myLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates
        myLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates
        myLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // NEED TO FIGURE OUT HOW TO GET PERMISSION TO USE LOCATION FOR OLDER APIS
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
    public void onLocationChanged(Location location) {
        myLocation = location;
        double myLat = myLocation.getLatitude();
        double myLon = myLocation.getLongitude();
        float[] distance = new float[2];

        for (TourStop tourStop : allTourStops)
        {
            Location.distanceBetween(myLat, myLon, tourStop.getCenterLatitude(), tourStop.getCenterLongitude(), distance);
            if (distance[0] < tourStop.getRadius() && !infoDisplayed && !tourStop.hasBeenPlayed())
            {
                Intent myIntent = new Intent(TourStopList.this, TourStopInfo.class);
                tourStop.setPlayed(true);
                myIntent.putExtra("TourStop", tourStop);
                infoDisplayed = true;
                startActivityForResult(myIntent, RESULT_OK);
            }
        }
    }

    // If connection is suspended, attempt to reconnect
    @Override
    public void onConnectionSuspended(int i) {
        myGoogleClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(this, R.string.location_conn_error, Toast.LENGTH_SHORT);
        toast.show();
    }

    /* ***************************** LOCATION METHODS END HERE ****************************** */


    private class ListClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            // pass selected tour stop info to new intent
            TourArrayAdapter myAdapter = (TourArrayAdapter) locationListLV.getAdapter();
            TourStop selectedStop = myAdapter.getTourStop(i);
            Intent myIntent = new Intent(TourStopList.this, Directions.class);
            myIntent.putExtra(SELECTED_STOP_EXTRA, selectedStop);
            myIntent.putParcelableArrayListExtra(TOUR_STOP_ARRAY_EXTRA, allTourStops);
            infoDisplayed = true;
            startActivityForResult(myIntent, RESULT_OK);
        }
    }

    // Reset boolean to allow another info activity to be displayed
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            infoDisplayed = false;
        }
    }
}
