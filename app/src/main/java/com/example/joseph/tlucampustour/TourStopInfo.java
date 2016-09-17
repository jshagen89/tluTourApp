package com.example.joseph.tlucampustour;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TourStopInfo extends AppCompatActivity {

    private AudioPlayer myAudioPlayer;
    private boolean isAudioPlaying;
    private boolean isAudioPaused;
    private TourStop currStop;
    private String currName;
    private int infoTextID;
    private int audioID;
    private int imgID;
    private ImageButton playPauseButton;
    private ImageButton stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_stop_info);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        currStop = getIntent().getExtras().getParcelable("TourStop");
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
        stopButton = (ImageButton) findViewById(R.id.stopButton);
        myAudioPlayer = new AudioPlayer(audioID);
        NarrationCompletionListener myCompletionListener = new NarrationCompletionListener();
        myAudioPlayer.setAudioCompletionListener(myCompletionListener);
    }

    @Override
    public void onStart()
    {
        super.onStart();
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
}
