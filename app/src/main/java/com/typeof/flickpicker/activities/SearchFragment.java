package com.typeof.flickpicker.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.Toast;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.sql.MovieTable;
import com.typeof.flickpicker.database.sql.UserTable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private TabHost mTabHostSearch;
    private ListView listViewSearchMovies;
    private ListView listViewSearchUser;
    private SearchView mSearchViewMovie;
    private SearchView mSearchViewFriend;
    private TextView hiddenMoviesText;
    private TextView hiddenUsersText;

    private List<Movie> mMovieResults;
    private List<User> mUserResults;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View searchView = inflater.inflate(R.layout.activity_search, container, false);
        hookUpViews(searchView);
        configureTabs(searchView);
        setUpListeners();
        return searchView;
    }

    public void hookUpViews(View view){
        listViewSearchMovies = (ListView) view.findViewById(R.id.listViewSearchMovie);
        listViewSearchUser = (ListView) view.findViewById(R.id.listViewSearchFriend);
        mSearchViewMovie = (SearchView) view.findViewById(R.id.searchViewMovie);
        mSearchViewFriend = (SearchView) view.findViewById(R.id.searchViewFriend);
        hiddenMoviesText = (TextView) view.findViewById(R.id.hiddenNoMoviesText);
        hiddenUsersText = (TextView) view.findViewById(R.id.hiddenNoUsersText);

        initResults();
        populateMovieListView(listViewSearchMovies, mMovieResults);
        populateUserListView(listViewSearchUser, mUserResults);

    }

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

        setActiveTabColor();//default

        //NOTE: only needed if we want to return to specific tab and have previous search result displayed:
        mTabHostSearch.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setActiveTabColor();
            }
        });

    }

    public void setActiveTabColor(){
        mTabHostSearch.getTabWidget().getChildAt(mTabHostSearch.getCurrentTab()).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    public void setUpListeners(){
        mSearchViewMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String s) {

                if (s == null) {
                    s = "";
                }

                List<Movie> popList = new ArrayList<>();

                for (Movie m : mMovieResults) {

                    String movieTitle = m.getTitle().toLowerCase();
                    String searchText = s.toLowerCase();

                    if (movieTitle.contains(searchText)) {
                        popList.add(m);
                    }
                }

                populateMovieListView(listViewSearchMovies, popList);

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

                if (s == null) {
                    s = "";
                }

                List<User> popList = new ArrayList<>();

                for (User u : mUserResults) {

                    String userName = u.getUsername().toLowerCase();
                    String searchText = s.toLowerCase();

                    if (userName.contains(searchText)) {
                        popList.add(u);
                    }
                }

                populateUserListView(listViewSearchUser, popList);

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

        });
    }

    public void populateMovieListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass){
        ListAdapter adapter = new MovieAdapter(getActivity(),listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);
    }

    public void populateUserListView(ListView listView, List<User> listOfViewCellsWeGotFromHelpClass){
        ListAdapter adapter = new UserAdapter(getActivity(),listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);
    }

    public void initResults() {
        mMovieResults = App.getMovieDAO().searchMovieBy(MovieTable.MovieEntry.COLUMN_NAME_TITLE, "");
        mUserResults = App.getUserDAO().searchUser(UserTable.UserEntry.COLUMN_NAME_USERNAME, "");
    }

}
