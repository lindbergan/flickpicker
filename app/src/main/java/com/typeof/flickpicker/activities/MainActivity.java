package com.typeof.flickpicker.activities;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.view.Display;
import android.view.LayoutInflater;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.typeof.flickpicker.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    TabHost tabHost;
    TabHost tabHost2;

    private ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private List<Integer> previousPositions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/DISTGRG_.ttf");
        //TextView title = (TextView)findViewById(R.id.flickPickerText);
        //title.setTypeface(titleFont);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        // setupScore();
        // setupSettings();
        initViewPager();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        if (tabHost != null) {
            tabHost.setup();
        }

        tabHost2 = (TabHost) findViewById(R.id.tabHost2);
        if (tabHost2 != null) {
            tabHost2.setup();
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
        fragments.add(new CollectionFragment());
        fragments.add(new SearchFragment());
        fragments.add(new MyProfileFragment());
        fragments.add(new SettingsFragment());


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
    /*
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
    }*/

    /*private void setupSettings() {

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        TextView settingsIcon = (TextView)findViewById(R.id.settingsIcon);
        settingsIcon.setTypeface(font);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(6);
            }
        });
    }*/

    public void configureTabs() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x / 5;
        int height = size.y;

        // TabHost header

        final TabHost.TabSpec mProfile = createTabSpec(tabHost2, "Profile", R.id.tabProfile, R.layout.tab_profile, R.id.myProfileIcon);
        tabHost2.addTab(mProfile);

        final TabHost.TabSpec title = createTabSpec(tabHost2, "Title", R.id.titleLayout, R.layout.tab_title, R.id.flickPickerText);
        tabHost2.addTab(title);

        final TabHost.TabSpec settings = createTabSpec(tabHost2, "Settings", R.id.tabSettings, R.layout.tab_settings, R.id.settingsIcon);
        tabHost2.addTab(settings);


        tabHost2.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (tabId.equals("Profile")) {
                    mViewPager.setCurrentItem(5);
                    changeColor(tabHost2, 0);
                }

                if (tabId.equals("Title")) {
                    mViewPager.setCurrentItem(0);
                    changeColor(tabHost, 0);
                }

                if (tabId.equals("Settings")) {
                    mViewPager.setCurrentItem(6);
                    changeColor(tabHost2, 2);
                }
            }
        });

        // TabHost footer

        final TabHost.TabSpec mTabSpecRecommendations = createTabSpec(tabHost, "Recommendations", R.id.tabRecommendations,
                R.layout.tab_recommendation, R.id.recommendationsIcon);
        tabHost.addTab(mTabSpecRecommendations);

        // Sets the active tabs color
        tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.active_tab_color));

        final TabHost.TabSpec mTabSpecCommunity = createTabSpec(tabHost, "Community", R.id.tabCommunity,
                                                                    R.layout.tab_community, R.id.communityIcon);
        tabHost.addTab(mTabSpecCommunity);


        final TabHost.TabSpec mTabSpecFriendsActivities = createTabSpec(tabHost, "Friends", R.id.tabFriendsActivities,
                R.layout.tab_friends, R.id.friendsIcon);
        tabHost.addTab(mTabSpecFriendsActivities);


        final TabHost.TabSpec mTabSpecMyMovies = createTabSpec(tabHost, "MyCollection", R.id.tabMyMovies,
                                                                 R.layout.tab_my_collection, R.id.myCollectionIcon);
        tabHost.addTab(mTabSpecMyMovies);

        final TabHost.TabSpec mTabSpecSearch = createTabSpec(tabHost, "Search", R.id.tabSearch,
                                                                R.layout.tab_search, R.id.searchIcon);
        tabHost.addTab(mTabSpecSearch);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

            if (tabId.equals("Recommendations")) {
                mViewPager.setCurrentItem(0);
                changeColor(tabHost, 0);
            }
            if (tabId.equals("Community")) {
                mViewPager.setCurrentItem(1);
                changeColor(tabHost, 1);
            }
            if (tabId.equals("Friends")) {
                mViewPager.setCurrentItem(2);
                changeColor(tabHost, 2);
            }
            if (tabId.equals("MyCollection")) {
                mViewPager.setCurrentItem(3);
                changeColor(tabHost, 3);
            }
            if (tabId.equals("Search")) {
                mViewPager.setCurrentItem(4);
                changeColor(tabHost, 4);
            }

            }
        });
    }

    public boolean isTabHost2(TabHost tabHost) {
        return tabHost.getTabWidget().getChildCount() == tabHost2.getTabWidget().getChildCount();
    }

    public void changeColor(TabHost tabHost, int position) {
        // Set all tabs to the primary color
        for (int i = 0; i < this.tabHost.getTabWidget().getChildCount(); i++) {
            this.tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
        }
        for (int i = 0; i < this.tabHost2.getTabWidget().getChildCount(); i++) {
            this.tabHost2.getTabWidget().getChildTabViewAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
        }

        if (isTabHost2(tabHost)) {
            if (position != 1) {
                this.tabHost2.getTabWidget().getChildTabViewAt(position).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.active_tab_color));
            }
        }
        else {
            this.tabHost.getTabWidget().getChildTabViewAt(position).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.active_tab_color));
        }

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
    public TabHost.TabSpec createTabSpec(TabHost selTabHost, String tag, int viewId, int iconViewId, int iconId){

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        TabHost.TabSpec tabSpec = selTabHost.newTabSpec(tag);
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
