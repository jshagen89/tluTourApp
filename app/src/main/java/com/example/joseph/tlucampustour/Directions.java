package com.example.joseph.tlucampustour;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class Directions extends AppCompatActivity implements OnMapReadyCallback {

    private TourStop destination;
    private GoogleMap mMap;
    private MapView myMV;
    private Location myLocation;
    private double myLat;
    private double myLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        // Set up Google Map for directions
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get all needed Tour Stop information from passed data
        destination = getIntent().getExtras().getParcelable("Selected Stop");
        myLat = getIntent().getExtras().getDouble("Latitude");
        myLon = getIntent().getExtras().getDouble("Longitude");
        String destName = "";
        if (destination != null)
        {
            destName = destination.getName();
        }
        setTitle("Directions to " + destName);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView myTV = (TextView) findViewById(R.id.tourStopName);
        myTV.setText(destName);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        finish();
    }

    // Called by return to list button
    public void returnToList(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    // Called if user arrives at a tour stop
    public void reachedTourStop(View view)
    {
        Intent myIntent = new Intent(Directions.this, TourStopInfo.class);
        myIntent.putExtra("TourStop", destination);
        startActivity(myIntent);
    }

    private void updateMap()
    {
        if (mMap == null)
            return;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMap();
    }

}
