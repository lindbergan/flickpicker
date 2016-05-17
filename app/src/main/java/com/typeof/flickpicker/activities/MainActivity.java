package com.typeof.flickpicker.activities;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;

import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import com.typeof.flickpicker.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    TabHost tabHost;

    private ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private List<Integer> previousPositions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupScore();
        setupSettings();
        initViewPager();

        App.getDatabase().seedDatabase();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        if (tabHost != null) {
            tabHost.setup();
        }

        configureTabs();
    }


    public void initViewPager() {
        // instantiate viewpager
        mViewPager = (ViewPager) findViewById(R.id.pager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecommendationsFragment());
        fragments.add(new CommunityFragment());
        fragments.add(new FriendsFragment());
        fragments.add(new MyCollectionFragment());
        fragments.add(new SearchFragment());
        fragments.add(new MyProfileFragment());


        mPagerAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(10);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // The back button logic
                if (previousPositions.indexOf(position) != -1) {
                    previousPositions.remove(previousPositions.indexOf(position));
                }

                previousPositions.add(position);

                if (position < 5) {
                    tabHost.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            if (previousPositions.size() > 1) {
                previousPositions.remove(previousPositions.size() - 1);
                mViewPager.setCurrentItem(previousPositions.get(previousPositions.size() - 1), false);
            } else {
                mViewPager.setCurrentItem(0, false);
            }
        }
    }

    private void setupScore() {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView myProfileIcon = (TextView)findViewById(R.id.myProfileIcon);
        TextView userScore = (TextView) findViewById(R.id.userScore);

        myProfileIcon.setTypeface(font);
        userScore.setText(String.valueOf(App.getCurrentUser().getScore()));

        myProfileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(5);
            }
        });
    }

    private void setupSettings() {

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");


        TextView settingsIcon = (TextView)findViewById(R.id.settingsIcon);
        settingsIcon.setTypeface(font);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SettingsFragment settingsFragment = new SettingsFragment();
                // loadFragment(settingsFragment);
            }
        });
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
                mViewPager.setCurrentItem(0);
            }
            if (tabId.equals("Community")) {
                mViewPager.setCurrentItem(1);
            }
            if (tabId.equals("Friends")) {
                mViewPager.setCurrentItem(2);
            }
            if (tabId.equals("MyCollection")) {
                mViewPager.setCurrentItem(3);
            }
            if (tabId.equals("Search")) {
                mViewPager.setCurrentItem(4);
            }

            }
        });
    }


    public ViewPager getViewPager() {
        return mViewPager;
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.v("Activity", "Saved Main Activity");
    }
}
