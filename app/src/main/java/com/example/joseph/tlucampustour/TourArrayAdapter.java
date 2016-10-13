package com.example.joseph.tlucampustour;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 9/14/2016.
 */
public class TourArrayAdapter extends ArrayAdapter {

    private ArrayList<TourStop> allTourStops;
    private Context givenContext;

    public TourArrayAdapter(Context context, ArrayList<TourStop> tourStops) {
        super(context, 0, tourStops);
        allTourStops = new ArrayList<>(tourStops);
        givenContext = context;
    }

    @Override
    public View getView(int position, View convertView, @Nullable ViewGroup parent)
    {
        // get the TourStop that we are displaying
        TourStop currStop = allTourStops.get(position);

        // get the inflater and inflate the layout XML
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) givenContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tour_stop_list_item, null);
        }

        // Set the title of the list item
        TextView list_item = (TextView) convertView.findViewById(R.id.tour_stop_item);
        list_item.setText(currStop.getName());

        return convertView;
    }


    public TourStop getTourStop(int position)
    {
        return (allTourStops.get(position));
    }
}
