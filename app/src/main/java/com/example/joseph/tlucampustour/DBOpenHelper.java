package com.example.joseph.tlucampustour;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        db.execSQL(TABLE_CREATE);

        //Add all tour stops to new db
        TourStop[] myTourStops = new TourStop[NUM_TOUR_STOPS];
        myTourStops[0] = new TourStop("Martin Luther Statue", 29.57089, -97.98201, 20, R.string.martin_luther_statue_info, R.drawable.martin_luther_statue, R.raw.martin_luther_statue);
        myTourStops[1] = new TourStop("AT&T & Moody Science Complex", 29.57098, -97.98293, 45, R.string.att_science_info, R.drawable.att_science, R.raw.att_science);
        myTourStops[2] = new TourStop("Emma Frey", 29.57148, -97.98263, 30, R.string.emma_frey_info, R.drawable.emma_frey, R.raw.emma_frey);
        myTourStops[3] = new TourStop("Tschoepe Hall", 29.57206, -97.98265, 40, R.string.tschoepe_info, R.drawable.tschoepe, R.raw.tschope);
        myTourStops[4] = new TourStop("Baldus, Clifton, Trinity Housing", 29.57184, -97.98363, 55, R.string.baldus_clifton_trinity_info, R.drawable.baldus_clifton_trinity_housing, R.raw.baldus_clifton_trinity);
        myTourStops[5] = new TourStop("Krost and Health Sciences", 29.57096, -97.98347, 30, R.string.krost_info, R.drawable.krost_health_sciences, R.raw.krost);
        myTourStops[6] = new TourStop("Weston Ranch", 29, -97, 50, R.string.weston_ranch_info, R.drawable.weston_ranch, R.raw.weston_ranch);
        myTourStops[7] = new TourStop("Chapel of the Abiding Presence", 29.57262, -97.98376, 30, R.string.chapel_info, R.drawable.chapel, R.raw.chapel);
        myTourStops[8] = new TourStop("Campus Ministry/Servant Leadership", 29.57322, -97.98481, 40, R.string.campus_ministry_info, R.drawable.campus_ministry, R.raw.campus_ministry);
        myTourStops[9] = new TourStop("Hein Dining Hall", 29.57208, -97.98512, 45, R.string.hein_info, R.drawable.hein_dining_hall, R.raw.hein_dining_hall);
        myTourStops[10] = new TourStop("Centennial Hall Courtyard", 29.57218, -97.98605, 50, R.string.centennial_info, R.drawable.centennial_hall_courtyard, R.raw.centennial_hall);
        myTourStops[11] = new TourStop("Jackson Auditorium", 29.57173, -97.98715, 50, R.string.jackson_info, R.drawable.jackson_auditorium, R.raw.jackson_auditorium);
        myTourStops[12] = new TourStop("Sports Complex/Athletic Training", 29.5764, -97.98376, 60, R.string.sports_complex_info, R.drawable.sports_complex_athletic_training, R.raw.sports_complex);
        myTourStops[13] = new TourStop("Fitness Center", 29.57464, -97.98339, 65, R.string.fitness_center_info, R.drawable.fitness_center, R.raw.fitness_center);
        myTourStops[14] = new TourStop("Graduation Walk", 29.57395, -97.98297, 35, R.string.graduation_walk_info, R.drawable.graduation_walk, R.raw.graduation_walk);
        myTourStops[15] = new TourStop("Alumni Student Center", 29.57331, -97.9836, 40, R.string.asc_info, R.drawable.asc, R.raw.asc);
        myTourStops[16] = new TourStop("Blumburg Memorial Library", 29.57323, -97.98262, 40, R.string.library_info, R.drawable.blumberg_library, R.raw.blumburg_library);
        myTourStops[17] = new TourStop("Schuech Fine Arts", 29.57332, -97.98161, 50, R.string.scheuch_info, R.drawable.schuech_fine_arts, R.raw.schuech_fine_arts);
        myTourStops[18] = new TourStop("Langner Hall", 29.57239, -97.98145, 30, R.string.langner_info, R.drawable.langner_hall, R.raw.langner_hall);
        myTourStops[19] = new TourStop("Alumni Plaza", 29.57269, -97.98203, 20, R.string.alumni_plaza_info, R.drawable.alumni_plaza, R.raw.alumni_plaza);
        for (TourStop myTourStop : myTourStops) addTourStop(db, myTourStop);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR_STOPS);
        onCreate(db);
    }

    // Add a new TourStop to the db
    private void addTourStop(SQLiteDatabase db, TourStop newStop)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newStop.getName());
        values.put(COLUMN_LATITUDE, newStop.getLatitude());
        values.put(COLUMN_LONGITUDE, newStop.getLongitude());
        values.put(COLUMN_RADIUS, newStop.getRadius());
        values.put(COLUMN_INFO_TEXT, newStop.getInfoTextID());
        values.put(COLUMN_IMAGE, newStop.getImage());
        values.put(COLUMN_AUDIO_FILE, newStop.getAudioFile());
        db.insert(TABLE_TOUR_STOPS, null, values);
    }
}
