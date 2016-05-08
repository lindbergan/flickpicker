package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    //TODO: find out how to create a toggle at the bottom for the whole app
    //TODO: find out how to create and teardown database after session ("dummyData") (sloved by Sebert?)
    //TODO: need to track which tab user previously was at (thus: currentTab)
    //TODO: create new cell: MovieCell (users rating, NOT community) BUT playlist want communityRating (that is - the present MovieCell)

    //Instance variables
    private int desiredSizeOfList = 6;
    MovieDAO mMovieDAO;
    TabHost mTabHost;
    private ListView listViewTopMovies;
    private ListView listViewWorstMovies;
    private ListView listViewTopMoviesByYear;
    private int thisYear = 2016; //NOTE: need to be changed into a dynamic fetch -> Date.getYear() or something similar
    private String currentTab;
    private boolean isYearListCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        mMovieDAO = App.getMovieDAO();

        //reboot the database
        App.getDatabase().dropTables();
        App.getDatabase().setUpTables();

        //Feed the database with dummy Data
        SeedData.seedCommunityData();

        //Hook up views (Buttons, TextFields Cells etc...)
        hookUpViews();

        //Connect the listeners to the relevant views
        setUpListeners();

    }

    public void hookUpViews(){

        //Configure the tabs
        configureTabs();

        listViewTopMovies = (ListView) findViewById(R.id.listViewTopMovies);
        listViewWorstMovies = (ListView) findViewById(R.id.listViewWorstMovies);
        listViewTopMoviesByYear = (ListView) findViewById(R.id.listViewTopMoviesByYear);
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

                    List <Movie> topMoviesAllTime = mMovieDAO.getCommunityTopPicks(desiredSizeOfList);
                    populateListView(listViewTopMovies, topMoviesAllTime);
                }
                else if(tabId == mTabSpecWorstMovies.getTag()){

                    List<Movie> worstMoviesAllTime = mMovieDAO.getMostDislikedMovies(desiredSizeOfList);
                    populateListView(listViewWorstMovies, worstMoviesAllTime);

                }
                else{
                    List<String> yearList = generateYearList();
                    populateListWithYears(listViewTopMoviesByYear,yearList);
                }
            }
        });
    }

    public void populateListWithYears(ListView listView, List<String> yearList){

        isYearListCurrent = true;
        int defaultLayout = android.R.layout.simple_list_item_1; //default
        ListAdapter yearAdapter = new ArrayAdapter(this,defaultLayout,yearList);
        listView.setAdapter(yearAdapter);
    }

    public void populateListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass){

        //Code for populating elements in the listView;
        ListAdapter adapter = new MovieAdapter(this,listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);
    }

    public List<String> generateYearList(){

        List<String> years = new ArrayList<String>();

        for (int i = thisYear; i >= 1900; i--){
            years.add(i+"");
        }
        return years;
    }

    public void setUpListeners(){

        listViewTopMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Go to MovieView/DetailedView
            }
        });

        listViewWorstMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Go to MovieView/DetailedView
            }
        });

        listViewTopMoviesByYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Determine if yearList or MovieList of that year is currently displayed
                if (isYearListCurrent){

                    //TODO: need to find the input year from user

                    int chosenYear = 2014;

                    //get the MovieList for the year in question
                    List<Movie> topMoviesByYear = mMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList,chosenYear);

                    //poulate the list and set isYearListCurrent to false
                    populateListView(listViewTopMoviesByYear,topMoviesByYear);
                    isYearListCurrent = false;
                }
                else{
                    //in that case - we are presently at the specific year movie list:
                    //TODO: detalied view of the movie
                    //TODO: Thnk about how the user should "go back" to the "year" screen
                }
            }
        });
    }

}




