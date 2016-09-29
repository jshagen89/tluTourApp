package com.example.joseph.tlucampustour;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Joseph on 9/8/2016.
 */
public class TourStop implements Parcelable{

    private String name;
    private double latitude;
    private double longitude;
    private double radius;
    private int infoTextID;
    private int imageID;
    private int audioID;

    public TourStop(String name, double lat, double lon, double rad, int infoText, int img, int audio)

    {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.radius = rad;
        this.infoTextID = infoText;
        this.imageID = img;
        this.audioID = audio;
    }

    public String getName()

    {
        return this.name;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public double getRadius()
    {
        return this.radius;
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
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(radius);
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
        latitude = in.readDouble();
        longitude = in.readDouble();
        radius = in.readDouble();
        infoTextID = in.readInt();
        imageID = in.readInt();
        audioID = in.readInt();
    }
}
