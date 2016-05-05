package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    //TODO: find out how to create a toggle at the bottom
    //TODO: find out how to create and teardown database after session ("dummyData")
    //TODO: need to track which view user previusly was at

    //Instance variables
    private int desiredSizeOfList = 3;
    private int specifiedYear = 2016;
    MovieDAO mSQLMovieDAO = new App().getMovieDAO();
    TabHost mTabHost;
    private ListView listViewTopMovies;
    private ListView listViewWorstMovies;
    private ListView listViewTopMoviesByYear;
    private int thisYear = 2016; //NOTE: need to be changed into a dynamic fetch -> Date.getYear() or something similar

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

                    //TODO:
                    //List<Movie> topRatedAllTime = mSQLMovieDAO.getCommunityTopPicks(desiredSizeOfList);
                    //call MovieCell.createMovieView()(...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies) --> returns listOfViewCellsWeGotFromHelpClass
                    //Add each one of the Vies to created ListCell


                    //Test to fill the listView with strings:
                    List<String> dummyStringList = new ArrayList<String>();
                    dummyStringList.add("BraveHeart");
                    dummyStringList.add("Gone with the Wind");
                    dummyStringList.add("Jurassic Park");
                    dummyStringList.add("Back to the Future II");
                    dummyStringList.add("Karate Kid");
                    dummyStringList.add("Oblivion");


                    /*
                    //Test to fill the listView with other objects than strings:
                    Button a = new Button(getApplicationContext());
                    Button b = new Button(getApplicationContext());
                    Button c = new Button(getApplicationContext());

                    Object[] dummyObjects = {a,b,c};
                    */

                    populateListView(listViewTopMovies, dummyStringList);

                }
                else if(tabId == mTabSpecWorstMovies.getTag()){

                    //TODO:
                    //List<Movie> mostDislikedMovies = mSQLMovieDAO.getMostDislikedMovies(desiredSizeOfList);
                    //call MovieCell.createMovieView() class (...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies)
                    //Add each one of the Vies to created ListCell

                    List<String> dummyStringList = new ArrayList<String>();
                    dummyStringList.add("Sharknado");
                    dummyStringList.add("Scream IV");
                    dummyStringList.add("Jurassic World");

                    populateListView(listViewWorstMovies, dummyStringList);

                }
                else{

                    //TODO:
                    //List<Movie> topRatedThisYear = mSQLMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, specifiedYear);
                    //callMovieCellClass(); (...should return a list of Views of the corresponding movies)
                    //Add each one of the Vies to created ListCell

                    List<String> yearList = generateYearList();
                    populateListView(listViewTopMoviesByYear,yearList);

                }

            }
        });
    }

    public List<String> generateYearList(){

        List<String> years = new ArrayList<String>();

        for (int i = thisYear; i >= 1900; i--){
            years.add(i+"");
        }
        return years;
    }

    public void populateListView(ListView listView, List<String> listOfViewCellsWeGotFromHelpClass){

        int layout = android.R.layout.simple_list_item_1;

        //Code for populating elements in the listView;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,layout,listOfViewCellsWeGotFromHelpClass);
        listView.setAdapter(adapter);

        //TODO: Need to write methods onClicked for the different elements in the list;
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

                //Go into another subMenu: A list of Movies by that year:

                //...Fetch that year and call:
                //List<Movie> topRatedThisYear = mSQLMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, specifiedYear);

                //then re-populate the listview by calling the HelpClass with the list above and sending that list to populateList()
                // In this case dummyMovies - 2014 no matter what year the user clicks:

                //dummyMovies 2014
                List<String> dummyStringList = new ArrayList<String>();
                dummyStringList.add("Whiplash (2014)");
                dummyStringList.add("Guardian of the Galaxy (2014)");
                dummyStringList.add("Birdman (2014)");
                dummyStringList.add("Interstellar (2014)");

                populateListView(listViewTopMoviesByYear,dummyStringList);

                //TODO: Thnk about how the user should "go back" to the year screen
            }
        });
    }

}




