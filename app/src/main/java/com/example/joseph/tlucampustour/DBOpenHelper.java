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
        addTxtAndAudioResources(db);

        /*
        TourStop martinLutherStatue = new TourStop("Martin Luther Statue", 29.57089, -97.98201, 29.57089, -97.98201, 29.57089, -97.98201, 20, R.string.martin_luther_statue_eng_info, R.drawable.martin_luther_statue, R.raw.martin_luther_statue_eng, 0);
        TourStop aTTScience = new TourStop("AT&T & Moody Science Complex", 29.57098, -97.98293, 29.571, -97.98255, 29.571, -97.98255, 45, R.string.att_science_eng_info, R.drawable.att_science, R.raw.att_science_eng, 1);
        TourStop emmaFrey = new TourStop("Emma Frey", 29.57148, -97.98263, 29.57147, -97.98255, 29.57167, -97.98264, 30, R.string.emma_frey_eng_info, R.drawable.emma_frey, R.raw.emma_frey_eng, 1);
        TourStop tschoepeHall = new TourStop("Tschoepe Hall", 29.57206, -97.98265, 29.57216, -97.98253, 29.57219, -97.98289, 40, R.string.tschoepe_eng_info, R.drawable.tschoepe, R.raw.tschope_eng, 1);
        TourStop BCTHousing = new TourStop("Baldus, Clifton, Trinity Housing", 29.57184, -97.98363, 29.57184, -97.98363, 29.57184, -97.98363, 55, R.string.baldus_clifton_trinity_eng_info, R.drawable.baldus_clifton_trinity_housing, R.raw.baldus_clifton_trinity_eng, 1);
        TourStop krostHS = new TourStop("Krost and Health Sciences", 29.57096, -97.98347, 29.57096, -97.98347, 29.57096, -97.98347, 30, R.string.krost_eng_info, R.drawable.krost_health_sciences, R.raw.krost_eng, 1);
        TourStop westonRanch = new TourStop("Weston Ranch", 29.5745, -97.98088, 29.5745, -97.98088, 29.5745, -97.98088, 30, R.string.weston_ranch_eng_info, R.drawable.weston_ranch, R.raw.weston_ranch_eng, 0);
        TourStop chapel = new TourStop("Chapel of the Abiding Presence", 29.57262, -97.98376, 29.57263, -97.98357, 29.57254, -97.98384, 30, R.string.chapel_eng_info, R.drawable.chapel, R.raw.chapel_eng, 1);
        TourStop campusMinistry = new TourStop("Campus Ministry/Servant Leadership", 29.57322, -97.98481, 29.57322, -97.98481, 29.57322, -97.98481, 40, R.string.campus_ministry_eng_info, R.drawable.campus_ministry, R.raw.campus_ministry_eng, 1);
        TourStop heinDining = new TourStop("Hein Dining Hall", 29.57208, -97.98512, 29.57197, -97.98512, 29.57202, -97.98481, 45, R.string.hein_eng_info, R.drawable.hein_dining_hall, R.raw.hein_dining_hall_eng, 1);
        TourStop centennialHall = new TourStop("Centennial Hall Courtyard", 29.57218, -97.98605, 29.57218, -97.98605, 29.57218, -97.98605, 50, R.string.centennial_eng_info, R.drawable.centennial_hall_courtyard, R.raw.centennial_hall_eng, 1);
        TourStop jacksonAuditorium = new TourStop("Jackson Auditorium", 29.57173, -97.98715, 29.57173, -97.98715, 29.57173, -97.98715, 50, R.string.jackson_eng_info, R.drawable.jackson_auditorium, R.raw.jackson_auditorium_eng, 1);
        TourStop sportsComplex = new TourStop("Sports Complex/Athletic Training", 29.5764, -97.98376, 29.5764, -97.98376, 29.5764, -97.98376, 60, R.string.sports_complex_eng_info, R.drawable.sports_complex_athletic_training, R.raw.sports_complex_eng, 0);
        TourStop fitnessCenter = new TourStop("Fitness Center", 29.57464, -97.98339, 29.57464, -97.98339, 29.57464, -97.98339, 65, R.string.fitness_center_eng_info, R.drawable.fitness_center, R.raw.fitness_center_eng, 1);
        TourStop gradWalk = new TourStop("Graduation Walk", 29.57395, -97.98297, 29.57395, -97.98297, 29.57395, -97.98297, 35, R.string.graduation_walk_eng_info, R.drawable.graduation_walk, R.raw.graduation_walk_eng, 0);
        TourStop asc = new TourStop("Alumni Student Center", 29.57331, -97.9836, 29.5731, -97.98367, 29.5731, -97.98367, 40, R.string.asc_eng_info, R.drawable.asc, R.raw.asc_eng, 1);
        TourStop library = new TourStop("Blumburg Memorial Library", 29.57323, -97.98262, 29.57308, -97.98262, 29.57338, -97.98264, 40, R.string.library_eng_info, R.drawable.blumberg_library, R.raw.blumburg_library_eng, 1);
        TourStop schuechFA = new TourStop("Schuech Fine Arts", 29.57332, -97.98161, 29.57317, -97.98179, 29.57317, -97.98179, 50, R.string.scheuch_eng_info, R.drawable.schuech_fine_arts, R.raw.schuech_fine_arts_eng, 1);
        TourStop langnerHall = new TourStop("Langner Hall", 29.57239, -97.98145, 29.57238, -97.98151, 29.57217, -97.98146, 30, R.string.langner_eng_info, R.drawable.langner_hall, R.raw.langner_hall_eng, 1);
        TourStop alumniPlaza = new TourStop("Alumni Plaza", 29.57269, -97.98203, 29.57269, -97.98203, 29.57269, -97.98203, 20, R.string.alumni_plaza_eng_info, R.drawable.alumni_plaza, R.raw.alumni_plaza_eng, 0);
        //Add all tour stops to new db
        TourStop[] myTourStops = new TourStop[NUM_TOUR_STOPS];
        myTourStops[0] = martinLutherStatue;
        myTourStops[1] = aTTScience;
        myTourStops[2] = emmaFrey;
        myTourStops[3] = tschoepeHall;
        myTourStops[4] = BCTHousing;
        myTourStops[5] = krostHS;
        myTourStops[6] = westonRanch;
        myTourStops[7] = chapel;
        myTourStops[8] = campusMinistry;
        myTourStops[9] = heinDining;
        myTourStops[10] = centennialHall;
        myTourStops[11] = jacksonAuditorium;
        myTourStops[12] = sportsComplex;
        myTourStops[13] = fitnessCenter;
        myTourStops[14] = gradWalk;
        myTourStops[15] = asc;
        myTourStops[16] = library;
        myTourStops[17] = schuechFA;
        myTourStops[18] = langnerHall;
        myTourStops[19] = alumniPlaza;
        for (TourStop myTourStop : myTourStops) addTourStop(db, myTourStop);
        */
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
    private void addTxtAndAudioResources(SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        for (int i = 0; i < NUM_TOUR_STOPS; i++)
        {
            values.put(COLUMN_NAME, TOUR_STOP_NAMES[i]);
            values.put(COLUMN_ENG_INFO_TEXT, TOUR_STOP_ENG_TXT_IDS[i]);
            //values.put(COLUMN_SPAN_INFO_TEXT, BUILDING_ENTRY_LONGITUDES[i]);
            //values.put(COLUMN_MAND_INFO_TEXT, BUILDING_HANDICAP_LATITUDES[i]);
            values.put(COLUMN_ENG_AUDIO_FILE, TOUR_STOP_ENG_AUDIO_IDS[i]);
            //values.put(COLUMN_SPAN_AUDIO_FILE, BUILDING_HANDICAP_LONGITUDES[i]);
            //values.put(COLUMN_MAND_AUDIO_FILE, BUILDING_HANDICAP_LONGITUDES[i]);
            db.insert(TABLE_TEXT_AUDIO_RESOURCES, null, values);
        }
    }
}
