package com.typeof.flickpicker.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.sql.SQLMovieDAO;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-03.
 */
public class CommunityActivity extends AppCompatActivity {

    //Instance variables (mainly views)
    private Button mTopMoviesButton;
    private Button mTopThisYearButton;
    private Button mWorstMoviesButton;
    private TextView mMovieText;
    private int desiredSizeOfList = 3;
    MovieDAO mSQLMovieDAO = new App().getMovieDAO();
    SeedData mSeedData = new SeedData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_main);

        ////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //////////////////////////

        //Hook up views (Buttons, TextFields Cells etc...)
        mTopMoviesButton = (Button) findViewById(R.id.topPicksButton);
        mWorstMoviesButton = (Button) findViewById(R.id.worstMovieButton);
        mTopThisYearButton = (Button) findViewById(R.id.topThisYearButton);
        mMovieText = (TextView) findViewById(R.id.showMovieText);

        //Feed the database with dummy Data
        mSeedData.seed();

        //setup listeners here
        mTopMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Movie> topPicks = mSQLMovieDAO.getCommunityTopPicks(desiredSizeOfList);
                mMovieText.setText(topPicks.get(0).getTitle());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
