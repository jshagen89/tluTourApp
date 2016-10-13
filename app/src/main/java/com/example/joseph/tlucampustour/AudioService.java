package com.example.joseph.tlucampustour;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 9/18/2016.
 */
public class AudioService extends Service implements MediaPlayer.OnPreparedListener
{
    private AudioPlayer myAudioPlayer;

    public AudioService(int audioID, MediaPlayer.OnCompletionListener myListener)
    {
        myAudioPlayer = new AudioPlayer(audioID);
        myAudioPlayer.setOnCompletionListener(myListener);
        myAudioPlayer.setOnPreparedListener(this);
        myAudioPlayer.prepareAsync();
    }

    public void pauseAudio()
    {
        myAudioPlayer.pause();
    }

    public void resumeAudio()
    {
        myAudioPlayer.resume();
    }

    public void stopAudio()
    {
        myAudioPlayer.stop();
    }

    public void playAudio()
    {
        myAudioPlayer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }
}
