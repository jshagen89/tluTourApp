package com.example.joseph.tlucampustour;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Joseph on 10/7/2016.
 */

public class TourStopInfoFragment extends Fragment {

    private TourStop currStop;
    private String currName;
    private int infoTextID;
    private int[] imgIDs;
    private int audioID;
    private ImageButton playPauseButton;
    private ImageView imgView;
    private ImageButton nextImg;
    private ImageButton prevImg;
    private int currImgNum;
    private TextView imgNum;
    private SeekBar volumeControl;
    private AudioManager myAudioManager;
    private AudioPlayer myAudioPlayer;
    private boolean isAudioPlaying;
    private boolean isAudioPaused;
    private boolean audioControlsInit;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            currStop = arguments.getParcelable("TourStop");
            currName = currStop.getName();
            infoTextID = currStop.getInfoTextID();
            imgIDs = currStop.getImages();
            audioID = currStop.getAudioFile();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View myView = inflater.inflate(R.layout.fragment_tour_stop_info, container, false);

        // Initialize UI elements
        TextView nameTV = (TextView) myView.findViewById(R.id.tourStopName);
        nameTV.setText(currName);
        TextView infoTV = (TextView) myView.findViewById(R.id.tourStopInfo);
        infoTV.setText(infoTextID);

        // Initialize Img components
        imgView = (ImageView) myView.findViewById(R.id.tourStopImg);
        imgView.setImageResource(imgIDs[0]);
        ChangeImageListener myImgControl = new ChangeImageListener();
        nextImg = (ImageButton) myView.findViewById(R.id.nextImg);
        prevImg = (ImageButton) myView.findViewById(R.id.prevImg);
        nextImg.setOnClickListener(myImgControl);
        prevImg.setOnClickListener(myImgControl);
        imgNum = (TextView) myView.findViewById(R.id.imgNum);
        currImgNum = 0;
        imgNum.setText("  " + (currImgNum+1) + " / " + imgIDs.length + "  ");

        // NEED TO LOOP THROUGH IMAGES HERE FOR IMAGE GALLERY ***********************************


        // Initialize audio controls
        playPauseButton = (ImageButton) myView.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new PlayPauseAudioListener());
        ImageButton stopButton = (ImageButton) myView.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new StopAudioListener());
        ImageButton restartTrack = (ImageButton) myView.findViewById(R.id.prevButton);
        restartTrack.setOnClickListener(new RestartTrackListener());
        volumeControl = (SeekBar) myView.findViewById(R.id.volumeControl);
        myAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // Volume control not present on small devices
        if (volumeControl != null)
        {
            VolumeChangeListener myVolumeListener = new VolumeChangeListener();
            volumeControl.setOnSeekBarChangeListener(myVolumeListener);
            volumeControl.setMax(myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeControl.setProgress(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }

        // Initialize Audio Player
        myAudioPlayer = new AudioPlayer(audioID);
        NarrationCompletionListener myCompletionListener = new NarrationCompletionListener();
        myAudioPlayer.setAudioCompletionListener(myCompletionListener);
        audioControlsInit = true;

        return myView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        playAudio();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (isAudioPlaying || isAudioPaused)
        {
            myAudioPlayer.stop();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (myAudioPlayer != null)
        {
            myAudioPlayer.release();
        }
    }

    private void playAudio()
    {
        myAudioPlayer.play(this.getActivity());
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

    // Needed for audio completion listener..since no view is available
    public void audioFinished()
    {
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
        myAudioPlayer.stop();
        isAudioPlaying = false;
    }

    // Used by prev and next img buttons to change displayed image
    private class ChangeImageListener implements View.OnClickListener {
        @Override
        public void onClick(View view)
        {
            if (view.getId() == nextImg.getId())
            {
                if (currImgNum < imgIDs.length - 1)
                {
                    currImgNum++;
                }
                else
                {
                    currImgNum = 0;
                }
            }
            else
            {
                if (currImgNum > 0)
                {
                    currImgNum--;
                }
                else
                {
                    currImgNum = imgIDs.length - 1;
                }
            }
            imgView.setImageResource(imgIDs[currImgNum]);
            imgNum.setText("  " + (currImgNum+1) + " / " + imgIDs.length + "  ");
        }
    }

    private class PlayPauseAudioListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (isAudioPlaying)
            {
                togglePauseAudio();
            }
            else
            {
                playAudio();
            }
        }
    }

    private class RestartTrackListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            playAudio();
        }
    }

    private class StopAudioListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            myAudioPlayer.stop();
            isAudioPlaying = false;
            playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
        }
    }

    // Used by hosting activity to get reference to audio player
    public AudioPlayer getAudioPlayer()
    {
        return myAudioPlayer;
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
            // Only set volume after initialization to prevent volume from resetting to 0 on init
            if (audioControlsInit)
            {
                myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}
