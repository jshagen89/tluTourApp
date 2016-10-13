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
        double Clat;
        double Clon;
        double Elat;
        double Elon;
        double Hlat;
        double Hlon;
        double radius;
        int infoID;
        int imgID;
        int audioID;
        int isBuild;
        Cursor myCursor = db.query(TABLE_TOUR_STOPS, TOUR_STOP_COLUMNS, null, null, null, null, null);
        if (myCursor.getCount() > 0)
        {
            while (myCursor.moveToNext())
            {
                name = myCursor.getString(NAME_COL_POSITION);
                Clat = myCursor.getDouble(CENTER_LAT_COL_POSITION);
                Clon = myCursor.getDouble(CENTER_LONG_COL_POSITION);
                Elat = Clat;
                Elon = Clon;
                Hlat = Clat;
                Hlon = Clon;
                radius = myCursor.getDouble(RADIUS_COL_POSITION);
                infoID = myCursor.getInt(INFO_COL_POSITION);
                imgID = myCursor.getInt(IMG_COL_POSITION);
                audioID = myCursor.getInt(AUDIO_COL_POSITION);
                isBuild = myCursor.getInt(IS_BUILDING_COL_POSITION);

                // get building info for all tour stops that are buildings
                if (isBuild == 1)
                {
                    String whereClause = COLUMN_NAME + " = ?";
                    String[] values = {name};
                    Cursor buildingCursor = db.query(TABLE_BUILDING_INFO, BUILDING_INFO_COLUMNS, whereClause, values, null, null, null);
                    if (buildingCursor.getCount() > 0)
                    {
                        while (buildingCursor.moveToNext())
                        {
                            Elat = buildingCursor.getDouble(ENTRY_LAT_COL_POSITION);
                            Elon = buildingCursor.getDouble(ENTRY_LON_COL_POSITION);
                            Hlat = buildingCursor.getDouble(HANDICAP_LAT_COL_POSITION);
                            Hlon = buildingCursor.getDouble(HANDICAP_LON_COL_POSITION);
                        }
                    }
                }

                TourStop newStop = new TourStop(name,Clat,Clon,Elat,Elon,Hlat,Hlon,radius,infoID,imgID,audioID, isBuild);
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
