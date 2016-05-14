package com.typeof.flickpicker.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.utils.ExecutionTimeLogger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommunityFragment extends Fragment {

    //Instance variables
    private int desiredSizeOfList = 10;
    MovieDAO mMovieDAO;
    TabHost mTabHost;
    private ListView listViewTopMovies;
    private ListView listViewWorstMovies;
    private ListView listViewTopMoviesByYear;
    private int thisYear;
    private boolean isYearListCurrent;
    SingletonViewTracker viewTracker = SingletonViewTracker.getInstance();
    //--------------------------------
    private SQLiteDatabase db;
    //--------------------------------

    //TESTING
    private FragmentManager fragmentManager = getFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieDAO = App.getMovieDAO();

        //--------------------------------
        SQLiteDatabaseHelper dbhelper = SQLiteDatabaseHelper.getInstance(getActivity());
        db = dbhelper.getWritableDatabase();
        //--------------------------------

        thisYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View communityView = inflater.inflate(R.layout.activity_community, container, false);
        hookUpViews(communityView);
        configureTabs(communityView);
        determineCurrentView();
        setUpListeners();
        return communityView;
    }

    public void hookUpViews(View view) {
        listViewTopMovies = (ListView) view.findViewById(R.id.listViewTopMovies);
        listViewWorstMovies = (ListView) view.findViewById(R.id.listViewWorstMovies);
        listViewTopMoviesByYear = (ListView) view.findViewById(R.id.listViewTopMoviesByYear);
    }

    public void configureTabs(View view) {

        mTabHost = (TabHost) view.findViewById(R.id.tabHost);
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

                switch (tabId) {
                    case "topMovies":
                        setTopMoviesAsCurrentView();
                        break;
                    case "worstMovies":
                        setWorstMoviesAsCurrentView();
                        break;
                    default:
                        setTopMoviesByYearAsCurrentView();

                        break;
                }
            }
        });
    }

    public void setTopMoviesAsCurrentView(){
        List <Movie> topMoviesAllTime = mMovieDAO.getCommunityTopPicks(desiredSizeOfList);
        populateListView(listViewTopMovies, topMoviesAllTime);
        viewTracker.setCurrentCommunityTab("topMovies");
    }
    public void setWorstMoviesAsCurrentView(){
        List<Movie> worstMoviesAllTime = mMovieDAO.getMostDislikedMovies(desiredSizeOfList);
        populateListView(listViewWorstMovies, worstMoviesAllTime);
        viewTracker.setCurrentCommunityTab("worstMovies");
    }
    public void setTopMoviesByYearAsCurrentView(){
        List<String> yearList = generateYearList();
        populateListWithYears(listViewTopMoviesByYear,yearList);
        viewTracker.setCurrentCommunityTab("topMoviesByYear");
    }

    //Maybe better with an ENUM solution
    public void determineCurrentView(){

        String currentTab = viewTracker.getCurrentCommunityTab();

        if(currentTab.equals("topMovies")){
            setTopMoviesAsCurrentView();
        }
        else if (currentTab.equals("worstMovies")) {
            setWorstMoviesAsCurrentView();
        }
        else{
            setTopMoviesByYearAsCurrentView();
        }
    }

    public void populateListWithYears(ListView listView, List<String> yearList){


        if (this.getActivity() != null) {

            int defaultLayout = android.R.layout.simple_list_item_1; //default
            ListAdapter yearAdapter = new ArrayAdapter(getActivity(), defaultLayout, yearList);
            listView.setAdapter(yearAdapter);

            isYearListCurrent = true;

        }
    }

    public void populateListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass) {

        //--------------------------------
        ExecutionTimeLogger executionTimeLogger = new ExecutionTimeLogger();
        executionTimeLogger.startTimer();
        db.beginTransaction();
        db.setTransactionSuccessful();
        //--------------------------------

        //Code for populating elements in the listView;
        ListAdapter adapter = new MovieAdapter(getActivity(), listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);

        //--------------------------------
        db.endTransaction();
        Log.d("EXECUTION TIME FOR LW:", listOfViewCellsWeGotFromHelpClass.size() + "");
        executionTimeLogger.stopTimerAndLogResults();
        //--------------------------------

    }

    public List<String> generateYearList(){

        List<String> years = new ArrayList<>();

        for (int i = thisYear; i >= 1900; i--){
            years.add(String.valueOf(i));

        }
        return years;
    }

    public void setUpListeners() {

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
                if (isYearListCurrent) {

                    //get users input
                    String selectedYearAsString = (String) listViewTopMoviesByYear.getItemAtPosition(position);
                    int chosenYear = Integer.parseInt(selectedYearAsString);

                    //get the MovieList for the year in question
                    List<Movie> topMoviesByYear = mMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, chosenYear);


                    //populate the list and set isYearListCurrent to false
                    populateListView(listViewTopMoviesByYear, topMoviesByYear);
                    isYearListCurrent = false;


                    if(topMoviesByYear.size() != 0) {
                        //populate the list and set isYearListCurrent to false
                        populateListView(listViewTopMoviesByYear, topMoviesByYear);
                        isYearListCurrent = false;
                    }
                    else{
                        //Display a message to user why no movies show up for specified year
                        Toast message = Toast.makeText(getActivity(), "No movies in database for that year", Toast.LENGTH_SHORT);
                        message.show();
                    }

                }
                else{

                    //in that case - we are presently at the specific year movie list:
                    //TODO: detailed view of the movie
                }
            }
        });
    }
}




