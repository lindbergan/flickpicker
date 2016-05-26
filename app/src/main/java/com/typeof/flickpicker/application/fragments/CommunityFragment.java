package com.typeof.flickpicker.application.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.adapters.MovieAdapter;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * CommunityFragment
 *
 * A controller class for the community view
 */

public class CommunityFragment extends Fragment {

    private int desiredSizeOfList = 10;
    MovieDAO mMovieDAO;
    TabHost mTabHost;
    private ListView listViewTopMovies;
    private ListView listViewWorstMovies;
    private ListView listViewTopMoviesByYear;
    private int thisYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieDAO = App.getMovieDAO();
        thisYear = Calendar.getInstance().get(Calendar.YEAR);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View communityView = inflater.inflate(R.layout.activity_community, container, false);
        hookUpViews(communityView);
        configureTabs(communityView);
        setUpListeners();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return communityView;
    }

    //Setup the views in the corresponding XML-file
    public void hookUpViews(View view) {
        listViewTopMovies = (ListView) view.findViewById(R.id.listViewTopMovies);
        listViewWorstMovies = (ListView) view.findViewById(R.id.listViewWorstMovies);
        listViewTopMoviesByYear = (ListView) view.findViewById(R.id.listViewTopMoviesByYear);
    }

    //Configures the fragment's tabs. Set the names, a marker for which tab is currently active and
    //connects listeners for tab changes.
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

        //Sets TopMovies as the default tab the first time the fragment is created
        setTopMoviesAsCurrentView();
        //Sets an underline at that tab the first time the fragment is created
        setActiveTabColor();

        //Sets a listener to the tabhost in order to display and set the underline to the correct tab
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                setActiveTabColor();

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
    }
    public void setWorstMoviesAsCurrentView(){
        List<Movie> worstMoviesAllTime = mMovieDAO.getMostDislikedMovies(desiredSizeOfList);
        populateListView(listViewWorstMovies, worstMoviesAllTime);
    }
    public void setTopMoviesByYearAsCurrentView(){
        List<String> yearList = generateYearList();
        populateListWithYears(listViewTopMoviesByYear,yearList);
    }

    //Sets an underline at the current tab for easier navigation
    public void setActiveTabColor(){
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    //Populate the listView with a list of years (TextFields) with the help of the default ArrayAdapter
    public void populateListWithYears(ListView listView, List<String> yearList){

        if (this.getActivity() != null) {

            int defaultLayout = android.R.layout.simple_list_item_1; //default
            ListAdapter yearAdapter = new ArrayAdapter(getActivity(), defaultLayout, yearList);
            listView.setAdapter(yearAdapter);
        }
    }

    //Populate the listView with a list of movies with the help of the custom MovieAdapter
    public void populateListView(ListView listView, List<Movie> movieList) {

        ListAdapter adapter = new MovieAdapter(getActivity(), movieList.toArray());
        listView.setAdapter(adapter);
    }

    //Generate a list of years covering the 20th century up until today
    public List<String> generateYearList(){

        List<String> years = new ArrayList<>();

        for (int i = thisYear; i >= 1900; i--){
            years.add(String.valueOf(i));
        }
        return years;
    }

    //Set up a listener for the yearList in order to access the correct year and to display the movies
    //from that year in decending order in terms of rating
    public void setUpListeners() {
        listViewTopMoviesByYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //get users input
                    String selectedYearAsString = (String) listViewTopMoviesByYear.getItemAtPosition(position);
                    int chosenYear = Integer.parseInt(selectedYearAsString);

                    //get the MovieList for the year in question
                    List<Movie> topMoviesByYear = mMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOfList, chosenYear);


                    if(topMoviesByYear.size() != 0) {
                        populateListView(listViewTopMoviesByYear, topMoviesByYear);
                    }
                    else{
                        //Display a message to user why no movies show up for specified year
                        Toast message = Toast.makeText(getActivity(), "No movies in database for that year", Toast.LENGTH_SHORT);
                        message.show();
                    }
            }
        });
    }
}




