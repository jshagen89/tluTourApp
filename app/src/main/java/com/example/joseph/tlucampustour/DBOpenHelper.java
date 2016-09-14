package com.example.joseph.tlucampustour;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joseph on 9/14/2016.
 */
public class DBOpenHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "tlucampustour.db";
    public static final String TABLE_TOUR_STOPS = "tourstops";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_AUDIO_FILE = "audiofile";
    public static final String COLUMN_IMAGE = "image";

    public DBOpenHelper(Context context, String name, CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOUR_STOPS_TABLE = "CREATE TABLE " + TABLE_TOUR_STOPS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT," + COLUMN_LATITUDE + " INTEGER, "
                + COLUMN_LONGITUDE + " INTEGER, " + COLUMN_IMAGE + " TEXT, " + COLUMN_AUDIO_FILE + " TEXT" + ")";
        db.execSQL(CREATE_TOUR_STOPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_STOPS);
        onCreate(db);
    }

    public void addTourStop(TourStop newStop)
    {
        ContentValues values = new ContentValues();
        // put values into content values object
        // insert new tour stop into db
        // close db
    }
}
