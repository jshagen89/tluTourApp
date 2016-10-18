package com.example.joseph.tlucampustour;

import android.app.*;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.akexorcist.googledirection.constant.TransportMode;
import com.google.android.gms.maps.GoogleMap;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 10/12/2016.
 */

public class MapOptionsMenuFragment extends DialogFragment {

    private Dialog options;
    private RadioGroup mapViewGroup;
    private RadioGroup transportModeGroup;
    private int currMapType;
    private String currTransMode;

    public static MapOptionsMenuFragment newInstance(int currMapView, String transMode)
    {
        Bundle args = new Bundle();
        args.putInt(MAP_OPTIONS_RESULT, currMapView);
        args.putString(TRANSPORTATION_MODE_RESULT, transMode);
        MapOptionsMenuFragment fragmentMenu = new MapOptionsMenuFragment();
        fragmentMenu.setArguments(args);
        return fragmentMenu;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Check to see what type of map is currently displayed
        currMapType = getArguments().getInt(MAP_OPTIONS_RESULT);
        currTransMode = getArguments().getString(TRANSPORTATION_MODE_RESULT);

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
            transportModeGroup = (RadioGroup) options.findViewById(R.id.transportModeGroup);

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

            // Check radioButton for current mode of transportation
            if (currTransMode.equals(TransportMode.WALKING))
            {
                RadioButton walkingOption = (RadioButton) options.findViewById(R.id.walkingOption);
                walkingOption.setChecked(true);
            }
            else
            {
                RadioButton drivingOption = (RadioButton) options.findViewById(R.id.drivingOption);
                drivingOption.setChecked(true);
            }
        }
    }

    private class MapOptionsCompleteListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int mapTypeChoice = currMapType;
            String transportModeChoice = currTransMode;
            int checkedViewButton = mapViewGroup.getCheckedRadioButtonId();
            int checkedTransportMode = transportModeGroup.getCheckedRadioButtonId();
            // Check which radio button was clicked for map view
            switch(checkedViewButton)
            {
                case R.id.satelliteViewOption:
                    mapTypeChoice = SATELLITE_MAP_CHOICE;
                    break;
                case R.id.normalViewOption:
                    mapTypeChoice = NORMAL_MAP_CHOICE;
                    break;
            }

            // Check which radio button was clicked for transportation mode
            switch (checkedTransportMode)
            {
                case R.id.walkingOption:
                    transportModeChoice = TransportMode.WALKING;
                    break;
                case R.id.drivingOption:
                    transportModeChoice = TransportMode.DRIVING;
                    break;
            }

            // Send choices back to Directions activity
            Bundle selections = new Bundle();
            selections.putInt(MAP_OPTIONS_RESULT, mapTypeChoice);
            selections.putString(TRANSPORTATION_MODE_RESULT, transportModeChoice);
            Directions callingActivity = (Directions) getActivity();
            callingActivity.onOptionsSelected(selections);
            getDialog().dismiss();
        }
    }
}
