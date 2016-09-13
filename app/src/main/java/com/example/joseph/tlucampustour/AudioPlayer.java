package com.example.joseph.tlucampustour;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;

/**
 * Created by Joseph on 9/12/2016.
 */
public class AudioPlayer {

    private MediaPlayer myMediaPlayer;
    private int audioID;

    public AudioPlayer(int audioFileID)
    {
        audioID = audioFileID;
    }

    public void play(Context c)
    {
        // Ensures that only one audio track is playing at any given time
        stop();

        myMediaPlayer = MediaPlayer.create(c, audioID);
        AudioCompletionListener myCompletionListener = new AudioCompletionListener();
        myMediaPlayer.setOnCompletionListener(myCompletionListener);
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

    // Listens for audio track to complete
    private class AudioCompletionListener implements MediaPlayer.OnCompletionListener
    {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            stop();
        }
    }
}
