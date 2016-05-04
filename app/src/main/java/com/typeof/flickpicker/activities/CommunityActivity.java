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

import com.roughike.bottombar.BottomBar;
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

    //Instance variables
    private Button mTopMoviesButton;
    private Button mTopThisYearButton;
    private Button mWorstMoviesButton;
    private TextView mMovieText;
    private int desiredSizeOfList = 3;
    MovieDAO mSQLMovieDAO = new App().getMovieDAO();
    //SeedData mSeedData = new SeedData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_main);

        //Feed the database with dummy Data
        //mSeedData.seed();

        //Hook up views (Buttons, TextFields Cells etc...)
        hookUpViews();

        //Connect the listsners to the relevant views
        setUpClickListeners();

    }

    public void hookUpViews(){

        mTopMoviesButton = (Button) findViewById(R.id.topPicksButton);
        mWorstMoviesButton = (Button) findViewById(R.id.worstMovieButton);
        mTopThisYearButton = (Button) findViewById(R.id.topThisYearButton);
        mMovieText = (TextView) findViewById(R.id.showMovieText);

    }

    public void setUpClickListeners(){


        //setup listeners here
        mTopMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Movie> topPicks = mSQLMovieDAO.getCommunityTopPicks(desiredSizeOfList);
                mMovieText.setText(topPicks.get(0).getTitle());
            }
        });
        mWorstMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Movie> worstPick = mSQLMovieDAO.getMostDislikedMovies(desiredSizeOfList);
                mMovieText.setText(worstPick.get(0).getTitle());
            }
        });
        mTopThisYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Movie> topThisYear = mSQLMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, 2016);
                mMovieText.setText(topThisYear.get(0).getTitle());
            }
        });

    }

}
