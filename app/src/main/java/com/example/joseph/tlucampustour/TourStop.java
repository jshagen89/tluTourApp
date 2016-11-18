package com.example.joseph.tlucampustour;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joseph on 9/8/2016.
 */
public class TourStop implements Parcelable{

    private String name;
    private double centerLatitude;
    private double centerLongitude;
    private double entryLatitude;
    private double entryLongitude;
    private double radius;
    private int infoTextID;
    private int[] imageIDs;
    private int audioID;
    private boolean isBuilding;
    private boolean beenPlayed;

    public TourStop(String name, double Clat, double Clon, double Elat, double Elon, double rad, int infoText, int[] imgs, int audio, int isBuild)

    {
        this.name = name;
        this.centerLatitude = Clat;
        this.centerLongitude = Clon;
        this.entryLatitude = Elat;
        this.entryLongitude = Elon;
        this.radius = rad;
        this.infoTextID = infoText;
        this.imageIDs = imgs;
        this.audioID = audio;
        if (isBuild == 1)
            isBuilding = true;
        else if (isBuild == 0)
            isBuilding = false;
        this.beenPlayed = false;
    }

    public String getName()

    {
        return this.name;
    }

    public double getCenterLatitude()
    {
        return this.centerLatitude;
    }

    public double getCenterLongitude()
    {
        return this.centerLongitude;
    }

    public double getEntryLatitude()
    {
        return this.entryLatitude;
    }

    public double getEntryLongitude()
    {
        return this.entryLongitude;
    }

    public double getRadius()
    {
        return this.radius;
    }

    public int getInfoTextID() {
        return this.infoTextID;
    }

    public int[] getImages()
    {
        return this.imageIDs;
    }

    public int getAudioFile()
    {
        return this.audioID;
    }

    public boolean isBuilding()
    {
        return this.isBuilding;
    }

    public boolean hasBeenPlayed()
    {
        return this.beenPlayed;
    }

    public void setPlayed(boolean value)
    {
        this.beenPlayed = value;
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
        parcel.writeDouble(centerLatitude);
        parcel.writeDouble(centerLongitude);
        parcel.writeDouble(entryLatitude);
        parcel.writeDouble(entryLongitude);
        parcel.writeDouble(radius);
        parcel.writeInt(infoTextID);
        parcel.writeIntArray(imageIDs);
        parcel.writeInt(audioID);
        parcel.writeByte((byte) (isBuilding ? 1 : 0));
        parcel.writeByte((byte) (beenPlayed ? 1 : 0));
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
        centerLatitude = in.readDouble();
        centerLongitude = in.readDouble();
        entryLatitude = in.readDouble();
        entryLongitude = in.readDouble();
        radius = in.readDouble();
        infoTextID = in.readInt();
        imageIDs = in.createIntArray();
        audioID = in.readInt();
        isBuilding = in.readByte() != 0;
        beenPlayed = in.readByte() != 0;
    }
}
