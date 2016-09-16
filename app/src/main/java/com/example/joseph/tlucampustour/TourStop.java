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
    private int infoTextID;
    private int imageID;
    private int audioID;

    public TourStop(String name, float lat, float lon, int infoText, int img, int audio)

    {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.infoTextID = infoText;
        this.imageID = img;
        this.audioID = audio;
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

    public int getInfoTextID() {
        return this.infoTextID;
    }

    public int getImage()
    {
        return this.imageID;
    }

    public int getAudioFile()
    {
        return this.audioID;
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
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
        parcel.writeInt(infoTextID);
        parcel.writeInt(imageID);
        parcel.writeInt(audioID);
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
        latitude = in.readFloat();
        longitude = in.readFloat();
        infoTextID = in.readInt();
        imageID = in.readInt();
        audioID = in.readInt();
    }
}
