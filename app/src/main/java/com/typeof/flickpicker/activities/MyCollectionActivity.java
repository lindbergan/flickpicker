package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-05.
 */

public class MyCollectionActivity extends AppCompatActivity {

    private TabHost mTabHostMyCollection;
    private ListView listViewMyCollection;
    private ListView listViewMyPlaylist;
    private MovieDAO mMovieDAO;
    private int desireSizeOfList = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        mMovieDAO = App.getMovieDAO();

        //reboot the database
        App.getDatabase().dropTables();
        App.getDatabase().setUpTables();

        //Feed the database with dummy Data
        SeedData.seedMyCollectionData();

        //Hook up views (Buttons, TextFields Cells etc...)
        hookUpViews();

        //Connect the listeners to the relevant views
        setUpListeners();
    }

    public void hookUpViews(){

        //Configure the tabs
        configureTabs();

        listViewMyCollection = (ListView) findViewById(R.id.listViewMyCollection);
        listViewMyPlaylist = (ListView) findViewById(R.id.listViewMyPlaylist);
    }

    public void configureTabs(){
        mTabHostMyCollection = (TabHost) findViewById(R.id.tabHostMyCollection);
        mTabHostMyCollection.setup();

        final TabHost.TabSpec mTabSpecMyCollectionTab = mTabHostMyCollection.newTabSpec("myCollection");
        mTabSpecMyCollectionTab.setContent(R.id.tabMyCollection);
        mTabSpecMyCollectionTab.setIndicator("my Collection");
        mTabHostMyCollection.addTab(mTabSpecMyCollectionTab);

        final TabHost.TabSpec mTabSpecMyPlaylistTab = mTabHostMyCollection.newTabSpec("myPlaylist");
        mTabSpecMyPlaylistTab.setContent(R.id.tabMyPlaylist);
        mTabSpecMyPlaylistTab.setIndicator("my Playlist");
        mTabHostMyCollection.addTab(mTabSpecMyPlaylistTab);

        mTabHostMyCollection.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if(tabId == mTabSpecMyCollectionTab.getTag()){

                    List<Movie> userMovieCollection = mMovieDAO.getUsersMovieCollection(desireSizeOfList, SeedData.getUserId());
                    populateListView(listViewMyCollection, userMovieCollection);
                }
                else{

                    //get user's playlists, loop through them and save the movies in them to a new array:
                    List<Playlist> usersPlaylist = App.getPlaylistDAO().getUserPlaylists(SeedData.getUserId());

                    List<Movie> usersPlaylistMovies = new ArrayList<Movie>();
                    Iterator<Playlist> playlistIterator = usersPlaylist.iterator();

                    while(playlistIterator.hasNext()){

                        Playlist currentPlaylist = playlistIterator.next();

                        for(int i = 0; i < currentPlaylist.getMovieIds().size(); i++){

                            long movieId = currentPlaylist.getMovieIds().get(i).longValue();
                            usersPlaylistMovies.add(mMovieDAO.findMovie(movieId));
                        }
                    }

                    //...send that array along with the specified listview to populate it
                    populateListView(listViewMyPlaylist, usersPlaylistMovies);
                }

            }
        });
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
        ListAdapter adapter = new MovieAdapter(this,listOfViewCellsWeGotFromHelpClass.toArray());
        listView.setAdapter(adapter);
    }

}
