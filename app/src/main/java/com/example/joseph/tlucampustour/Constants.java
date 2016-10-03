package com.example.joseph.tlucampustour;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Joseph on 9/28/2016.
 */

abstract class Constants {

    // Database Constants
    static final String DATABASE_NAME = "tlucampustour.db";
    static final String TABLE_TOUR_STOPS = "tourstops";
    static final int DATABASE_VERSION = 1;
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_LATITUDE = "latitude";
    static final String COLUMN_LONGITUDE = "longitude";
    static final String COLUMN_RADIUS = "radius";
    static final String COLUMN_INFO_TEXT = "infotextID";
    static final String COLUMN_IMAGE = "imageID";
    static final String COLUMN_AUDIO_FILE = "audiofileID";
    static final String[] TOUR_STOP_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
            COLUMN_RADIUS, COLUMN_INFO_TEXT, COLUMN_IMAGE, COLUMN_AUDIO_FILE};
    static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TOUR_STOPS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_RADIUS + " REAL, " +
                    COLUMN_INFO_TEXT + " INTEGER, " +
                    COLUMN_IMAGE + " INTEGER, " +
                    COLUMN_AUDIO_FILE + " INTEGER" + ")";
    static final int ID_COL_POSITION = 0;
    static final int NAME_COL_POSITION = 1;
    static final int LAT_COL_POSITION = 2;
    static final int LONG_COL_POSITION = 3;
    static final int RADIUS_COL_POSITION = 4;
    static final int INFO_COL_POSITION = 5;
    static final int IMG_COL_POSITION = 6;
    static final int AUDIO_COL_POSITION = 7;
    static final int NUM_TOUR_STOPS = 20;

    // Content Provider Constants
    static final String AUTHORITY = "com.example.joseph.tlucampustour.tourcontentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TOUR_STOPS);
    static final int TABLE_REF = 1;
    static final int ID_REF = 2;

    // Google Location Service Constants
    static final int EDITOR_REQUEST_CODE = 1001;
    static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    static final LatLng TLUPoint = new LatLng(29.572682, -97.985592);
    static final float DEFAULT_CAMERA_ZOOM = 15.0f;
    static final int UPDATE_LOCATION_DISTANCE = 10;
}
