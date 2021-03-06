package com.example.joseph.tlucampustour;

import android.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private ArrayList<TourStop> allTourStops;
    private String destName;
    private GoogleApiClient myGoogleClient;
    private GoogleMap mMap;
    private Location myLocation;
    private Location prevLocation;
    private boolean isMapInitialized;
    private Marker myMarker;
    private Marker passingMarker;
    private TourStop passingStop;
    private LocationRequest myLocationRequest;
    private double selectedLat;
    private double selectedLon;
    private double locationRadius;
    private LatLng myPoint;
    private LatLng selectedPoint;
    private boolean useHandicapEntries;
    private LatLng centerPoint;
    private Polyline myMapRoute;
    private boolean dialogOpen;
    private boolean infoDisplayed;
    private boolean gotDirections;
    private String transMode;
    private boolean gottenTransModePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");

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

        // Get user preferences
        getUserPrefs();

        // Get all needed Tour Stop information from passed data
        destination = getIntent().getExtras().getParcelable(SELECTED_STOP_EXTRA);
        allTourStops = getIntent().getExtras().getParcelableArrayList(TOUR_STOP_ARRAY_EXTRA);
        if (destination != null)
        {
            getDestinationInfo();
        }

        TextView myTV = (TextView) findViewById(R.id.tourStopName);
        String titleStart = getResources().getString(R.string.directions_title_start);
        myTV.setText(String.format("%s%s", titleStart, destName));

        // Set up Google Map for directions
        transMode = TransportMode.WALKING;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getUserPrefs()
    {
        // Use preferred entries or normal entry if no preference is available
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        useHandicapEntries = prefs.getBoolean(ACCESS_PREF_RESULT, false);
    }

    // initializes all instance variables for selected destination
    private void getDestinationInfo()
    {
        destName = destination.getName();
        selectedLat = destination.getEntryLatitude();
        selectedLon = destination.getEntryLongitude();
        centerPoint = new LatLng(destination.getCenterLatitude(), destination.getCenterLongitude());
        locationRadius = destination.getRadius();
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
        if (mMap != null && (transMode.equals(TransportMode.WALKING) || transMode.equals(TransportMode.DRIVING)))
        {
            FragmentManager fm = getFragmentManager();
            MapOptionsMenuFragment options = new MapOptionsMenuFragment().newInstance(mMap.getMapType(), transMode);
            String title = getResources().getString(R.string.map_options_title);
            options.show(fm, title);
            dialogOpen = true;
        }
        else
        {
            Toast toast = Toast.makeText(this, R.string.map_loading_message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Process map option choices
    public void onOptionsSelected(Bundle choices)
    {
        // Map type user selection
        int mapViewChoice = choices.getInt(MAP_OPTIONS_RESULT, NORMAL_MAP_CHOICE);
        String transportModeChoice = choices.getString(TRANSPORTATION_MODE_RESULT, TransportMode.WALKING);
        gottenTransModePref = !transMode.equals(transportModeChoice);

        if (mapViewChoice == NORMAL_MAP_CHOICE)
        {
            setMapNormalView();
        }
        else if (mapViewChoice == SATELLITE_MAP_CHOICE) {
            setMapSatelliteView();
        }

        // May want to save to preferences file later
        transMode = transportModeChoice;
        updateMap();

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

    // Reset boolean to allow another info activity to be displayed
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            infoDisplayed = false;
        }
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
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
        infoDisplayed = true;
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
                destination.getCenterLatitude(), destination.getCenterLongitude(), destDistance);

        // If user is not already viewing another info activity
        if (!infoDisplayed && !dialogOpen)
        {
            // If user is within the radius of selected destination, load info activity
            if (Math.abs(destDistance[0]) < destination.getRadius() && !destination.hasBeenPlayed())
            {
                updateMap();
                reachedTourStop();
            }
            else if (!isMapInitialized || Math.abs(myDistance[0]) > UPDATE_LOCATION_DISTANCE)
            {
                // If user has moved far enough or if map is not initialized, update map
                updateMap();
            }
            else if (!gotDirections)
            {
                // If directions were not successfully loaded, try again
                getDirections();
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

    // Called when location change is detected
    private void updateMap()
    {

        if (myLocation == null)
        {
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
                .title(getString(R.string.my_location_marker_title))
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

        // Check to see if user is passing stops other than their destination
        float[] distance = new float[2];

        // Remove previous marker for passing stop, if necessary
        if (passingMarker != null)
        {
            Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                    passingStop.getCenterLatitude(), passingStop.getCenterLongitude(), distance);
            if (distance[0] > passingStop.getRadius())
            {
                passingMarker.remove();
            }
        }

        for (TourStop stop : allTourStops)
        {
                double stopLat = stop.getEntryLatitude();
                double stopLon = stop.getEntryLongitude();

            Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                    stop.getCenterLatitude(), stop.getCenterLongitude(), distance);
            double stopRadius = stop.getRadius();
            if (!stop.getName().equals(destination.getName()) && distance[0] < stopRadius)
            {
                passingStop = stop;
                String stopName = stop.getName();
                LatLng stopPoint = new LatLng(stopLat, stopLon);
                passingMarker = mMap.addMarker(new MarkerOptions()
                    .position(stopPoint)
                    .title(stopName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                passingMarker.showInfoWindow();
            }
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
        DirectionsInfoClickListener myMapListener = new DirectionsInfoClickListener();
        mMap.setOnInfoWindowClickListener(myMapListener);
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

        // Set transport mode to driving if user is off campus and has not chosen walking directions
        if (distance[0] > TLU_CAMPUS_RADIUS && !gottenTransModePref)
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
            gotDirections = true;
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
            Toast toast = Toast.makeText(this, R.string.directions_conn_error, Toast.LENGTH_SHORT);
            toast.show();
            gotDirections = false;
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast toast = Toast.makeText(this, R.string.directions_conn_error, Toast.LENGTH_SHORT);
        toast.show();
        gotDirections = false;
    }


    // Listener for map to determine if user selects tour stop that they are passing or they select the destination
    private class DirectionsInfoClickListener implements GoogleMap.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick(Marker marker) {
            String selectedName = marker.getTitle();

            if (passingStop != null && selectedName.equals(passingStop.getName()))
            {
                Intent myIntent = new Intent(Directions.this, TourStopInfo.class);
                passingStop.setPlayed(true);
                myIntent.putExtra("TourStop", passingStop);
                startActivityForResult(myIntent, RESULT_OK);
            }
            else if (selectedName.equals(destName))
            {
                reachedTourStop();
            }
        }
    }
}
