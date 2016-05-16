package com.typeof.flickpicker.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        //set up of 'My Profile icon' and its clickListener along with setting user score
        TextView myProfileIcon = (TextView)findViewById(R.id.myProfileIcon);
        TextView userScore = (TextView) findViewById(R.id.userScore);
        myProfileIcon.setTypeface(font);
        userScore.setText(String.valueOf(App.getCurrentUser().getScore()));
        myProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                loadFragment(myProfileFragment);
            }
        });

        TextView settingsIcon = (TextView)findViewById(R.id.settingsIcon);
        settingsIcon.setTypeface(font);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // SettingsFragment settingsFragment = new SettingsFragment();
               // loadFragment(settingsFragment);
            }
        });

        App.getDatabase().seedDatabase();

        fragmentManager = getFragmentManager();
        tabHost = (TabHost) findViewById(R.id.tabHost);
        if (tabHost != null) {
            tabHost.setup();
        }
        configureTabs();
    }

    public void configureTabs() {

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        final TabHost.TabSpec mTabSpecRecommendations = tabHost.newTabSpec("Recommendations");
        mTabSpecRecommendations.setContent(R.id.tabRecommendations);

        View icon = LayoutInflater.from(this).inflate(R.layout.tab_recommendation, null);
        TextView tv = (TextView)icon.findViewById(R.id.recommendationsIcon);
        tv.setTypeface(font);
        mTabSpecRecommendations.setIndicator(icon);
        tabHost.addTab(mTabSpecRecommendations);

        final TabHost.TabSpec mTabSpecCommunity = tabHost.newTabSpec("Community");
        mTabSpecCommunity.setContent(R.id.tabCommunity);

        View icon2 = LayoutInflater.from(this).inflate(R.layout.tab_community, null);
        TextView tv2 = (TextView)icon2.findViewById(R.id.communityIcon);
        tv2.setTypeface(font);
        mTabSpecCommunity.setIndicator(icon2);
        tabHost.addTab(mTabSpecCommunity);

        final TabHost.TabSpec mTabSpecFriendsActivities = tabHost.newTabSpec("Friends");
        mTabSpecFriendsActivities.setContent(R.id.tabFriendsActivities);

        View icon3 = LayoutInflater.from(this).inflate(R.layout.tab_friends, null);
        TextView tv3 = (TextView)icon3.findViewById(R.id.friendsIcon);
        tv3.setTypeface(font);
        mTabSpecFriendsActivities.setIndicator(icon3);
        tabHost.addTab(mTabSpecFriendsActivities);

        final TabHost.TabSpec mTabSpecMyMovies = tabHost.newTabSpec("MyCollection");
        mTabSpecMyMovies.setContent(R.id.tabMyMovies);

        View icon4 = LayoutInflater.from(this).inflate(R.layout.tab_my_collection, null);
        TextView tv4 = (TextView)icon4.findViewById(R.id.myCollectionIcon);
        tv4.setTypeface(font);
        mTabSpecMyMovies.setIndicator(icon4);
        tabHost.addTab(mTabSpecMyMovies);

        final TabHost.TabSpec mTabSpecSearch = tabHost.newTabSpec("Search");
        mTabSpecSearch.setContent(R.id.tabSearch);

        View icon5 = LayoutInflater.from(this).inflate(R.layout.tab_search, null);
        TextView tv5 = (TextView)icon5.findViewById(R.id.searchIcon);
        tv5.setTypeface(font);
        mTabSpecSearch.setIndicator(icon5);
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
                    MyCollectionFragment myCollectionFragment = new MyCollectionFragment();
                    loadFragment(myCollectionFragment);
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
