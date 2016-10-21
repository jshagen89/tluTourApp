package com.example.joseph.tlucampustour;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 9/14/2016.
 */
class DBOpenHelper extends SQLiteOpenHelper
{

    DBOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create new db if none exists
        db.execSQL(TOUR_STOP_TABLE_CREATE);
        db.execSQL(BUILDING_INFO_TABLE_CREATE);
        db.execSQL(TXT_AUDIO_RES_TABLE_CREATE);

        // Populate all db tables
        populateTourStopTable(db);
        populateBuildingInfoTable(db);
        populateTxtAndAudioResources(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_STOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEXT_AUDIO_RESOURCES);
        onCreate(db);
    }

    // Add all TourStops to the tourStop db table
    private void populateTourStopTable(SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        for (int i = 0; i < NUM_TOUR_STOPS; i++)
        {
            values.put(COLUMN_NAME, TOUR_STOP_NAMES[i]);
            values.put(COLUMN_CENTER_LATITUDE, TOUR_STOP_CENTER_LATITUDES[i]);
            values.put(COLUMN_CENTER_LONGITUDE, TOUR_STOP_CENTER_LONGITUDES[i]);
            values.put(COLUMN_RADIUS, TOUR_STOP_RADII[i]);
            values.put(COLUMN_IMAGE, TOUR_STOP_IMAGE_IDS[i]);
            values.put(COLUMN_IS_BUILDING, TOUR_STOP_IS_BUILDING[i]);
            db.insert(TABLE_TOUR_STOPS, null, values);
        }
    }

    // Add all buildings to the buildingInfo db table
    private void populateBuildingInfoTable(SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        for (int i = 0; i < NUM_BUILDINGS; i++)
        {
            values.put(COLUMN_NAME, BUILDING_NAMES[i]);
            values.put(COLUMN_ENTRY_LATITUDE, BUILDING_ENTRY_LATITUDES[i]);
            values.put(COLUMN_ENTRY_LONGITUDE, BUILDING_ENTRY_LONGITUDES[i]);
            values.put(COLUMN_HANDICAP_LATITUDE, BUILDING_HANDICAP_LATITUDES[i]);
            values.put(COLUMN_HANDICAP_LONGITUDE, BUILDING_HANDICAP_LONGITUDES[i]);
            db.insert(TABLE_BUILDING_INFO, null, values);
        }
    }

    // Add all txt and audio resources to db table
    private void populateTxtAndAudioResources(SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        for (int i = 0; i < NUM_TOUR_STOPS; i++)
        {
            values.put(COLUMN_NAME, TOUR_STOP_NAMES[i]);
            values.put(COLUMN_INFO_TEXT, TOUR_STOP_TXT_IDS[i]);
            values.put(COLUMN_AUDIO_FILE, TOUR_STOP_AUDIO_IDS[i]);
            db.insert(TABLE_TEXT_AUDIO_RESOURCES, null, values);
        }
    }
}
