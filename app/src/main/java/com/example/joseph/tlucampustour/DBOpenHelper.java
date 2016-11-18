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
        db.execSQL(RESOURCES_TABLE_CREATE);
        db.execSQL(IMAGES_TABLE_CREATE);

        // Populate all db tables
        populateTourStopTable(db);
        populateBuildingInfoTable(db);
        populateTxtAndAudioResources(db);
        populateImages(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_STOPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUILDING_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESOURCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        onCreate(db);
    }

    // Add all TourStops to the tourStop db table
    private void populateTourStopTable(SQLiteDatabase db)
    {
        ContentValues tourStopValues = new ContentValues();
        int buildingID = 1;
        for (int i = 0; i < NUM_TOUR_STOPS; i++)
        {
            tourStopValues.put(COLUMN_TOUR_STOP_ID, i + 1);
            tourStopValues.put(COLUMN_NAME, TOUR_STOP_NAMES[i]);
            tourStopValues.put(COLUMN_CENTER_LATITUDE, TOUR_STOP_CENTER_LATITUDES[i]);
            tourStopValues.put(COLUMN_CENTER_LONGITUDE, TOUR_STOP_CENTER_LONGITUDES[i]);
            tourStopValues.put(COLUMN_RADIUS, TOUR_STOP_RADII[i]);
            tourStopValues.put(COLUMN_IS_BUILDING, TOUR_STOP_IS_BUILDING[i]);

            // put building ID that corresponds to correct index of building arrays
            if (TOUR_STOP_IS_BUILDING[i] == 1)
            {
                tourStopValues.put(COLUMN_BUILDING_ID, buildingID);
                buildingID++;
            }
            else
            {
                tourStopValues.put(COLUMN_BUILDING_ID, 0);
            }
            db.insert(TABLE_TOUR_STOPS, null, tourStopValues);
        }
    }

    // Add all buildings to the buildingInfo db table
    private void populateBuildingInfoTable(SQLiteDatabase db)
    {
        ContentValues buildingValues = new ContentValues();
        for (int i = 0; i < NUM_BUILDINGS; i++)
        {
            buildingValues.put(COLUMN_BUILDING_ID, i + 1);
            buildingValues.put(COLUMN_NAME, BUILDING_NAMES[i]);
            buildingValues.put(COLUMN_ENTRY_LATITUDE, BUILDING_ENTRY_LATITUDES[i]);
            buildingValues.put(COLUMN_ENTRY_LONGITUDE, BUILDING_ENTRY_LONGITUDES[i]);
            buildingValues.put(COLUMN_HANDICAP_LATITUDE, BUILDING_HANDICAP_LATITUDES[i]);
            buildingValues.put(COLUMN_HANDICAP_LONGITUDE, BUILDING_HANDICAP_LONGITUDES[i]);
            db.insert(TABLE_BUILDING_INFO, null, buildingValues);
        }
    }

    // Add all txt and audio resources to db table
    private void populateTxtAndAudioResources(SQLiteDatabase db)
    {
        ContentValues resourcesValues = new ContentValues();
        for (int i = 0; i < NUM_TOUR_STOPS; i++)
        {
            resourcesValues.put(COLUMN_TOUR_STOP_ID, i + 1);
            resourcesValues.put(COLUMN_NAME, TOUR_STOP_NAMES[i]);
            resourcesValues.put(COLUMN_INFO_TEXT, TOUR_STOP_TXT_IDS[i]);
            resourcesValues.put(COLUMN_AUDIO_FILE, TOUR_STOP_AUDIO_IDS[i]);
            db.insert(TABLE_RESOURCES, null, resourcesValues);
        }
    }

    // Add all images for each tour stop to db table
    private void populateImages(SQLiteDatabase db)
    {
        ContentValues imagesVales = new ContentValues();
        for (int i = 0; i < TOUR_STOP_IMAGE_IDS.length; i++)
        {
            imagesVales.put(COLUMN_TOUR_STOP_ID, i + 1);
            for (int j = 0; j < TOUR_STOP_IMAGE_IDS[i].length; j++)
            {
                imagesVales.put(COLUMN_IMAGE, TOUR_STOP_IMAGE_IDS[i][j]);
                db.insert(TABLE_IMAGES, null, imagesVales);
            }
        }
    }
}
