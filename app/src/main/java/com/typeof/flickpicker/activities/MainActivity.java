package com.typeof.flickpicker.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Button;
import android.widget.TextView;

import com.typeof.flickpicker.R;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    TabHost tabHost;

    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView myProfileIcon = (TextView)findViewById(R.id.myProfileIcon);
        myProfileIcon.setTypeface(font);

        fragmentManager = getFragmentManager();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
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
                    loadFragment(recommendationsFragment);
                }
                if (tabId.equals("Community")) {
                    CommunityFragment communityFragment = new CommunityFragment();
                    loadFragment(communityFragment);
                }
                if (tabId.equals("MyCollection")) {
                    MyCollectionFragment myColletionFragment = new MyCollectionFragment();
                    loadFragment(myColletionFragment);
                }
                if (tabId.equals("Search")) {
                    SearchFragment searchFragment = new SearchFragment();
                    loadFragment(searchFragment);
                }
                if (tabId.equals("Friends")) {
                    FriendsFragment friendsFragment = new FriendsFragment();
                    loadFragment(friendsFragment);
                }
            }
        });
    }

    public static void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentWrap, fragment);
        fragmentTransaction.addToBackStack(null); // last fragment used can be reached with the back button
        fragmentTransaction.commit();
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
