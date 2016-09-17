package com.example.joseph.tlucampustour;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

/**
 * Created by Joseph on 9/12/2016.
 */
public class AudioPlayer extends MediaPlayer{

    private MediaPlayer myMediaPlayer;
    private int audioID;

    public AudioPlayer(int audioFileID)
    {

        audioID = audioFileID;
    }

    public void play(Context c, OnCompletionListener listener)
    {
        // Ensures that only one audio track is playing at any given time
        stop();
        myMediaPlayer = MediaPlayer.create(c, audioID);
        myMediaPlayer.setOnCompletionListener(listener);
        myMediaPlayer.start();
    }

    public void pause()
    {
        myMediaPlayer.pause();
    }

    public void resume()
    {
        myMediaPlayer.start();
    }

    public void stop()
    {
        if (myMediaPlayer != null)
        {
            myMediaPlayer.release();
            myMediaPlayer = null;
        }
    }
}
