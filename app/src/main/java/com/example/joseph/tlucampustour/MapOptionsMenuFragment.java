package com.example.joseph.tlucampustour;

import android.app.*;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.google.android.gms.identity.intents.AddressConstants;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 10/12/2016.
 */

public class MapOptionsMenuFragment extends DialogFragment {

    private Dialog options;
    private RadioGroup mapViewGroup;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
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
