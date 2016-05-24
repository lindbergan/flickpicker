package com.typeof.flickpicker.application.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.helpers.KeyboardHelper;
import com.typeof.flickpicker.application.adapters.MovieAdapter;
import com.typeof.flickpicker.application.adapters.UserAdapter;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.sql.tables.MovieTable;
import com.typeof.flickpicker.database.sql.tables.UserTable;

import java.util.List;


/**
 * SearchFragment
 *
 * A controller class for the search view
 */

public class SearchFragment extends Fragment {

    private TabHost mTabHostSearch;
    private ListView listViewSearchMovies;
    private ListView listViewSearchUser;
    private SearchView mSearchViewMovie;
    private SearchView mSearchViewFriend;
    private TextView hiddenMoviesText;
    private TextView hiddenUsersText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.activity_search, container, false);
        KeyboardHelper keyboardHelper = new KeyboardHelper(getActivity(), getContext());
        keyboardHelper.setupUI(searchView.findViewById(R.id.parent));
        hookUpViews(searchView);
        configureTabs(searchView);
        setUpListeners();
        return searchView;
    }

    //Setup the views in the corresponding XML-file
    public void hookUpViews(View view){
        listViewSearchMovies = (ListView) view.findViewById(R.id.listViewSearchMovie);
        listViewSearchUser = (ListView) view.findViewById(R.id.listViewSearchFriend);
        mSearchViewMovie = (SearchView) view.findViewById(R.id.searchViewMovie);
        mSearchViewFriend = (SearchView) view.findViewById(R.id.searchViewFriend);
        hiddenMoviesText = (TextView) view.findViewById(R.id.hiddenNoMoviesText);
        hiddenUsersText = (TextView) view.findViewById(R.id.hiddenNoUsersText);

        if (hiddenMoviesText.getVisibility() == View.VISIBLE) hiddenMoviesText.setVisibility(View.INVISIBLE);
        if (hiddenUsersText.getVisibility() == View.VISIBLE) hiddenUsersText.setVisibility(View.INVISIBLE);

    }

    //Configures the fragment's tabs. Set the names, a marker for which tab is currently active and
    //connects listeners for tab changes.
    public void configureTabs(View view){

        mTabHostSearch = (TabHost) view.findViewById(R.id.tabHostSearch);
        mTabHostSearch.setup();

        final TabHost.TabSpec mTabSpecSearchMovie = mTabHostSearch.newTabSpec("searchMovie");
        mTabSpecSearchMovie.setContent(R.id.tabSearchMovie);
        mTabSpecSearchMovie.setIndicator("Search Movie");
        mTabHostSearch.addTab(mTabSpecSearchMovie);

        final TabHost.TabSpec mTabSpecSearchFriend = mTabHostSearch.newTabSpec("searchFriend");
        mTabSpecSearchFriend.setContent(R.id.tabSearchFriend);
        mTabSpecSearchFriend.setIndicator("Search User");
        mTabHostSearch.addTab(mTabSpecSearchFriend);

        //Sets an underline at the starting tab the first time the fragment is created
        setActiveTabColor();

        //Changes the underline depending on which tab is currently active
        mTabHostSearch.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setActiveTabColor();
            }
        });

    }

    //Sets an underline at the current tab for easier navigation
    public void setActiveTabColor(){
        mTabHostSearch.getTabWidget().getChildAt(mTabHostSearch.getCurrentTab()).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    //Set up listeners for the SearchViews. A handler with delay is implemented in order to give better response timing to user's input.
    public void setUpListeners(){
        mSearchViewMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Listen to key down while in the search field
            @Override
            public boolean onQueryTextChange(String s) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        populateMovieListView(listViewSearchMovies,
                                App.getMovieDAO().searchMovieBy(MovieTable.MovieEntry.COLUMN_NAME_TITLE, mSearchViewMovie.getQuery().toString()));
                    }
                }, 1000);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });


        mSearchViewFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        populateUserListView(listViewSearchUser,
                                App.getUserDAO().searchUser(UserTable.UserEntry.COLUMN_NAME_USERNAME, mSearchViewFriend.getQuery().toString()));
                    }
                }, 1000);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

        });
    }

    //Populate the listView with searchResults with the help of a custom MovieAdapter to draw the correct cells
    public void populateMovieListView(ListView listView, List<Movie> searchResults){
        ListAdapter adapter = new MovieAdapter(getActivity(),searchResults.toArray());
        if (hiddenMoviesText.getVisibility() == View.VISIBLE) hiddenMoviesText.setVisibility(View.INVISIBLE);
        if (searchResults.size() == 0) {
            hiddenMoviesText.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(adapter);
    }

    //Populate the listView with searchResults with the help of a custom UserAdapter to draw the correct cells
    public void populateUserListView(ListView listView, List<User> searchResults){
        ListAdapter adapter = new UserAdapter(getActivity(),searchResults.toArray());
        if (hiddenUsersText.getVisibility() == View.VISIBLE) hiddenUsersText.setVisibility(View.INVISIBLE);
        if (searchResults.size() == 0) {
            hiddenUsersText.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(adapter);
    }

}
