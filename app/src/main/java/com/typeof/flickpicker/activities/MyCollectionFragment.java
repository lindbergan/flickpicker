package com.typeof.flickpicker.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.sql.MovieTable;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-05.
 */

    //TODO: Fix a good solution on how to track which tab user was previously at when returning from another "outer tab" (avoid SingletonPattern)
    //TODO: Replace notes with real JavaDoc
    //TODO: different cellCalls (users rating, NOT community) BUT playlist want communityRating (that is - the present MovieCell)
    //TODO: Think about how the user should "go back" to the "year" screen [CommunityTab - TopMoviesByYear]


public class MyCollectionFragment extends Fragment {

    private TabHost mTabHostMyCollection;
    private ListView listViewMyCollection;
    private ListView listViewMyPlaylist;
    private SearchView searchViewMovie;
    private MovieDAO mMovieDAO;
    private int desireSizeOfList = 25;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieDAO = App.getMovieDAO();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myCollectionView = inflater.inflate(R.layout.activity_my_collection, container, false);
        hookUpViews(myCollectionView);
        configureTabs(myCollectionView);
        setUpListeners();

        KeyboardHelper keyboardHelper = new KeyboardHelper(getActivity(), getContext());
        keyboardHelper.setupUI(myCollectionView);

        return myCollectionView;
    }

    public void hookUpViews(View view){
        listViewMyCollection = (ListView) view.findViewById(R.id.listViewMyCollection);
        listViewMyPlaylist = (ListView) view.findViewById(R.id.listViewMyPlaylist);
        searchViewMovie = (SearchView) view.findViewById(R.id.searchView);

        populatePlaylist();
        populateCollection();

    }

    public void configureTabs(View view){
        mTabHostMyCollection = (TabHost) view.findViewById(R.id.tabHostMyCollection);
        mTabHostMyCollection.setup();

        final TabHost.TabSpec mTabSpecMyCollectionTab = mTabHostMyCollection.newTabSpec("myCollection");
        mTabSpecMyCollectionTab.setContent(R.id.tabMyCollection);
        mTabSpecMyCollectionTab.setIndicator("My Collection");
        mTabHostMyCollection.addTab(mTabSpecMyCollectionTab);

        final TabHost.TabSpec mTabSpecMyPlaylistTab = mTabHostMyCollection.newTabSpec("myPlaylist");
        mTabSpecMyPlaylistTab.setContent(R.id.tabMyPlaylist);
        mTabSpecMyPlaylistTab.setIndicator("Watchlist");
        mTabHostMyCollection.addTab(mTabSpecMyPlaylistTab);

        setActiveTabColor();//default

        mTabHostMyCollection.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setActiveTabColor();
            }
        });
    }

    public void setActiveTabColor(){
        mTabHostMyCollection.getTabWidget().getChildAt(mTabHostMyCollection.getCurrentTab()).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    public void populateCollection() {
        List<Movie> userMovieCollection = mMovieDAO.getMovieCollectionFromUserId(desireSizeOfList, App.getCurrentUser().getId());
        populateListView(listViewMyCollection, userMovieCollection);
    }

    public void populatePlaylist() {
        Playlist usersPlaylist = App.getPlaylistDAO().getPlaylist(App.getCurrentUser().getId());

        if (usersPlaylist != null) {
            List<Movie> usersPlaylistMovies = new ArrayList<>();

            for (int i = 0; i < usersPlaylist.getMovieIds().size(); i++) {
                long movieId = usersPlaylist.getMovieIds().get(i).longValue();
                usersPlaylistMovies.add(mMovieDAO.findMovie(movieId));
            }

            //...send that array along with the specified listview to populate it
            populateListView(listViewMyPlaylist, usersPlaylistMovies);
        }
    }

    public void setUpListeners(){

        searchViewMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Movie> matches = App.getMovieDAO().searchMovieBy(MovieTable.MovieEntry.COLUMN_NAME_TITLE, s);

                if(matches.size() != 0){
                    populateListView(listViewMyCollection, matches);
                }
                else{
                    Toast message = Toast.makeText(getActivity(), "No such movie in database", Toast.LENGTH_SHORT);
                    message.show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Movie> matches = App.getMovieDAO().searchMovieBy(MovieTable.MovieEntry.COLUMN_NAME_TITLE, s);
                populateListView(listViewMyCollection, matches);
                return true;
            }
        });
    }

    public void populateListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass){

        //Code for populating elements in the listView;

            ListAdapter adapter = new MovieAdapter(getActivity(), listOfViewCellsWeGotFromHelpClass.toArray());
            listView.setAdapter(adapter);

        //TODO: Different cells for different tab
    }

}
