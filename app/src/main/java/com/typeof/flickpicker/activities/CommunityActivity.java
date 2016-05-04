package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    //TODO: find out how to create a toggle at the bottom
    //TODO: find out how to create and teardown database after session ("dummyData")

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
        setContentView(R.layout.activity_community);

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
                //List<Movie> topPicks = mSQLMovieDAO.getCommunityTopPicks(desiredSizeOfList);
                //mMovieText.setText(topPicks.get(0).getTitle());
                mMovieText.setText("TopMovies");

            }
        });
        mWorstMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //List<Movie> worstPick = mSQLMovieDAO.getMostDislikedMovies(desiredSizeOfList);
                //mMovieText.setText(worstPick.get(0).getTitle());
                mMovieText.setText("LeastLiked");

            }
        });
        mTopThisYearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //List<Movie> topThisYear = mSQLMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, 2016);
                //mMovieText.setText(topThisYear.get(0).getTitle());
                mMovieText.setText("TopMoviesthisYear");

            }
        });
    }
}
