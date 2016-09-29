package com.example.joseph.tlucampustour;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.*;
import android.widget.TextView;

import static com.example.joseph.tlucampustour.Constants.*;

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
        String tourStopName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        TextView tourStopNameTV = (TextView) view.findViewById(R.id.tour_stop_item);
        tourStopNameTV.setText(tourStopName);
    }

    public TourStop getTourStop(int position)
    {
        Cursor myCursor = getCursor();
        String name = myCursor.getString(NAME_COL_POSITION);
        double lat = myCursor.getDouble(LAT_COL_POSITION);
        double lon = myCursor.getDouble(LONG_COL_POSITION);
        double radius = myCursor.getDouble(RADIUS_COL_POSITION);
        int info = myCursor.getInt(INFO_COL_POSITION);
        int img = myCursor.getInt(IMG_COL_POSITION);
        int audio = myCursor.getInt(AUDIO_COL_POSITION);
        return new TourStop(name,lat,lon,radius,info,img,audio);
    }
}
