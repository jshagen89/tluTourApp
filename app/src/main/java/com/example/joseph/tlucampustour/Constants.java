package com.example.joseph.tlucampustour;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Joseph on 9/28/2016.
 */

abstract class Constants {

    // Tour Stop Info
    static final String[] TOUR_STOP_NAMES = {"Martin Luther Statue", "AT&T & Moody Science Complex", "Emma Frey",
            "Tschoepe Hall", "Baldus, Clifton, Trinity Housing", "Krost and Health Sciences", "Weston Ranch",
            "Chapel of the Abiding Presence", "Campus Ministry/Servant Leadership", "Hein Dining Hall", "Centennial Hall Courtyard",
            "Jackson Auditorium", "Sports Complex/Athletic Training", "Fitness Center", "Graduation Walk", "Alumni Student Center",
            "Blumburg Memorial Library", "Schuech Fine Arts", "Langner Hall", "Alumni Plaza"};
    static final double[] TOUR_STOP_CENTER_LATITUDES = {29.57089, 29.57098, 29.57148, 29.57206, 29.57184, 29.57096,
            29.5745, 29.57262, 29.57322, 29.57208, 29.57218, 29.57173, 29.5764, 29.57464, 29.57395, 29.57331,
            29.57323, 29.57332, 29.57239, 29.57269};
    static final double[] TOUR_STOP_CENTER_LONGITUDES = {-97.98201, -97.98293, -97.98263, -97.98265, -97.98363, -97.98347,
            -97.98088, -97.98376, -97.98481, -97.98512, -97.98605, -97.98715, -97.98376, -97.98339, -97.98297, -97.9836, -97.98262,
            -97.98161, -97.98145, -97.98203};
    static final int[] TOUR_STOP_RADII = {20, 45, 30, 40, 55, 30, 30, 30, 40, 45, 50, 50, 60, 65, 35, 40, 40, 50, 30, 20};
    static final int[] TOUR_STOP_IMAGE_IDS = {R.drawable.martin_luther_statue, R.drawable.att_science, R.drawable.emma_frey,
            R.drawable.tschoepe, R.drawable.baldus_clifton_trinity_housing, R.drawable.krost_health_sciences, R.drawable.weston_ranch,
            R.drawable.chapel, R.drawable.campus_ministry, R.drawable.hein_dining_hall, R.drawable.centennial_hall_courtyard,
            R.drawable.jackson_auditorium, R.drawable.sports_complex_athletic_training, R.drawable.fitness_center,
            R.drawable.graduation_walk, R.drawable.asc, R.drawable.blumberg_library, R.drawable.schuech_fine_arts,
            R.drawable.langner_hall, R.drawable.alumni_plaza};
    static final int[] TOUR_STOP_IS_BUILDING = {0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0};
    static final String[] BUILDING_NAMES = {"AT&T & Moody Science Complex", "Emma Frey", "Tschoepe Hall", "Baldus, Clifton, Trinity Housing",
            "Krost and Health Sciences", "Chapel of the Abiding Presence", "Campus Ministry/Servant Leadership", "Hein Dining Hall",
            "Centennial Hall Courtyard", "Jackson Auditorium", "Fitness Center", "Alumni Student Center", "Blumburg Memorial Library",
            "Schuech Fine Arts", "Langner Hall"};
    static final double[] BUILDING_ENTRY_LATITUDES = {29.571, 29.57147, 29.57216, 29.57184, 29.57096, 29.57263,
            29.57322, 29.57197, 29.57218, 29.57163, 29.57476, 29.5731, 29.57308, 29.57317,
            29.57238};
    static final double[] BUILDING_ENTRY_LONGITUDES = {-97.98255, -97.98255, -97.98253, -97.98363, -97.98347,
            -97.98357,-97.98481, -97.98512, -97.98605, -97.98692, -97.98353, -97.98367,
            -97.98262, -97.98179, -97.98151};
    static final double[] BUILDING_HANDICAP_LATITUDES = {29.571, 29.57167, 29.57219, 29.57184, 29.57096, 29.57254, 29.57322,
            29.57202, 29.57218, 29.57173, 29.57464, 29.5731, 29.57338, 29.57317, 29.57217};
    static final double[] BUILDING_HANDICAP_LONGITUDES = {-97.98255, -97.98264, -97.98289, -97.98363, -97.98347, -97.98384,
            -97.98481, -97.98481, -97.98605, -97.98715, -97.98339, -97.98367, -97.98264, -97.98179, -97.98146};
    static final int[] TOUR_STOP_TXT_IDS = {R.string.martin_luther_statue_info, R.string.att_science_info, R.string.emma_frey_info,
            R.string.tschoepe_info, R.string.baldus_clifton_trinity_info, R.string.krost_info, R.string.weston_ranch_info, R.string.chapel_info,
            R.string.campus_ministry_info, R.string.hein_info, R.string.centennial_info, R.string.jackson_info, R.string.sports_complex_info,
            R.string.fitness_center_info, R.string.graduation_walk_info, R.string.asc_info, R.string.library_info, R.string.scheuch_info,
            R.string.langner_info, R.string.alumni_plaza_info};
    static final int[] TOUR_STOP_AUDIO_IDS = {R.raw.martin_luther_statue, R.raw.att_science, R.raw.emma_frey, R.raw.tschope, R.raw.baldus_clifton_trinity,
            R.raw.krost, R.raw.weston_ranch, R.raw.chapel, R.raw.campus_ministry, R.raw.hein_dining_hall, R.raw.centennial_hall,
            R.raw.jackson_auditorium, R.raw.sports_complex, R.raw.fitness_center, R.raw.graduation_walk, R.raw.asc, R.raw.blumburg_library,
            R.raw.schuech_fine_arts, R.raw.langner_hall, R.raw.alumni_plaza};

    // Database Creation and Table Refs
    static final String DATABASE_NAME = "tluCampusTour.db";
    static final String TABLE_TOUR_STOPS = "tourStops";
    static final String TABLE_TEXT_AUDIO_RESOURCES = "txtAudioResources";
    static final String TABLE_BUILDING_INFO = "buildingInfo";
    static final int DATABASE_VERSION = 1;

    // Database Columns
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_CENTER_LATITUDE = "centerLatitude";
    static final String COLUMN_CENTER_LONGITUDE = "centerLongitude";
    static final String COLUMN_RADIUS = "radius";
    static final String COLUMN_IMAGE = "imageID";
    static final String COLUMN_IS_BUILDING = "isBuilding";
    static final String COLUMN_ENTRY_LATITUDE = "entryLatitude";
    static final String COLUMN_ENTRY_LONGITUDE = "entryLongitude";
    static final String COLUMN_HANDICAP_LATITUDE = "handicapLatitude";
    static final String COLUMN_HANDICAP_LONGITUDE = "handicapLongitude";
    static final String COLUMN_INFO_TEXT = "engInfoTextID";
    static final String COLUMN_AUDIO_FILE = "engAudioFileID";
    static final String[] TOUR_STOP_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_CENTER_LATITUDE, COLUMN_CENTER_LONGITUDE,
            COLUMN_RADIUS, COLUMN_IMAGE, COLUMN_IS_BUILDING};
    static final String[] BUILDING_INFO_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_ENTRY_LATITUDE, COLUMN_ENTRY_LONGITUDE,
            COLUMN_HANDICAP_LATITUDE, COLUMN_HANDICAP_LONGITUDE};
    static final String[] TEXT_AUDIO_RESOURCES_COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_INFO_TEXT, COLUMN_AUDIO_FILE};

    // Table Create Queries
    static final String TOUR_STOP_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TOUR_STOPS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_CENTER_LATITUDE + " REAL, " +
                    COLUMN_CENTER_LONGITUDE + " REAL, " +
                    COLUMN_RADIUS + " REAL, " +
                    COLUMN_IMAGE + " INTEGER, " +
                    COLUMN_IS_BUILDING + " INTEGER" + ")";
    static final String BUILDING_INFO_TABLE_CREATE =
            "CREATE TABLE " + TABLE_BUILDING_INFO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_ENTRY_LATITUDE + " REAL, " +
                    COLUMN_ENTRY_LONGITUDE + " REAL, " +
                    COLUMN_HANDICAP_LATITUDE + " REAL, " +
                    COLUMN_HANDICAP_LONGITUDE + " REAL" + ")";
    static final String TXT_AUDIO_RES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TEXT_AUDIO_RESOURCES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_INFO_TEXT + " INTEGER, " +
                    COLUMN_AUDIO_FILE + " INTEGER" + ")";

    // Column Position Refs for Queries
    static final int ID_COL_POSITION = 0;
    static final int NAME_COL_POSITION = 1;
    static final int CENTER_LAT_COL_POSITION = 2;
    static final int CENTER_LONG_COL_POSITION = 3;
    static final int RADIUS_COL_POSITION = 4;
    static final int IMG_COL_POSITION = 5;
    static final int IS_BUILDING_COL_POSITION = 6;
    static final int ENTRY_LAT_COL_POSITION = 2;
    static final int ENTRY_LON_COL_POSITION = 3;
    static final int HANDICAP_LAT_COL_POSITION = 4;
    static final int HANDICAP_LON_COL_POSITION = 5;
    static final int TXT_COL_POSITION = 2;
    static final int AUDIO_COL_POSITION = 3;
    static final int NUM_TOUR_STOPS = 20;
    static final int NUM_BUILDINGS = 15;

    // Content Provider Constants
    static final String AUTHORITY = "com.example.joseph.tlucampustour.tourcontentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TOUR_STOPS);
    static final int TABLE_REF = 1;
    static final int ID_REF = 2;

    // Passing Data Between Activities Constants
    static final String SELECTED_STOP_EXTRA = "Selected Stop";
    static final String TOUR_STOP_ARRAY_EXTRA = "Tour Stop Array";
    static final String MAP_OPTIONS_RESULT = "Map View";
    static final String TRANSPORTATION_MODE_RESULT = "Directions Type";
    static final int SATELLITE_MAP_CHOICE = 1;
    static final int NORMAL_MAP_CHOICE = 2;
    static final String LANGUAGE_PREF_RESULT = "Language Pref";
    static final String ACCESS_PREF_RESULT = "Accessibility Pref";
    static final String PREV_LANGUAGE_RESULT = "Previous Language Pref";
    static final int ENGLISH_CHOICE = 1;
    static final int SPANISH_CHOICE = 2;

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
}
