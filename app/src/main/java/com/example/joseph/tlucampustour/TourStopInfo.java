package com.example.joseph.tlucampustour;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TourStopInfo extends AppCompatActivity {

    private AudioPlayer myAudioPlayer;
    private boolean isAudioPlaying;
    private TourStop currStop;
    private String currName;
    private String audioPath;
    private String imgPath;

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
            imgPath = currStop.getImage();
            audioPath = currStop.getAudioFile();
        }

        if (audioPath == null)
        {
            audioPath = "It is Null";
        }
        setTitle(currName);

        TextView nameTV = (TextView) findViewById(R.id.tourStopName);
        nameTV.setText(currName);

        Uri audioUri = Uri.parse("R.raw." + audioPath);
        Log.d("AudioTest", audioUri.toString());
        myAudioPlayer = new AudioPlayer(0);
    }

    public void playAudio(View view)
    {
        myAudioPlayer.play(this);
        isAudioPlaying = true;
    }

    public void pauseAudio(View view)
    {
        // Need to handle if audio is in stopped state and pause is pressed

        if (isAudioPlaying)
        {
            myAudioPlayer.pause();
            isAudioPlaying = false;
        }
        else
        {
            myAudioPlayer.resume();
            isAudioPlaying = true;
        }
    }

    public void stopAudio(View view)
    {
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

}
