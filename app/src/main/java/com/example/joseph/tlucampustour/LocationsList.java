package com.example.joseph.tlucampustour;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class LocationsList extends AppCompatActivity {

    private CursorAdapter myCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_list);
        ActionBar myActionBar = getSupportActionBar();
        if (myActionBar != null)
        {
            myActionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Tour Stops");


        myCursorAdapter = new LocationsCursorAdapter(this,null,0);


        ListView list = (ListView) findViewById(R.id.locationList);
        list.setAdapter(myCursorAdapter);
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        finish();
    }
}
