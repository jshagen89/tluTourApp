package com.example.joseph.tlucampustour;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.*;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

import static com.example.joseph.tlucampustour.Constants.*;

public class Directions extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        DirectionCallback{

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
    private LatLng myPoint;
    private LatLng selectedPoint;
    private Polyline myMapRoute;
    private boolean dialogOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock orientation to portrait if device is a phone
        if(getResources().getBoolean(R.bool.portrait_only))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

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
                if (myGoogleClient.isConnected())
                {
                    stopLocationUpdates();
                    myGoogleClient.disconnect();
                }
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.location_details:
                reachedTourStop();
                break;
            case R.id.mapOptions:
                createOptionsDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createOptionsDialog()
    {
        FragmentManager fm = getFragmentManager();
        MapOptionsMenuFragment options = new MapOptionsMenuFragment();
        options.show(fm, "Map Options");
        dialogOpen = true;
    }

    // Process map option choices
    public void onOptionsSelected(Bundle choices)
    {
        // Map type user selection
        int mapViewChoice = choices.getInt(MAP_OPTIONS_RESULT, NORMAL_MAP_CHOICE);

        if (mapViewChoice == NORMAL_MAP_CHOICE)
        {
            setMapNormalView();
        }
        else if (mapViewChoice == SATELLITE_MAP_CHOICE) {
            setMapSatelliteView();
        }
        dialogOpen = false;
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

    // Called by return to list button
    public void returnToList(View view)
    {
        if (myGoogleClient.isConnected())
        {
            stopLocationUpdates();
            myGoogleClient.disconnect();
        }
        setResult(RESULT_OK);
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
            Log.d("LocationService", "Error: " + e);
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

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(myGoogleClient, this);
    }

    // Called from location listener if user arrives at a tour stop
    public void reachedTourStop()
    {
        Intent myIntent = new Intent(Directions.this, TourStopInfo.class);
        destination.setPlayed(true);
        myIntent.putExtra("TourStop", destination);
        startActivityForResult(myIntent, RESULT_OK);
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
        if (Math.abs(destDistance[0]) < destination.getRadius() && !destination.hasBeenPlayed() && !dialogOpen)
        {
            reachedTourStop();
        }
        else if (!isMapInitialized || Math.abs(myDistance[0]) > UPDATE_LOCATION_DISTANCE && !dialogOpen)
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
            // Move camera to TLU campus and place destination marker
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
        getDirections();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TLUPoint, DEFAULT_CAMERA_ZOOM));
        myLocation = getCurrentLocation();
        updateMap();
    }


    /* ***************************** END LOCATION METHODS ****************************** */
    /* ***************************** DIRECTION METHODS START HERE ****************************** */

    // Get Walking Directions for user to destination

    public void getDirections()
    {
        // Determine if user is on TLU campus
        float[] distance = new float[2];
        Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                TLU_CAMPUS_LAT, TLU_CAMPUS_LON, distance);
        String transMode = TransportMode.WALKING;
        // Set transport mode to driving if user is off campus
        if (distance[0] > TLU_CAMPUS_RADIUS)
        {
            transMode = TransportMode.DRIVING;
        }

        GoogleDirection.withServerKey(DIRECTIONS_API_KEY)
                .from(myPoint)
                .to(selectedPoint)
                .transportMode(transMode)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        String status = direction.getStatus();
        if(status.equals(RequestResult.OK))
        {
            Route route = direction.getRouteList().get(0);
            Leg leg = route.getLegList().get(0);
            ArrayList<LatLng> pointList = leg.getDirectionPoint();
            PolylineOptions polylineOptions = DirectionConverter.createPolyline(this, pointList, 5, Color.BLUE);

            // Remove old route before placing new one
            if (myMapRoute != null)
            {
                myMapRoute.remove();
            }
            myMapRoute = mMap.addPolyline(polylineOptions);
        }
        else
        {
            Toast toast = Toast.makeText(this, "Directions Not Available", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast toast = Toast.makeText(this, "Directions Not Available", Toast.LENGTH_SHORT);
        toast.show();
    }
}
