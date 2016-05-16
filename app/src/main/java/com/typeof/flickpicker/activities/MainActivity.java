package com.typeof.flickpicker.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Button;
import android.widget.TextView;

import com.typeof.flickpicker.R;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    TabHost tabHost;
    public static FragmentManager fragmentManager;
    public static Bundle savedFragments;
    public static Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView myProfileIcon = (TextView)findViewById(R.id.myProfileIcon);
        TextView userScore = (TextView) findViewById(R.id.userScore);

        myProfileIcon.setTypeface(font);
        userScore.setText(String.valueOf(App.getCurrentUser().getScore()));


        fragmentManager = getFragmentManager();

        App.getDatabase().seedDatabase();

        myProfileIcon.setTypeface(font);
        fragmentManager = getFragmentManager();
        tabHost = (TabHost) findViewById(R.id.tabHost);
        if (tabHost != null) {
            tabHost.setup();
        }
        configureTabs();
    }

    public void configureTabs() {

        final TabHost.TabSpec mTabSpecRecommendations = tabHost.newTabSpec("Recommendations");
        mTabSpecRecommendations.setContent(R.id.tabRecommendations);
        mTabSpecRecommendations.setIndicator("R");
        tabHost.addTab(mTabSpecRecommendations);

        final TabHost.TabSpec mTabSpecCommunity = tabHost.newTabSpec("Community");
        mTabSpecCommunity.setContent(R.id.tabCommunity);
        mTabSpecCommunity.setIndicator("C");
        tabHost.addTab(mTabSpecCommunity);

        final TabHost.TabSpec mTabSpecFriendsActivities = tabHost.newTabSpec("Friends");
        mTabSpecFriendsActivities.setContent(R.id.tabFriendsActivities);
        mTabSpecFriendsActivities.setIndicator("F");
        tabHost.addTab(mTabSpecFriendsActivities);

        final TabHost.TabSpec mTabSpecMyMovies = tabHost.newTabSpec("MyCollection");
        mTabSpecMyMovies.setContent(R.id.tabMyMovies);
        mTabSpecMyMovies.setIndicator("M");
        tabHost.addTab(mTabSpecMyMovies);

        final TabHost.TabSpec mTabSpecSearch = tabHost.newTabSpec("Search");
        mTabSpecSearch.setContent(R.id.tabSearch);
        mTabSpecSearch.setIndicator("S");
        tabHost.addTab(mTabSpecSearch);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

            if (tabId.equals("Recommendations")) {
                RecommendationsFragment recommendationsFragment = new RecommendationsFragment();
                loadFragment(recommendationsFragment, "recommendations");
            }
            if (tabId.equals("Community")) {
                loadFragment(new CommunityFragment(), "community");
            }
            if (tabId.equals("MyCollection")) {
                MyCollectionFragment myColletionFragment = new MyCollectionFragment();
                //loadFragment(myColletionFragment, "myCollection");
            }
            if (tabId.equals("Search")) {
                SearchFragment searchFragment = new SearchFragment();
                //loadFragment(searchFragment, "search");
            }
            if (tabId.equals("Friends")) {
                FriendsFragment friendsFragment = new FriendsFragment();
                //loadFragment(friendsFragment, "friends");
            }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void loadFragment(Fragment fragment, String fragmentTag) {
        // See if the fragment has been loaded before
        try {
            currentFragment = fragment;

            // Replace the last fragment with the new one
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.add(R.id.contentWrap, fragment, fragmentTag);

            //fragmentTransaction.add(fragment, fragmentTag);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();

            // Save the fragment in the bundle
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.v("Activity", "Saved Main Activity");
    }
}
