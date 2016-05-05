package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    //TODO: find out how to create a toggle at the bottom
    //TODO: find out how to create and teardown database after session ("dummyData")
    //TODO: need to track which view user previusly was at

    //Instance variables
    private TextView mMovieText;
    private int desiredSizeOfList = 3;
    private int specifiedYear;
    MovieDAO mSQLMovieDAO = new App().getMovieDAO();
    TabHost mTabHost;
    private ListView listViewTopMovies;
    private ListView listViewWorstMovies;
    private ListView listViewTopMoviesByYear;
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

        listViewTopMovies = (ListView) findViewById(R.id.listViewTopMovies);
        listViewWorstMovies = (ListView) findViewById(R.id.listViewWorstMovies);
        listViewTopMoviesByYear = (ListView) findViewById(R.id.listViewTopMoviesByYear);

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
                    //call MovieCell.createMovieView()(...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies) --> returns listOfViewCellsWeGotFromHelpClass
                    //Add each one of the Vies to created ListCell


                    //Test to fill the listView with strings:
                    String[] dummyStringArray = {"BraveHeart", "Speed"};
                    List<String> dummyStringList = new ArrayList<String>();
                    dummyStringList.add("BraveHeart");
                    dummyStringList.add("Speed");


                    //Test to fill the listView with other objects than strings:
                    Button a = new Button(getApplicationContext());
                    Button b = new Button(getApplicationContext());
                    Button c = new Button(getApplicationContext());

                    Object[] dummyObjects = {a,b,c};


                    populateListView(listViewTopMovies, dummyStringList);


                }
                else if(tabId == mTabSpecWorstMovies.getTag()){
                    mMovieText.setText("Worst Movies");

                    //TODO:
                    //List<Movie> mostDislikedMovies = mSQLMovieDAO.getMostDislikedMovies(desiredSizeOfList);
                    //call MovieCell.createMovieView() class (...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies)
                    //Add each one of the Vies to created ListCell

                    //populateListView(listViewWorstMovies, listOfViewCellsWeGotFromHelpClass);

                }
                else{

                    mMovieText.setText("Top Movies Year XXXX");

                    //TODO:
                    //List<Movie> topRatedThisYear = mSQLMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, specifiedYear);
                    //callMovieCellClass(); (...should return a list of Views of the corresponding movies)
                    //Add each one of the Vies to created ListCell

                    //populateListView(listViewTopMoviesByYear, listOfViewCellsWeGotFromHelpClass);

                }

            }
        });


    }

    public void populateListView(ListView l, List<String> listOfViewCellsWeGotFromHelpClass){

        int a = android.R.layout.simple_list_item_1;

        //Code for populating elements in the listView;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,a,listOfViewCellsWeGotFromHelpClass);
        listViewTopMovies.setAdapter(adapter);

        //TODO: Need to write methods onClicked for the different elements in the list;

    }

    public void setUpListeners(){


    }

}
