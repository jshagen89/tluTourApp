package com.example.joseph.tlucampustour;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 9/30/2016.
 */

public class TourStopDataSource {

    private SQLiteOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public TourStopDataSource(Context context)
    {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public void open()
    {
        db = dbOpenHelper.getWritableDatabase();
    }

    public ArrayList<TourStop> getAllTourStops(Context context)
    {
        // Determine which type of entries the user prefers to use
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean useHandicapEntries = prefs.getBoolean(ACCESS_PREF_RESULT, false);
        int entryLatColumnPos = ENTRY_LAT_COL_POSITION;
        int entryLonColumnPos = ENTRY_LON_COL_POSITION;
        if (useHandicapEntries)
        {
            entryLatColumnPos = HANDICAP_LAT_COL_POSITION;
            entryLonColumnPos = HANDICAP_LON_COL_POSITION;
        }

        ArrayList<TourStop> allStops = new ArrayList<>(NUM_TOUR_STOPS);
        int tourStopID;
        int nameTextID;
        String name;
        double Clat;
        double Clon;
        double Elat;
        double Elon;
        double radius;
        int infoID;
        ArrayList<Integer> imgIDs = new ArrayList<>(MAX_NUM_IMAGES);
        int audioID;
        int isBuild;
        int buildingID;
        Cursor myCursor = db.query(TABLE_TOUR_STOPS, TOUR_STOP_COLUMNS, null, null, null, null, null);
        if (myCursor.getCount() > 0)
        {
            while (myCursor.moveToNext())
            {
                tourStopID = myCursor.getInt(TOUR_STOP_ID_COL_POSITION);
                nameTextID = myCursor.getInt(NAME_COL_POSITION);
                name = context.getResources().getString(nameTextID);
                Clat = myCursor.getDouble(CENTER_LAT_COL_POSITION);
                Clon = myCursor.getDouble(CENTER_LONG_COL_POSITION);
                Elat = Clat;
                Elon = Clon;
                radius = myCursor.getDouble(RADIUS_COL_POSITION);
                isBuild = myCursor.getInt(IS_BUILDING_COL_POSITION);

                // have to initialize these to 0 for compiler
                infoID = 0;
                audioID = 0;

                // get building info for all tour stops that are buildings
                if (isBuild == 1)
                {
                    buildingID = myCursor.getInt(TOUR_STOP_BUILDING_ID_COL_POSITION);
                    String buildingWhereClause = COLUMN_BUILDING_ID + " = ? LIMIT 1";
                    String[] buildingValues = {Integer.toString(buildingID)};
                    Cursor buildingCursor = db.query(TABLE_BUILDING_INFO, BUILDING_INFO_COLUMNS, buildingWhereClause, buildingValues, null, null, null);
                    if (buildingCursor.getCount() > 0)
                    {
                        buildingCursor.moveToFirst();
                        Elat = buildingCursor.getDouble(entryLatColumnPos);
                        Elon = buildingCursor.getDouble(entryLonColumnPos);
                    }
                    buildingCursor.close();
                }

                // Get info txt and audio resource files for each tour stop
                String resWhereClause = COLUMN_TOUR_STOP_ID + " = ? LIMIT 1";
                String[] resValues = {Integer.toString(tourStopID)};
                Cursor resourceCursor = db.query(TABLE_RESOURCES, RESOURCES_COLUMNS, resWhereClause, resValues, null, null, null);
                if (resourceCursor.getCount() > 0)
                {
                    resourceCursor.moveToFirst();
                    infoID = resourceCursor.getInt(TXT_COL_POSITION);
                    audioID = resourceCursor.getInt(AUDIO_COL_POSITION);
                }
                resourceCursor.close();

                String imgWhereClause = COLUMN_TOUR_STOP_ID + " = ?";
                String[] imgValues = {Integer.toString(tourStopID)};
                Cursor imagesCursor = db.query(TABLE_IMAGES, IMAGES_COLUMNS, imgWhereClause, imgValues, null, null, null);
                if (imagesCursor.getCount() > 0)
                {
                    for (; imagesCursor.moveToNext(); )
                    {
                        imgIDs.add(imagesCursor.getInt(IMG_COL_POSITION));
                    }
                }
                imgIDs.trimToSize();
                int[] imgArr = new int[imgIDs.size()];
                for (int i = 0; i < imgArr.length; i++)
                    imgArr[i] = imgIDs.get(i);
                TourStop newStop = new TourStop(name,Clat,Clon,Elat,Elon,radius,infoID,imgArr,audioID, isBuild);
                allStops.add(newStop);
                imgIDs.clear();
            }
            myCursor.close();
        }
        return allStops;
    }

    public void close()
    {
        dbOpenHelper.close();
    }
}
