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
    static final String TABLE_BUILDING_INFO = "buildingInfo";
    static final int DATABASE_VERSION = 1;
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_CENTER_LATITUDE = "centerLatitude";
    static final String COLUMN_CENTER_LONGITUDE = "centerLongitude";
    static final String COLUMN_RADIUS = "radius";
    static final String COLUMN_INFO_TEXT = "infotextID";
    static final String COLUMN_IMAGE = "imageID";
    static final String COLUMN_AUDIO_FILE = "audiofileID";
    static final String COLUMN_IS_BUILDING = "isBuilding";
    static final String COLUMN_ENTRY_LATITUDE = "entryLatitude";
    static final String COLUMN_ENTRY_LONGITUDE = "entryLongitude";
    static final String COLUMN_HANDICAP_LATITUDE = "handicapLatitude";
    static final String COLUMN_HANDICAP_LONGITUDE = "handicapLongitude";
    static final String[] TOUR_STOP_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_CENTER_LATITUDE, COLUMN_CENTER_LONGITUDE,
            COLUMN_RADIUS, COLUMN_INFO_TEXT, COLUMN_IMAGE, COLUMN_AUDIO_FILE, COLUMN_IS_BUILDING};
    static final String[] BUILDING_INFO_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_ENTRY_LATITUDE, COLUMN_ENTRY_LONGITUDE,
            COLUMN_HANDICAP_LATITUDE, COLUMN_HANDICAP_LONGITUDE};
    static final String TOUR_STOP_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TOUR_STOPS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_CENTER_LATITUDE + " REAL, " +
                    COLUMN_CENTER_LONGITUDE + " REAL, " +
                    COLUMN_RADIUS + " REAL, " +
                    COLUMN_INFO_TEXT + " INTEGER, " +
                    COLUMN_IMAGE + " INTEGER, " +
                    COLUMN_AUDIO_FILE + " INTEGER, " +
                    COLUMN_IS_BUILDING + " INTEGER" + ")";
    static final String BUILDING_INFO_TABLE_CREATE =
            "CREATE TABLE " + TABLE_BUILDING_INFO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_ENTRY_LATITUDE + " REAL, " +
                    COLUMN_ENTRY_LONGITUDE + " REAL, " +
                    COLUMN_HANDICAP_LATITUDE + " REAL, " +
                    COLUMN_HANDICAP_LONGITUDE + " REAL" + ")";
    static final int ID_COL_POSITION = 0;
    static final int NAME_COL_POSITION = 1;
    static final int CENTER_LAT_COL_POSITION = 2;
    static final int CENTER_LONG_COL_POSITION = 3;
    static final int RADIUS_COL_POSITION = 4;
    static final int INFO_COL_POSITION = 5;
    static final int IMG_COL_POSITION = 6;
    static final int AUDIO_COL_POSITION = 7;
    static final int IS_BUILDING_COL_POSITION = 8;
    static final int ENTRY_LAT_COL_POSITION = 2;
    static final int ENTRY_LON_COL_POSITION = 3;
    static final int HANDICAP_LAT_COL_POSITION = 4;
    static final int HANDICAP_LON_COL_POSITION = 5;
    static final int NUM_TOUR_STOPS = 20;
    static final int NUM_BUILDINGS = 15;

    // Content Provider Constants
    static final String AUTHORITY = "com.example.joseph.tlucampustour.tourcontentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TOUR_STOPS);
    static final int TABLE_REF = 1;
    static final int ID_REF = 2;

    // Passing Data Constants
    static final String SELECTED_STOP_EXTRA = "Selected Stop";
    static final String TOUR_STOP_ARRAY_EXTRA = "Tour Stop Array";
    static final String MAP_OPTIONS_RESULT = "Map View";
    static final int SATELLITE_MAP_CHOICE = 1;
    static final int NORMAL_MAP_CHOICE = 2;
    static final String LANGUAGE_PREF_RESULT = "Language Pref";
    static final String ACCESS_PREF_RESULT = "Accessibility Pref";
    static final int ENGLISH_CHOICE = 1;
    static final int SPANISH_CHOICE = 2;
    static final int MANDARIN_CHOICE = 3;

    // Google Location Service Constants
    static final int EDITOR_REQUEST_CODE = 1001;
    static final String DIRECTIONS_API_KEY = "AIzaSyDwu1KeZqcNGUihZcCYQS9uywE4XNaHFwQ";
    static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    static final double TLU_CAMPUS_LAT = 29.572682;
    static final double TLU_CAMPUS_LON = -97.985592;
    static final LatLng TLUPoint = new LatLng(TLU_CAMPUS_LAT, TLU_CAMPUS_LON);
    static final int TLU_CAMPUS_RADIUS = 600;
    static final float DEFAULT_CAMERA_ZOOM = 15.0f;
    static final int UPDATE_LOCATION_DISTANCE = 20;
    static final int MAP_OPTIONS_REQUEST_CODE = 0;
}
