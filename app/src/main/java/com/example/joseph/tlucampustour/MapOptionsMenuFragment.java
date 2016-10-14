package com.example.joseph.tlucampustour;

import android.app.*;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.google.android.gms.maps.GoogleMap;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 10/12/2016.
 */

public class MapOptionsMenuFragment extends DialogFragment {

    private Dialog options;
    private RadioGroup mapViewGroup;
    private int currMapType;

    public static MapOptionsMenuFragment newInstance(int currMapView)
    {
        Bundle args = new Bundle();
        args.putInt(MAP_OPTIONS_RESULT, currMapView);
        MapOptionsMenuFragment fragmentMenu = new MapOptionsMenuFragment();
        fragmentMenu.setArguments(args);
        return fragmentMenu;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Check to see what type of map is currently displayed
        currMapType = getArguments().getInt(MAP_OPTIONS_RESULT);

        // Create dialog and set appropriate attributes
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.map_options, null);
        options = new AlertDialog.Builder(getActivity())
            .setView(v)
            .setTitle("Map Options")
            .setPositiveButton(android.R.string.ok, null)
            .create();
        MapOptionsShowListener myShowListener = new MapOptionsShowListener();
        options.setOnShowListener(myShowListener);

        return options;
    }

    private class MapOptionsShowListener implements DialogInterface.OnShowListener {

        @Override
        public void onShow(DialogInterface dialogInterface) {
            options.setCancelable(false);
            // Change color of title text
            int textViewId = options.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
            TextView titleTV = (TextView) options.findViewById(textViewId);
            titleTV.setTextColor(Color.WHITE);

            // Change color of dialog box to dark grey to match theme
            if (options.getWindow() != null)
            {
                options.getWindow().setBackgroundDrawableResource(R.color.dark_grey);
            }

            // Change positive button text color
            Button positiveButton = ((AlertDialog)options).getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(Color.WHITE);
            MapOptionsCompleteListener myButtonListener = new MapOptionsCompleteListener();
            positiveButton.setOnClickListener(myButtonListener);
            mapViewGroup = (RadioGroup) options.findViewById(R.id.mapViewGroup);

            // Check radioButton for current map type
            if (currMapType == GoogleMap.MAP_TYPE_HYBRID)
            {
                RadioButton satelliteOption = (RadioButton) options.findViewById(R.id.satelliteViewOption);
                satelliteOption.setChecked(true);
            }
            else if (currMapType == GoogleMap.MAP_TYPE_NORMAL)
            {
                RadioButton normalOption = (RadioButton) options.findViewById(R.id.normalViewOption);
                normalOption.setChecked(true);
            }
        }
    }

    private class MapOptionsCompleteListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int mapTypeChoice = 0;
            int checkedViewButton = mapViewGroup.getCheckedRadioButtonId();

            // Check which radio button was clicked
            switch(checkedViewButton)
            {
                case R.id.satelliteViewOption:
                    mapTypeChoice = SATELLITE_MAP_CHOICE;
                    break;
                case R.id.normalViewOption:
                    mapTypeChoice = NORMAL_MAP_CHOICE;
                    break;
            }

            // Send choices back to Directions activity
            Bundle selections = new Bundle();
            selections.putInt(MAP_OPTIONS_RESULT, mapTypeChoice);
            Directions callingActivity = (Directions) getActivity();
            callingActivity.onOptionsSelected(selections);
            getDialog().dismiss();
        }
    }
}
