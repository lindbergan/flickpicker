package com.typeof.flickpicker.application.fragments;

import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-05.
 */

/**
 * CollectionFragment extends Fragment
 * Used for showing the user its rated movies
 */

public class CollectionFragment extends Fragment {

    // Views

    private TabHost mTabHostMyCollection;
    private ListView listViewMyCollection;
    private ListView listViewMyWatchlist;
    private SearchView mSearchViewCollection;
    private SearchView mSearchViewWatchlist;
    private TextView hiddenCollectionText;
    private TextView hiddenWatchlistText;

    // Fields

    private int desireSizeOfList = 1000;
    private List<Movie> mWatchlist;
    private List<Movie> mCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_collection, container, false);

        initViews(view);
        configureTabs(view);
        setUpListeners();

        KeyboardHelper keyboardHelper = new KeyboardHelper(getActivity(), getContext());
        keyboardHelper.setupUI(view);

        mCollection = App.getMovieDAO().getMovieCollectionFromUserId(desireSizeOfList, App.getCurrentUser().getId());

        // Finds the current users watchlist

        mWatchlist = new ArrayList<>();
        Playlist p = App.getPlaylistDAO().getUserPlaylist(App.getCurrentUser().getId());
        if (p != null) {
            for (Number i : p.getMovieIds()) {
                mWatchlist.add(App.getMovieDAO().findMovie(i.intValue()));
            }
        }
        populateCollection(listViewMyCollection, mCollection);
        populateWatchlist(listViewMyWatchlist, mWatchlist);

        return view;
    }

    public void initViews(View view){
        listViewMyCollection = (ListView) view.findViewById(R.id.listViewMyCollection);
        listViewMyWatchlist = (ListView) view.findViewById(R.id.listViewMyPlaylist);
        mSearchViewCollection = (SearchView) view.findViewById(R.id.searchView);
        mSearchViewWatchlist = (SearchView) view.findViewById(R.id.searchViewPlaylist);
        hiddenCollectionText = (TextView) view.findViewById(R.id.hiddenNoCollectionText);
        hiddenWatchlistText = (TextView) view.findViewById(R.id.hiddenNoWatchlistText);
    }

    public void configureTabs(View view){
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

    public void setActiveTabColor(){
        mTabHostMyCollection.getTabWidget().getChildAt(mTabHostMyCollection.getCurrentTab()).getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.active_tab_collection_filter)
                , PorterDuff.Mode.MULTIPLY);
    }

    /**
     * onQueryTextChange methods is used to filter out movies in Collection and Watchlist
     * Handler class is used to create a delay
     * Waits for the user to finish typing before searching for movies to minimize amount of searches
     */

    public void setUpListeners(){

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
     * @param listView
     * @param movieList
     */

    public void populateCollection(ListView listView, List<Movie> movieList){
        ListAdapter adapter = new MovieAdapter(getActivity(),movieList.toArray());

        if (hiddenCollectionText.getVisibility() == View.VISIBLE) hiddenCollectionText.setVisibility(View.INVISIBLE);

        if (movieList.size() == 0) {
            hiddenCollectionText.setVisibility(View.VISIBLE);
        }

        listView.setAdapter(adapter);
    }

    public void populateWatchlist(ListView listView, List<Movie> movieList){
        ListAdapter adapter = new MovieAdapter(getActivity(),movieList.toArray());

        if (hiddenWatchlistText.getVisibility() == View.VISIBLE) hiddenWatchlistText.setVisibility(View.INVISIBLE);

        if (movieList.size() == 0) {
            hiddenWatchlistText.setVisibility(View.VISIBLE);
        }

        listView.setAdapter(adapter);
    }

}
