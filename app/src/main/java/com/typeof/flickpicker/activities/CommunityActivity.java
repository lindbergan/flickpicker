package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    //TODO: find out how to create a toggle at the bottom
    //TODO: find out how to create and teardown database after session ("dummyData")

    //Instance variables
    private TextView mMovieText;
    private int desiredSizeOfList = 3;
    private int specifiedYear;
    MovieDAO mSQLMovieDAO = new App().getMovieDAO();
    TabHost mTabHost;
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
        setUpListeners();

    }

    public void hookUpViews(){

        //Configure the tabs
        configureTabs();
        mMovieText = (TextView) findViewById(R.id.showMovieText);

    }

    public void configureTabs(){

        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        final TabSpec mTabSpecTopMovies = mTabHost.newTabSpec("topMovies");
        mTabSpecTopMovies.setContent(R.id.tabTopMovies);
        mTabSpecTopMovies.setIndicator("Top Movies");
        mTabHost.addTab(mTabSpecTopMovies);

        final TabSpec mTabSpecWorstMovies = mTabHost.newTabSpec("worstMovies");
        mTabSpecWorstMovies.setContent(R.id.tabWorstMovies);
        mTabSpecWorstMovies.setIndicator("Worst Movies");
        mTabHost.addTab(mTabSpecWorstMovies);

        TabSpec mTabSpecTopMoviesYear = mTabHost.newTabSpec("topMoviesYear");
        mTabSpecTopMoviesYear.setContent(R.id.tabTopMoviesYear);
        mTabSpecTopMoviesYear.setIndicator("Top Movies by Year");
        mTabHost.addTab(mTabSpecTopMoviesYear);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId == mTabSpecTopMovies.getTag()){
                    mMovieText.setText("Top Movies");

                    //TODO:
                    //List<Movie> topRatedAllTime = mSQLMovieDAO.getCommunityTopPicks(desiredSizeOfList);
                    //call MovieCell.createMovieView()(...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies)
                    //Add each one of the Vies to created ListCell


                }
                else if(tabId == mTabSpecWorstMovies.getTag()){
                    mMovieText.setText("Worst Movies");

                    //TODO:
                    //List<Movie> mostDislikedMovies = mSQLMovieDAO.getMostDislikedMovies(desiredSizeOfList);
                    //call MovieCell.createMovieView() class (...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies)
                    //Add each one of the Vies to created ListCell
                }
                else{

                    mMovieText.setText("Top Movies Year XXXX");

                    //TODO:
                    //List<Movie> topRatedThisYear = mSQLMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, specifiedYear);
                    //callMovieCellClass(); (...should return a list of Views of the corresponding movies)
                    //Add each one of the Vies to created ListCell
                }

            }
        });


    }

    public void setUpListeners(){

        /*
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
        */
    }

}
