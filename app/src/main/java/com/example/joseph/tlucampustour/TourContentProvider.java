package com.example.joseph.tlucampustour;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Joseph on 9/14/2016.
 */
public class TourContentProvider extends ContentProvider{

    private static final String AUTHORITY = "com.example.joseph.tlucampustour.tourcontentprovider";
    public static final String TABLE_TOUR_STOPS = "tourstops";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_TOUR_STOPS);
    public static final int TABLE_REF = 1;
    public static final int ID_REF = 2;

    private static final UriMatcher myUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        myUriMatcher.addURI(AUTHORITY, TABLE_TOUR_STOPS, TABLE_REF);
        myUriMatcher.addURI(AUTHORITY, TABLE_TOUR_STOPS + "/#", ID_REF);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        DBOpenHelper myDBHelper = new DBOpenHelper(getContext());
        db = myDBHelper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] args, String sortOrder) {
        return db.query(DBOpenHelper.TABLE_TOUR_STOPS,DBOpenHelper.TOUR_STOP_COLUMNS,selection,null,null,null,
                DBOpenHelper.COLUMN_NAME + " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id = db.insert(DBOpenHelper.TABLE_TOUR_STOPS,null,values);
        return Uri.parse(TABLE_TOUR_STOPS + "/" + id);
    }

    // Not needed for this application since user cannot delete db records
    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    // Not needed for this application since user cannot add new db records
    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
