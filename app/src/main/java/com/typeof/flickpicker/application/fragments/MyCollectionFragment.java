package com.typeof.flickpicker.application.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.helpers.KeyboardHelper;
import com.typeof.flickpicker.application.adapters.MovieAdapter;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.sql.tables.MovieTable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
/**
 * MyCollectionFragment extends Fragment
 * Used for showing the user its rated movies
 */

public class MyCollectionFragment extends Fragment implements PropertyChangeListener {

    // Views

    private TabHost mTabHostMyCollection;
    private ListView listViewMyCollection;
    private ListView listViewMyWatchlist;
    private SearchView mSearchViewCollection;
    private SearchView mSearchViewWatchlist;
    private TextView hiddenCollectionText;
    private TextView hiddenWatchlistText;
    private Context ctx;

    // Fields

    private final int desireSizeOfList = 1000;
    private List<Movie> mWatchlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_collection, container, false);

        ctx = getActivity();

        initViews(view);
        configureTabs(view);
        setUpListeners();

        KeyboardHelper keyboardHelper = new KeyboardHelper(getActivity(), getContext());
        keyboardHelper.setupUI(view);

        populateData();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return view;
    }

    private void populateData() {
        List<Movie> collection = App.getMovieDAO().getMovieCollectionFromUserId(desireSizeOfList, App.getCurrentUser().getId());

        // Finds the current users watchlist
        mWatchlist = new ArrayList<>();
        Playlist p = App.getPlaylistDAO().getUserPlaylist(App.getCurrentUser().getId());
        if (p != null) {
            for (Number i : p.getMovieIds()) {
                mWatchlist.add(App.getMovieDAO().findMovie(i.intValue()));
            }
        }
        populateCollection(listViewMyCollection, collection);
        populateWatchlist(listViewMyWatchlist, mWatchlist);
    }

    private void initViews(View view){
        listViewMyCollection = (ListView) view.findViewById(R.id.listViewMyCollection);
        listViewMyWatchlist = (ListView) view.findViewById(R.id.listViewMyPlaylist);
        mSearchViewCollection = (SearchView) view.findViewById(R.id.searchView);
        mSearchViewWatchlist = (SearchView) view.findViewById(R.id.searchViewPlaylist);
        hiddenCollectionText = (TextView) view.findViewById(R.id.hiddenNoCollectionText);
        hiddenWatchlistText = (TextView) view.findViewById(R.id.hiddenNoWatchlistText);
    }

    private void configureTabs(View view){
        mTabHostMyCollection = (TabHost) view.findViewById(R.id.tabHostMyCollection);
        mTabHostMyCollection.setup();

        final TabHost.TabSpec mTabSpecMyCollectionTab = mTabHostMyCollection.newTabSpec("myCollection");
        mTabSpecMyCollectionTab.setContent(R.id.tabMyCollection);
        mTabSpecMyCollectionTab.setIndicator("My Collection");
        mTabHostMyCollection.addTab(mTabSpecMyCollectionTab);

        final TabHost.TabSpec mTabSpecMyPlaylistTab = mTabHostMyCollection.newTabSpec("myPlaylist");
        mTabSpecMyPlaylistTab.setContent(R.id.tabMyWatchlist);
        mTabSpecMyPlaylistTab.setIndicator("Watchlist");
        mTabHostMyCollection.addTab(mTabSpecMyPlaylistTab);

        setActiveTabColor();

        mTabHostMyCollection.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            setActiveTabColor();
            }
        });
    }

    private void setActiveTabColor(){
        mTabHostMyCollection.getTabWidget().getChildAt(mTabHostMyCollection.getCurrentTab()).getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.active_tab_collection_filter)
                , PorterDuff.Mode.MULTIPLY);
    }

    /**
     * onQueryTextChange methods is used to filter out movies in Collection and Watchlist
     * Handler class is used to create a delay
     * Waits for the user to finish typing before searching for movies to minimize amount of searches
     */

    private void setUpListeners(){

        mSearchViewCollection.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                MovieDAO movieDAO = App.getMovieDAO();

                if (query != null) {
                    String searchText = query.toLowerCase();
                    List<Movie> movieList = movieDAO.searchMovieBy(MovieTable.MovieEntry.COLUMN_NAME_TITLE,
                            searchText);

                    populateCollection(listViewMyCollection, movieList);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (newText == null || newText.equalsIgnoreCase("")) {
                            populateCollection(listViewMyCollection,
                                    App.getMovieDAO().getMovieCollectionFromUserId(desireSizeOfList,
                                            App.getCurrentUser().getId()));
                        }

                    }
                }, 1000);
                return false;
            }
        });

        mSearchViewWatchlist.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        List<Movie> result = new ArrayList<>();
                        for (Movie m : mWatchlist) {

                            String title = m.getTitle().toLowerCase();
                            String searchText = mSearchViewWatchlist.getQuery().toString().toLowerCase();

                            if (title.contains(searchText)) {
                                result.add(m);
                            }
                        }

                        populateWatchlist(listViewMyWatchlist, result);
                    }
                }, 1000);
                return false;
            }
        });
    }

    /**
     * Populate methods uses MovieAdapter that extends CustomAdapter
     * @param listView  ListView element
     * @param movieList List of movies
     */

    private void populateCollection(ListView listView, List<Movie> movieList){

        ListAdapter adapter = new MovieAdapter(ctx,movieList.toArray());

        if (hiddenCollectionText.getVisibility() == View.VISIBLE) hiddenCollectionText.setVisibility(View.INVISIBLE);

        if (movieList.size() == 0) {
            hiddenCollectionText.setVisibility(View.VISIBLE);
        }

        listView.setAdapter(adapter);
    }

    private void populateWatchlist(ListView listView, List<Movie> movieList){
        ListAdapter adapter = new MovieAdapter(ctx,movieList.toArray());

        if (hiddenWatchlistText.getVisibility() == View.VISIBLE) hiddenWatchlistText.setVisibility(View.INVISIBLE);

        if (movieList.size() == 0) {
            hiddenWatchlistText.setVisibility(View.VISIBLE);
        }

        listView.setAdapter(adapter);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("playlist_changed") || event.getPropertyName().equals("ratings_changed")) {
            populateData();
        }
    }
}
