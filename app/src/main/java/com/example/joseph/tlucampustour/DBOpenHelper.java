package com.example.joseph.tlucampustour;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_AUDIO_FILE = "audiofile";
    public static final String[] TOUR_STOP_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
                                                        COLUMN_IMAGE, COLUMN_AUDIO_FILE};
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TOUR_STOPS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_AUDIO_FILE + " TEXT" + ")";
    public static final int ID_COL_POSITION = 0;
    public static final int NAME_COL_POSITION = 1;
    public static final int LAT_COL_POSITION = 2;
    public static final int LONG_COL_POSITION = 3;
    public static final int IMG_COL_POSITION = 4;
    public static final int AUDIO_COL_POSITION = 5;
    public static final int NUM_TOUR_STOPS = 20;

    private ContentResolver myResolver;

    public DBOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create new db if none exists
        db.execSQL(TABLE_CREATE);

        //Add all tour stops to new db
        TourStop[] myTourStops = new TourStop[NUM_TOUR_STOPS];
        myTourStops[0] = new TourStop("Martin Luther Statue", 0, 0, "tschope", "tschope");
        myTourStops[1] = new TourStop("AT&T & Moody Science Complex", 0, 0, "att_science", "att_science");
        myTourStops[2] = new TourStop("Emma Frey", 0, 0, "tschope", "tschope");
        myTourStops[3] = new TourStop("Tschope Hall", 0, 0, "tschope", "tschope");
        myTourStops[4] = new TourStop("Baldus, Clifton, Trinity Housing", 0, 0, "tschope", "tschope");
        myTourStops[5] = new TourStop("Krost and Health Sciences", 0, 0, "tschope", "tschope");
        myTourStops[6] = new TourStop("Weston Ranch", 0, 0, "tschope", "tschope");
        myTourStops[7] = new TourStop("Chapel of the Abiding Presence", 0, 0, "tschope", "tschope");
        myTourStops[8] = new TourStop("Campus Ministry/Servant Leadership", 0, 0, "tschope", "tschope");
        myTourStops[9] = new TourStop("Hein Dining Hall", 0, 0, "tschope", "tschope");
        myTourStops[10] = new TourStop("Centennial Hall Courtyard", 0, 0, "tschope", "tschope");
        myTourStops[11] = new TourStop("Jackson Auditorium", 0, 0, "tschope", "tschope");
        myTourStops[12] = new TourStop("Sports Complex/Athletic Training", 0, 0, "tschope", "tschope");
        myTourStops[13] = new TourStop("Fitness Center", 0, 0, "tschope", "tschope");
        myTourStops[14] = new TourStop("Graduation Walk", 0, 0, "tschope", "tschope");
        myTourStops[15] = new TourStop("Alumni Student Center", 0, 0, "tschope", "tschope");
        myTourStops[16] = new TourStop("Blumburg Memorial Library", 0, 0, "tschope", "tschope");
        myTourStops[17] = new TourStop("Schuech Fine Arts", 0, 0, "tschope", "tschope");
        myTourStops[18] = new TourStop("Langner Hall", 0, 0, "tschope", "tschope");
        myTourStops[19] = new TourStop("Alumni Plaza", 0, 0, "tschope", "tschope");
        for (TourStop myTourStop : myTourStops) addTourStop(db, myTourStop);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_STOPS);
        onCreate(db);
    }

    // Add a new TourStop to the db
    public void addTourStop(SQLiteDatabase db, TourStop newStop)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newStop.getName());
        values.put(COLUMN_LATITUDE, newStop.getLatitude());
        values.put(COLUMN_LONGITUDE, newStop.getLongitude());
        values.put(COLUMN_IMAGE, newStop.getImage());
        values.put(COLUMN_AUDIO_FILE, newStop.getAudioFile());
        db.insert(TABLE_TOUR_STOPS, null, values);
    }
}
