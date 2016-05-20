package com.typeof.flickpicker.activities;

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
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-05.
 */

public class MyCollectionFragment extends Fragment {

    private TabHost mTabHostMyCollection;
    private ListView listViewMyCollection;
    private ListView listViewMyWatchlist;
    private SearchView mSearchViewCollection;
    private SearchView mSearchViewWatchlist;
    private int desireSizeOfList = 1000;
    private TextView hiddenCollectionText;
    private TextView hiddenWatchlistText;
    private List<Movie> mWatchlist;
    private List<Movie> mCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        mCollection = App.getMovieDAO().getMovieCollectionFromUserId(desireSizeOfList, App.getCurrentUser().getId());

        mWatchlist = new ArrayList<>();
        Playlist p = App.getPlaylistDAO().getPlaylist(App.getCurrentUser().getId());
        for (Number i : p.getMovieIds()) {
            mWatchlist.add(App.getMovieDAO().findMovie(i.intValue()));
        }

        populateCollectionListView(listViewMyCollection, mCollection);
        populateWatchlistListView(listViewMyWatchlist, mWatchlist);

        return myCollectionView;
    }

    public void hookUpViews(View view){
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
        mTabSpecMyPlaylistTab.setContent(R.id.tabMyPlaylist);
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
        mTabHostMyCollection.getTabWidget().getChildAt(mTabHostMyCollection.getCurrentTab()).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    public void setUpListeners(){

        mSearchViewCollection.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String s) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        List<Movie> result = new ArrayList<>();
                        for (Movie m : mCollection) {

                            String title = m.getTitle().toLowerCase();
                            String searchText = mSearchViewCollection.getQuery().toString().toLowerCase();

                            if (title.contains(searchText)) {
                                result.add(m);
                            }
                        }

                        populateCollectionListView(listViewMyCollection, result);
                    }
                }, 1000);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
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

                        populateWatchlistListView(listViewMyWatchlist, result);
                    }
                }, 1000);
                return false;
            }
        });
    }

    public void populateCollectionListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass){
        ListAdapter adapter = new MovieAdapter(getActivity(),listOfViewCellsWeGotFromHelpClass.toArray());
        if (hiddenCollectionText.getVisibility() == View.VISIBLE) hiddenCollectionText.setVisibility(View.INVISIBLE);
        if (listOfViewCellsWeGotFromHelpClass.size() == 0) {
            hiddenCollectionText.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(adapter);
    }

    public void populateWatchlistListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass){
        ListAdapter adapter = new MovieAdapter(getActivity(),listOfViewCellsWeGotFromHelpClass.toArray());
        if (hiddenWatchlistText.getVisibility() == View.VISIBLE) hiddenWatchlistText.setVisibility(View.INVISIBLE);
        if (listOfViewCellsWeGotFromHelpClass.size() == 0) {
            hiddenWatchlistText.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(adapter);
    }

}
