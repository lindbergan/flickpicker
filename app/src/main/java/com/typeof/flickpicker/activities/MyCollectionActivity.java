package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 2016-05-05.
 */

    //Ide:
    //Two tabs
    // ---tab 1:---
    // a searchView in the header to add movies
    // a listView to show all movies in my collection
    // ---tab2:----
    // "To-watch-list", a list of your playlist

public class MyCollectionActivity extends AppCompatActivity {

    private TabHost mTabHostMyCollection;
    private ListView listViewMyCollection;
    private ListView listViewMyPlaylist;
    private MovieDAO mMovieDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        mMovieDAO = App.getMovieDAO();

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

                    //TODO: need to create method getMyMovieCollection(int max) in SQLMovieDAO that return all movies that ive seen;
                    //List<Movie> ratedByMe = mSQLMovieDAO.getMyMovieCollection(desiredSizeOfList);
                    //call MovieCell.createMovieView()(...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies) --> returns listOfViewCellsWeGotFromHelpClass
                    //Add each one of the Vies to created ListCell


                    //Test to fill the listView with strings:
                    List<String> dummyStringList = new ArrayList<String>();
                    dummyStringList.add("BraveHeart");
                    dummyStringList.add("Gone with the Wind");
                    dummyStringList.add("Jurassic Park");
                    dummyStringList.add("Back to the Future II");
                    dummyStringList.add("Karate Kid");
                    dummyStringList.add("Oblivion");

                    populateListView(listViewMyCollection, dummyStringList);

                }
                else{

                    //TODO:
                    //List<Movie> myPlayList = mSQLPlaylistDAO.getUserPlaylists(long userId);
                    //call MovieCell.createMovieView() class (...should return a list of Views of the same size as desiredSizeOfList: Arguments = List <Movie> movies)
                    //Add each one of the Vies to created ListCell

                    List<String> dummyStringList = new ArrayList<String>();
                    dummyStringList.add("Sharknado");
                    dummyStringList.add("Scream IV");
                    dummyStringList.add("Jurassic World");

                    populateListView(listViewMyPlaylist, dummyStringList);

                }

            }
        });

    }
    public void setUpListeners(){

    }

    public void populateListView(ListView listView, List<String> listOfViewCellsWeGotFromHelpClass){

        int layout = android.R.layout.simple_list_item_1;

        //Code for populating elements in the listView;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,layout,listOfViewCellsWeGotFromHelpClass);
        listView.setAdapter(adapter);

        //TODO: Need to write methods onClicked for the different elements in the list;

    }

}
