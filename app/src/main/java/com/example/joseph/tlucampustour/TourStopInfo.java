package com.example.joseph.tlucampustour;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class TourStopInfo extends AppCompatActivity {

    private AudioPlayer myAudioPlayer;
    private boolean isAudioPlaying;
    private boolean isAudioPaused;
    private ImageButton playPauseButton;
    private AudioManager myAudioManager;
    private SeekBar volumeControl;


    private String currName;
    private int infoTextID;
    private int audioID;
    private int imgID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_stop_info);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TourStop currStop = getIntent().getExtras().getParcelable("TourStop");
        if (currStop != null)
        {
            currName = currStop.getName();
            infoTextID = currStop.getInfoTextID();
            imgID = currStop.getImage();
            audioID = currStop.getAudioFile();
        }

        setTitle(currName);

        TextView nameTV = (TextView) findViewById(R.id.tourStopName);
        nameTV.setText(currName);

        TextView infoTV = (TextView) findViewById(R.id.tourStopInfo);
        infoTV.setText(infoTextID);

        ImageView tourStopIV = (ImageView) findViewById(R.id.tourStopImg);
        tourStopIV.setImageResource(imgID);

        playPauseButton = (ImageButton) findViewById(R.id.playPauseButton);
        volumeControl = (SeekBar) findViewById(R.id.volumeControl);
        VolumeChangeListener myVolumeListener = new VolumeChangeListener();
        volumeControl.setOnSeekBarChangeListener(myVolumeListener);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeControl.setMax(myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeControl.setProgress(myAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));

        myAudioPlayer = new AudioPlayer(audioID);
        NarrationCompletionListener myCompletionListener = new NarrationCompletionListener();
        myAudioPlayer.setAudioCompletionListener(myCompletionListener);
    }

    @Override
    public void onStart()
    {
        super.onStart(); 
        playAudio();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
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
        // Need to add on completed listener to button to handle completion of audio clip

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

    // Used to determine if user adjusted volume with device volume controls
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if one of the volume keys were pressed
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        {
            volumeControl.setProgress(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
        return super.onKeyDown(keyCode, event);
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        myAudioPlayer.stop();
        finish();
    }


    @Override
    public void onStop()
    {
        super.onStop();
        myAudioPlayer.stop();
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
