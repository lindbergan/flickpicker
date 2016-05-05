package com.typeof.flickpicker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;

import com.typeof.flickpicker.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        //Hook up views (Buttons, TextFields Cells etc...)
        hookUpViews();

        //Connect the listeners to the relevant views
        setUpListeners();
    }

    public void hookUpViews(){

        //Configure the tabs
        configureTabs();

        listViewMyCollection = (ListView) findViewById(R.id.listViewMyCollection);
        listViewMyPlaylist = (ListView) findViewById(R.id.listViewMyCollection);
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

    }
    public void setUpListeners(){}

}
