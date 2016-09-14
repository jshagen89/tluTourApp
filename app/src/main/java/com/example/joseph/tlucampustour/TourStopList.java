package com.example.joseph.tlucampustour;

import android.Manifest;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
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
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final static int NUM_TOUR_STOPS = 20;

    private GoogleApiClient myGoogleClient;
    private Location myLocation;
    private ListView locationListLV;
    private TourCursorAdapter myCursorAdapter;
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

        // Create CursorAdapter to populate list items in location list
        myCursorAdapter = new TourCursorAdapter(this,null,0);

        // layout list of tour stops and listen for user click events
        locationListLV = (ListView) findViewById(R.id.tourStopLV);
        locationListLV.setAdapter(myCursorAdapter);
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

        getLoaderManager().initLoader(0,null,this);
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

    // Reloads data from database each time data is added or deleted
    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }

    // Creates Loader and specifies where the data is coming from
    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,TourContentProvider.CONTENT_URI,null,null,null,null);
    }

    // Pass the returned data to the cursorAdapter when data is finished loading
    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        myCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        myCursorAdapter.swapCursor(null);
    }
}
