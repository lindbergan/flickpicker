package com.typeof.flickpicker.activities;

import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.MovieDAO;
import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-05.
 */

    //TODO: Fix the namingConvention for the XML-files
    //TODO: Fix the database loadtime latency
    //TODO: Fix a good solution on how to track which tab user was previously at when returning from another "outer tab" (avoid SingletonPattern)
    //TODO: Replace notes with real JavaDoc
    //TODO: different cellCalls (users rating, NOT community) BUT playlist want communityRating (that is - the present MovieCell)
    //TODO: Think about how the user should "go back" to the "year" screen [CommunityTab - TopMoviesByYear]


public class MyCollectionFragment extends Fragment {

    private TabHost mTabHostMyCollection;
    private ListView listViewMyCollection;
    private ListView listViewMyPlaylist;
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
        return myCollectionView;
    }

    public void hookUpViews(View view){
        listViewMyCollection = (ListView) view.findViewById(R.id.listViewMyCollection);
        listViewMyPlaylist = (ListView) view.findViewById(R.id.listViewMyPlaylist);

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
        mTabSpecMyPlaylistTab.setIndicator("My Playlist");
        mTabHostMyCollection.addTab(mTabSpecMyPlaylistTab);

        mTabHostMyCollection.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if(tabId.equals("myPlaylist")){
                    populatePlaylist();
                }
                else{
                    populateCollection();
                }

            }
        });
    }

    public void populatePlaylist() {
        List<Movie> userMovieCollection = mMovieDAO.getMovieCollectionFromUserId(desireSizeOfList, App.getCurrentUser().getId());
        populateListView(listViewMyCollection, userMovieCollection);
    }

    public void populateCollection() {
        Playlist usersPlaylist = App.getPlaylistDAO().getPlaylist(App.getCurrentUser().getId());

        List<Movie> usersPlaylistMovies = new ArrayList<>();

        for(int i = 0; i < usersPlaylist.getMovieIds().size(); i++){
            long movieId = usersPlaylist.getMovieIds().get(i).longValue();
            usersPlaylistMovies.add(mMovieDAO.findMovie(movieId));
        }

        //...send that array along with the specified listview to populate it
        populateListView(listViewMyPlaylist, usersPlaylistMovies);
    }

    public void setUpListeners(){

        listViewMyCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Movie Detailed view
            }
        });
        listViewMyPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Movie Detailed view
            }
        });
    }

    public void populateListView(ListView listView, List<Movie> listOfViewCellsWeGotFromHelpClass){

        //Code for populating elements in the listView;
        ListAdapter adapter = new MovieAdapter(getActivity(),listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);

        //TODO: Different cells for different tab
    }

}
