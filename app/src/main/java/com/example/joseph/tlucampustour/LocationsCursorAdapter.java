package com.example.joseph.tlucampustour;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LocationsCursorAdapter extends CursorAdapter {

    public LocationsCursorAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.tour_location,parent,false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String exampleLocation = "First Tour Stop";

        // Set the text of the view with the note text
        Button locationButton = (Button) view.findViewById(R.id.tourLocationName);
        locationButton.setText(exampleLocation);

    }
}
