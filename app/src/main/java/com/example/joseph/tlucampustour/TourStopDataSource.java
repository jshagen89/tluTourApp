package com.example.joseph.tlucampustour;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 9/30/2016.
 */

public class TourStopDataSource {

    SQLiteOpenHelper dbOpenHelper;
    SQLiteDatabase db;

    public TourStopDataSource(Context context)
    {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public void open()
    {
        db = dbOpenHelper.getWritableDatabase();
    }

    public ArrayList<TourStop> getAllTourStops()
    {
        ArrayList<TourStop> allStops = new ArrayList<>(NUM_TOUR_STOPS);
        String name;
        double lat;
        double lon;
        double radius;
        int infoID;
        int imgID;
        int audioID;
        Cursor myCursor = db.query(TABLE_TOUR_STOPS, TOUR_STOP_COLUMNS, null, null, null, null, null);
        if (myCursor.getCount() > 0)
        {
            while (myCursor.moveToNext())
            {
                name = myCursor.getString(NAME_COL_POSITION);
                lat = myCursor.getDouble(LAT_COL_POSITION);
                lon = myCursor.getDouble(LONG_COL_POSITION);
                radius = myCursor.getDouble(RADIUS_COL_POSITION);
                infoID = myCursor.getInt(INFO_COL_POSITION);
                imgID = myCursor.getInt(IMG_COL_POSITION);
                audioID = myCursor.getInt(AUDIO_COL_POSITION);
                TourStop newStop = new TourStop(name,lat,lon,radius,infoID,imgID,audioID);
                allStops.add(newStop);
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
