package com.example.joseph.tlucampustour;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by Joseph on 9/12/2016.
 */
public class AudioPlayer extends MediaPlayer implements Serializable {

    private MediaPlayer myMediaPlayer;
    private int audioID;
    private OnCompletionListener myCompletionListener;

    public AudioPlayer(int audioFileID)
    {

        audioID = audioFileID;
    }

    public void play(Context c)
    {
        // Ensures that only one audio track is playing at any given time
        stop();
        myMediaPlayer = MediaPlayer.create(c, audioID);
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        myMediaPlayer.setOnCompletionListener(myCompletionListener);
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

    public boolean isPlaying()
    {
        return myMediaPlayer.isPlaying();
    }

    public void release()
    {
        super.release();
    }

    public int getCurrentPosition()
    {
        return super.getCurrentPosition();
    }

    public void seekTo(int pos)
    {
        myMediaPlayer.seekTo(pos);
    }

    public void setAudioCompletionListener(OnCompletionListener listener)
    {
        myCompletionListener = listener;
    }
}
