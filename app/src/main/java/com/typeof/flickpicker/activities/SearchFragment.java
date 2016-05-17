package com.typeof.flickpicker.activities;

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

        populateMovieListView(listViewSearchMovies, App.getMovieDAO().getCommunityTopPicks(25));
        populateUserListView(listViewSearchUser, App.getUserDAO().searchUser(UserTable.UserEntry.COLUMN_NAME_USERNAME, ""));

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
    }

    public void setUpListeners(){

        mSearchViewMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                if (s != null) {
                    if (hiddenMoviesText.getVisibility() == View.VISIBLE) hiddenMoviesText.setVisibility(View.INVISIBLE);
                    List<Movie> matches = App.getMovieDAO().searchMovieBy(MovieTable.MovieEntry.COLUMN_NAME_TITLE, s);

                    if (matches.size() == 0) {
                        hiddenMoviesText.setVisibility(View.VISIBLE);
                    }
                    populateMovieListView(listViewSearchMovies, matches);
                }
                else {
                    populateMovieListView(listViewSearchMovies, App.getMovieDAO().searchMovieBy(MovieTable.MovieEntry.COLUMN_NAME_TITLE, ""));
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {return false;}
        });


        mSearchViewFriend.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                if (s != null) {
                    if (hiddenUsersText.getVisibility() == View.VISIBLE) hiddenUsersText.setVisibility(View.INVISIBLE);
                    List<User> matches = App.getUserDAO().searchUser(UserTable.UserEntry.COLUMN_NAME_USERNAME, s);

                    if (matches.size() == 0) {
                        hiddenUsersText.setVisibility(View.VISIBLE);
                    }
                    populateUserListView(listViewSearchMovies, matches);
                }
                else {
                    populateUserListView(listViewSearchMovies, App.getUserDAO().searchUser(UserTable.UserEntry.COLUMN_NAME_USERNAME, ""));
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String s) {return false;}
        });
    }

    public void populateMovieListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass){

        //Code for populating elements in the listView;

        ListAdapter adapter = new MovieAdapter(getActivity(),listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);
    }

    public void populateUserListView(ListView listView, List<User> listOfViewCellsWeGotFromHelpClass){

        ListAdapter adapter = new UserAdapter(getActivity(),listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);
    }

}
