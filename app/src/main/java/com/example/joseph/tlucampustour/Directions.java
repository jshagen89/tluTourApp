package com.example.joseph.tlucampustour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Directions extends AppCompatActivity {

    private TourStop destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        destination = getIntent().getExtras().getParcelable("Selected Stop");
        String destName = "";
        if (destination != null)
        {
            destName = destination.getName();
        }
        setTitle("Directions to " + destName);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView myTV = (TextView) findViewById(R.id.tourStopName);
        myTV.setText(destName);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called if user presses the back button
    @Override
    public void onBackPressed () {
        setResult(RESULT_OK);
        finish();
    }

    // Called by return to list button
    public void returnToList(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    // Called if user arrives at a tour stop
    public void reachedTourStop(View view)
    {
        Intent myIntent = new Intent(Directions.this, TourStopInfo.class);
        myIntent.putExtra("TourStop", destination);
        startActivity(myIntent);
    }

}
