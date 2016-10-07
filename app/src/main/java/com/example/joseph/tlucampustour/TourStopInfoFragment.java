package com.example.joseph.tlucampustour;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Joseph on 10/7/2016.
 */

public class TourStopInfoFragment extends Fragment {

    private TourStop currStop;
    private String currName;

    public TourStopInfoFragment()
    {
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            currStop = arguments.getParcelable("TourStop");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initializeUI();
        initializeAudioControls();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View myView = inflater.inflate(R.layout.fragment_tour_stop_info, container, false);
        return myView;
    }


    private void initializeUI()
    {
        setTitle(currName);
        TextView nameTV = (TextView) findViewById(R.id.tourStopName);
        nameTV.setText(currName);
        TextView infoTV = (TextView) findViewById(R.id.tourStopInfo);
        infoTV.setText(infoTextID);
        ImageView tourStopIV = (ImageView) findViewById(R.id.tourStopImg);
        tourStopIV.setImageResource(imgID);
    }

    private void initializeAudioControls()
    {
        playPauseButton = (ImageButton) findViewById(R.id.playPauseButton);
        volumeControl = (SeekBar) findViewById(R.id.volumeControl);
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Volume control not present on small devices
        if (volumeControl != null)
        {
            TourStopInfo.VolumeChangeListener myVolumeListener = new TourStopInfo.VolumeChangeListener();
            volumeControl.setOnSeekBarChangeListener(myVolumeListener);
            volumeControl.setMax(myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeControl.setProgress(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
        myAudioPlayer = new AudioPlayer(audioID);
        TourStopInfo.NarrationCompletionListener myCompletionListener = new TourStopInfo.NarrationCompletionListener();
        myAudioPlayer.setAudioCompletionListener(myCompletionListener);
    }
}
