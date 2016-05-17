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

        final TabHost.TabSpec mTabSpecRecommendations = createTabSpec("Recommendations", R.id.tabRecommendations,
                                                                        R.layout.tab_recommendation, R.id.recommendationsIcon);
        tabHost.addTab(mTabSpecRecommendations);


        final TabHost.TabSpec mTabSpecCommunity = createTabSpec("Community", R.id.tabCommunity,
                                                                    R.layout.tab_community, R.id.communityIcon);
        tabHost.addTab(mTabSpecCommunity);


        final TabHost.TabSpec mTabSpecFriendsActivities = createTabSpec("Friends", R.id.tabFriendsActivities,
                                                                             R.layout.tab_friends, R.id.friendsIcon);
        tabHost.addTab(mTabSpecFriendsActivities);


        final TabHost.TabSpec mTabSpecMyMovies = createTabSpec("MyCollection", R.id.tabMyMovies,
                                                                 R.layout.tab_my_collection, R.id.myCollectionIcon);
        tabHost.addTab(mTabSpecMyMovies);

        final TabHost.TabSpec mTabSpecSearch = createTabSpec("Search", R.id.tabSearch,
                                                                R.layout.tab_search, R.id.searchIcon);
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


    /**
     * Method for creating a TabSpec to use when adding new tabs to
     * the tabWidget.
     *
     * @param tag the new tabSpec's tag
     * @param viewId the Id for the content view associated with the new tab
     * @param iconViewId the Id for the view containing the icon for the new tab
     * @param iconId the Id for the TextView representing the icon for the new tab
     * @return tabSpec
     */
    public TabHost.TabSpec createTabSpec(String tag, int viewId, int iconViewId, int iconId){

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setContent(viewId);
        View iconView = LayoutInflater.from(this).inflate(iconViewId, null);
        TextView icon = (TextView)iconView.findViewById(iconId);
        icon.setTypeface(font);
        tabSpec.setIndicator(iconView);

        return tabSpec;
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
