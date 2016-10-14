package com.example.joseph.tlucampustour;

import android.app.*;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import static com.example.joseph.tlucampustour.Constants.*;

/**
 * Created by Joseph on 10/14/2016.
 */

public class UserPreferencesOptionMenu extends DialogFragment {

    private Dialog options;
    private RadioGroup languageGroup;
    private CheckBox handicapOption;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.user_prefs, null);
        options = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Settings")
                .setPositiveButton(android.R.string.ok, null)
                .create();

        UserPrefsShowListener myShowListener = new UserPrefsShowListener();
        options.setOnShowListener(myShowListener);

        return options;
    }

    private class UserPrefsShowListener implements DialogInterface.OnShowListener {

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
            UserPrefsCompleteListener myClickListener = new UserPrefsCompleteListener();
            positiveButton.setOnClickListener(myClickListener);

            // Get reference to all needed widgets
            languageGroup = (RadioGroup) options.findViewById(R.id.languageChoiceGroup);
            handicapOption = (CheckBox) options.findViewById(R.id.handicapOption);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            handicapOption.setChecked(prefs.getBoolean(ACCESS_PREF_RESULT, false));
        }
    }

    private class UserPrefsCompleteListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            // Check language prefs
            int languageChoice = 0;
            int checkedViewButton = languageGroup.getCheckedRadioButtonId();

            // Check which radio button was clicked
            switch(checkedViewButton)
            {
                case R.id.englishOption:
                    languageChoice = ENGLISH_CHOICE;
                    break;
                case R.id.spanishOption:
                    languageChoice = SPANISH_CHOICE;
                    break;
                case R.id.mandarinOption:
                    languageChoice = MANDARIN_CHOICE;
                    break;
            }

            // Check accessibility prefs
            boolean useHandicapEntrances = handicapOption.isChecked();

            // Send choices back to Directions activity
            Bundle prefs = new Bundle();
            prefs.putInt(LANGUAGE_PREF_RESULT, languageChoice);
            prefs.putBoolean(ACCESS_PREF_RESULT, useHandicapEntrances);
            TourInfo callingActivity = (TourInfo) getActivity();
            callingActivity.onSettingsSelected(prefs);
            getDialog().dismiss();
        }
    }
}
