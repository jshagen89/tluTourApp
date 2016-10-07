package com.example.joseph.tlucampustour;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import static com.example.joseph.tlucampustour.Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.example.joseph.tlucampustour.Constants.UPDATE_INTERVAL_IN_MILLISECONDS;

public class TourStopInfo extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TourStop currStop;
    private AudioPlayer myAudioPlayer;
    private boolean isAudioPlaying;
    private boolean isAudioPaused;
    private ImageButton playPauseButton;
    private AudioManager myAudioManager;
    private SeekBar volumeControl;
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

        if (getActionBar() != null)
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        currStop = getIntent().getExtras().getParcelable("TourStop");
        if (currStop != null)
        {
            currName = currStop.getName();
            infoTextID = currStop.getInfoTextID();
            imgID = currStop.getImage();
            audioID = currStop.getAudioFile();
        }

        setContentView(R.layout.activity_tour_stop_info);
        FragmentManager fm = getSupportFragmentManager();
        Fragment myFragment = fm.findFragmentById(R.id.fragmentContainer);

        if (myFragment != null)
        {
            myFragment = new TourStopInfoFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable("TourStop", currStop);
            myFragment.setArguments(arguments);
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, myFragment)
                    .commit();
        }

        buildGoogleApiClient();
    }


    @Override
    public void onStart()
    {
        super.onStart();
        playAudio();
    }

    @Override
    public  void onPause()
    {
        super.onPause();
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
                finish();
                break;
        }
        return true;
    }

    private void playAudio()
    {
        myAudioPlayer.play(this);
        isAudioPlaying = true;
        playPauseButton.setImageResource(R.drawable.ic_pause_black_48dp);
    }

    private void togglePauseAudio()
    {
        if (isAudioPaused)
        {
            myAudioPlayer.resume();
            isAudioPaused = false;
            playPauseButton.setImageResource(R.drawable.ic_pause_black_48dp);
        }
        else
        {
            myAudioPlayer.pause();
            isAudioPaused = true;
            playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
        }
    }

    // Called by play/pause button for audio controls
    public void playPauseAudio(View view)
    {
        if (isAudioPlaying)
        {
            togglePauseAudio();
        }
        else
        {
            playAudio();
        }
    }

    // Called by skip previous button
    public void restartTrack(View view)
    {
        playAudio();
    }

    // Needed for Stop button with view component needed
    public void stopAudio(View view)
    {
        myAudioPlayer.stop();
        isAudioPlaying = false;
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
    }

    // Needed for audio completion listener..since no view is available
    public void audioFinished()
    {
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
        myAudioPlayer.stop();
        isAudioPlaying = false;
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        if (myGoogleClient.isConnected())
        {
            stopLocationUpdates();
            myGoogleClient.disconnect();
        }
        myAudioPlayer.stop();
        finish();
    }


    @Override
    public void onStop()
    {
        myAudioPlayer.stop();
        super.onStop();
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

        if (distance[0] > currStop.getRadius() && !myAudioPlayer.isPlaying())
        {
            finish();
        }
    }

    // Adjust seekbar volume control if user presses volume buttons
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (volumeControl != null && action == KeyEvent.ACTION_DOWN) {
                    volumeControl.setProgress(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                }

        }
        return super.dispatchKeyEvent(event);
    }

    private class NarrationCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            audioFinished();
        }
    }

    private class VolumeChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
