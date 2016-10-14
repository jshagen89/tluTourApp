package com.example.joseph.tlucampustour;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.prefs.Preferences;

import static com.example.joseph.tlucampustour.Constants.*;

public class TourInfo extends AppCompatActivity {

    private boolean dialogOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock orientation to portrait if device is a phone
        if(getResources().getBoolean(R.bool.portrait_only))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_tour_info);
        setTitle(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tour_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            createSettingsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createSettingsDialog()
    {
        FragmentManager fm = getFragmentManager();
        UserPreferencesOptionMenu options = new UserPreferencesOptionMenu();
        options.show(fm, "Settings");
        dialogOpen = true;
    }

    // Called by settings dialog once user finishes selecting options
    public void onSettingsSelected(Bundle prefs)
    {
        // Get selected preferences or use default values
        int languagePref = prefs.getInt(LANGUAGE_PREF_RESULT, ENGLISH_CHOICE);
        boolean useHandicapEntries = prefs.getBoolean(ACCESS_PREF_RESULT, false);

        // Update user preferences with new choices
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putInt(LANGUAGE_PREF_RESULT, languagePref);
        prefsEditor.putBoolean(ACCESS_PREF_RESULT, useHandicapEntries);
        prefsEditor.apply();
        Toast toast = Toast.makeText(this, "Settings Updated", Toast.LENGTH_SHORT);
        toast.show();
        dialogOpen = false;
    }

    // Opens the location list
    public void startTour(View view) {
        if (!dialogOpen)
        {
            Intent myIntent = new Intent(this,TourStopList.class);
            startActivity(myIntent);
        }
    }
}
