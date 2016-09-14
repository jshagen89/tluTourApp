package com.example.joseph.tlucampustour;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Joseph on 9/8/2016.
 */
public class TourStop implements Parcelable{

    private String name;
    private float latitude;
    private float longitude;
    private String audioFile;
    private String image;

    public TourStop(String name, float lat, float lon, String img, String audio)

    {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.image = img;
        this.audioFile = audio;
    }

    public String getName()

    {
        return this.name;
    }

    public float getLatitude()
    {
        return this.latitude;
    }

    public float getLongitude()
    {
        return this.longitude;
    }

    public String getAudioFile()
    {
        return this.audioFile;
    }

    public String getImage()
    {
        return this.image;
    }

    @Override
    public String toString()

    {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<TourStop> CREATOR
            = new Parcelable.Creator<TourStop>() {
        public TourStop createFromParcel(Parcel in) {
            return new TourStop(in);
        }

        public TourStop[] newArray(int size) {
            return new TourStop[size];
        }
    };

    private TourStop(Parcel in) {
        name = in.readString();
    }
}
