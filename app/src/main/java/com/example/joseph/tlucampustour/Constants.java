package com.example.joseph.tlucampustour;

import android.net.Uri;

/**
 * Created by Joseph on 9/28/2016.
 */

abstract class Constants {

    // Database Constants
    public static final String DATABASE_NAME = "tlucampustour.db";
    public static final String TABLE_TOUR_STOPS = "tourstops";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_RADIUS = "radius";
    public static final String COLUMN_INFO_TEXT = "infotextID";
    public static final String COLUMN_IMAGE = "imageID";
    public static final String COLUMN_AUDIO_FILE = "audiofileID";
    public static final String[] TOUR_STOP_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
            COLUMN_RADIUS, COLUMN_INFO_TEXT, COLUMN_IMAGE, COLUMN_AUDIO_FILE};
    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TOUR_STOPS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_RADIUS + " REAL, " +
                    COLUMN_INFO_TEXT + " INTEGER, " +
                    COLUMN_IMAGE + " INTEGER, " +
                    COLUMN_AUDIO_FILE + " INTEGER" + ")";
    public static final int ID_COL_POSITION = 0;
    public static final int NAME_COL_POSITION = 1;
    public static final int LAT_COL_POSITION = 2;
    public static final int LONG_COL_POSITION = 3;
    public static final int RADIUS_COL_POSITION = 4;
    public static final int INFO_COL_POSITION = 5;
    public static final int IMG_COL_POSITION = 6;
    public static final int AUDIO_COL_POSITION = 7;
    public static final int NUM_TOUR_STOPS = 20;

    // Content Provider Constants
    public static final String AUTHORITY = "com.example.joseph.tlucampustour.tourcontentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TOUR_STOPS);
    public static final int TABLE_REF = 1;
    public static final int ID_REF = 2;

    // Google Location Service Constants
    public static final int EDITOR_REQUEST_CODE = 1001;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
}
