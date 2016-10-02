package com.example.joseph.tlucampustour;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.*;

import static com.example.joseph.tlucampustour.Constants.*;

public class Directions extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TourStop destination;
    private String destName;
    private GoogleApiClient myGoogleClient;
    private GoogleMap mMap;
    private Location myLocation;
    private Location prevLocation;
    private boolean isMapInitialized;
    private Marker myMarker;
    private LocationRequest myLocationRequest;
    private double selectedLat;
    private double selectedLon;
    private double locationRadius;
    LatLng myPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Build Google API Client
        buildGoogleApiClient();

        // Get all needed Tour Stop information from passed data
        destination = getIntent().getExtras().getParcelable("Selected Stop");
        if (destination != null)
        {
            destName = destination.getName();
            selectedLat = destination.getLatitude();
            selectedLon = destination.getLongitude();
            locationRadius = destination.getRadius();
        }
        setTitle("Directions to " + destName);

        TextView myTV = (TextView) findViewById(R.id.tourStopName);
        myTV.setText(destName);

        // Set up Google Map for directions
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /* ***************************** NAVIGATION METHODS START HERE ****************************** */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.directions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
            case R.id.location_details:
                reachedTourStop();
                break;
            case R.id.satelliteMap:
                setMapSatelliteView();
                break;
            case R.id.normalMap:
                setMapNormalView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        finish();
    }

    // Called by return to list button
    public void returnToList(View view)
    {
        finish();
    }

    /* ***************************** END NAVIGATION METHODS ****************************** */
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

    // Performs initialization for needed Location data
    protected void createLocationRequest() {
        myLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates
        myLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        // Sets the fastest rate for active location updates
        myLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        myLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    // Called once the connection to Location Service has been established
    @Override
    public void onConnected(Bundle bundle) {

        // Need to set permissions if not done in manifest file by some versions of Android
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                myGoogleClient, myLocationRequest, this);
    }

    // Called from location listener if user arrives at a tour stop
    public void reachedTourStop()
    {
        Intent myIntent = new Intent(Directions.this, TourStopInfo.class);
        myIntent.putExtra("TourStop", destination);
        startActivity(myIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        float[] myDistance = new float[2];
        float[] destDistance = new float[2];

        if (isMapInitialized)
        {
            prevLocation = myLocation;
        }
        else
        {
            prevLocation = location;
        }

        myLocation = location;

        // Get distance between last user location and current location
        Location.distanceBetween(prevLocation.getLatitude(), prevLocation.getLongitude(),
                myLocation.getLatitude(), myLocation.getLongitude(), myDistance);

        // Get distance between user and selected destination
        Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                destination.getLatitude(), destination.getLongitude(), destDistance);

        // If user is within the radius of selected destination, load info activity
        if (Math.abs(destDistance[0]) < destination.getRadius())
        {
            reachedTourStop();
        }
        else if (!isMapInitialized || Math.abs(myDistance[0]) > UPDATE_LOCATION_DISTANCE)
        {
            // If user has moved far enough or if map is not initialized, update map
            updateMap();
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

    // Called when location change is detected
    private void updateMap()
    {

        if (myLocation == null || prevLocation == null)
        {
            Log.d("LocationService", "Location is null");
            return;
        }

        // Remove old location marker so that updated marker can be placed on map
        if ( myMarker != null)
        {
            myMarker.remove();
        }

        LatLng selectedPoint;
        // Add markers and update camera
        double myLat = myLocation.getLatitude();
        double myLon = myLocation.getLongitude();

        // Create points and add markers to map
        myPoint = new LatLng(myLat, myLon);
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(myPoint)
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        myMarker.showInfoWindow();

        if (!isMapInitialized)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TLUPoint, DEFAULT_CAMERA_ZOOM));
            selectedPoint = new LatLng(selectedLat, selectedLon);
            Marker destMarker = mMap.addMarker(new MarkerOptions()
                    .position(selectedPoint)
                    .title(destName));

            // Add radius outline to map
            mMap.addCircle(new CircleOptions()
                    .center(selectedPoint)
                    .radius(locationRadius)
                    .strokeColor(Color.BLUE));

            // Move camera to markers and keep within specified bounds
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(myPoint)
                    .include(selectedPoint)
                    .build();
            int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
            CameraUpdate camUpdate = CameraUpdateFactory.newLatLngBounds(bounds, margin);
            mMap.animateCamera(camUpdate);
            isMapInitialized = true;
        }
    }

    private void setMapSatelliteView()
    {
        if (mMap != null && mMap.getMapType() != GoogleMap.MAP_TYPE_SATELLITE)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    }

    private void setMapNormalView()
    {
        if (mMap != null && mMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myLocation = getCurrentLocation();
        updateMap();
    }
}
