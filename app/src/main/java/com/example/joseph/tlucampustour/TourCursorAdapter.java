package com.example.joseph.tlucampustour;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Joseph on 9/14/2016.
 */
public class TourCursorAdapter extends CursorAdapter {

    public TourCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.tour_stop_list_item,parent,false);
    }

    // Sets fields in the list of tour stops appropriately
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String tourStopName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME));
        TextView tourStopNameTV = (TextView) view.findViewById(R.id.tour_stop_item);
        tourStopNameTV.setText(tourStopName);
    }

    public TourStop getTourStop(int position)
    {
        Cursor myCursor = getCursor();
        String name = myCursor.getString(DBOpenHelper.NAME_COL_POSITION);
        float lat = myCursor.getFloat(DBOpenHelper.LAT_COL_POSITION);
        float lon = myCursor.getFloat(DBOpenHelper.LONG_COL_POSITION);
        String img = myCursor.getString(DBOpenHelper.IMG_COL_POSITION);
        String audio = myCursor.getString(DBOpenHelper.AUDIO_COL_POSITION);
        return new TourStop(name,lat,lon,img,audio);
    }
}
