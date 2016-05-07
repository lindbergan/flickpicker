package com.typeof.flickpicker.activities;

import android.content.Intent;

import android.content.res.Resources;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TabHost;
import android.widget.TextView;

import android.widget.Button;


import com.typeof.flickpicker.R;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {



    TabHost tabHost;


    private Button mButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        configureTabs();
        //hookUpViews();

        //setUpClickListeners();

    }


    public void configureTabs() {


        final TabHost.TabSpec mTabSpecRecommendations = tabHost.newTabSpec("Recommendations");
        mTabSpecRecommendations.setContent(R.id.tabRecommendations);
        mTabSpecRecommendations.setIndicator("Recommendations");
        tabHost.addTab(mTabSpecRecommendations);

        final TabHost.TabSpec mTabSpecCommunity = tabHost.newTabSpec("Community");
        mTabSpecCommunity.setContent(R.id.tabCommunity);
        mTabSpecCommunity.setIndicator("Community");
        tabHost.addTab(mTabSpecCommunity);

        final TabHost.TabSpec mTabSpecFriendsActivities = tabHost.newTabSpec("FriendsActivities");
        mTabSpecFriendsActivities.setContent(R.id.tabFriendsActivities);
        mTabSpecFriendsActivities.setIndicator("Friends' activities");
        tabHost.addTab(mTabSpecFriendsActivities);

        final TabHost.TabSpec mTabSpecMyMovies = tabHost.newTabSpec("MyMovies");
        mTabSpecMyMovies.setContent(R.id.tabMyMovies);
        mTabSpecMyMovies.setIndicator("My Movies");
        tabHost.addTab(mTabSpecMyMovies);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if(tabId != tabHost.getCurrentTabTag()){

                    if(tabId == "Recommendations"){
                        startActivity(new Intent(getApplicationContext(), recommendationsActivity.class));
                    }
                    if(tabId == "Community"){
                        startActivity(new Intent(getApplicationContext(), CommunityActivity.class));
                    }

                    }



            }
        });
    }


    public void hookUpViews(){
        mButton = (Button) findViewById(R.id.changeViewButton);
        mButton.setText("ToCommunityTab");
    }

    public void setUpClickListeners(){
        //setup listeners here
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CommunityActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Unnecessary since we don't have a action bar???
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
