package com.typeof.flickpicker.application.activities;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.application.adapters.ViewPageAdapter;
import com.typeof.flickpicker.application.fragments.CollectionFragment;
import com.typeof.flickpicker.application.fragments.CommunityFragment;
import com.typeof.flickpicker.application.fragments.FriendsFragment;
import com.typeof.flickpicker.application.fragments.MyProfileFragment;
import com.typeof.flickpicker.application.fragments.RecommendationsFragment;
import com.typeof.flickpicker.application.fragments.SearchFragment;
import com.typeof.flickpicker.application.fragments.SettingsFragment;
import com.typeof.flickpicker.application.helpers.BackButtonHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements PropertyChangeListener {

    TabHost tabHost;

    private ViewPager mViewPager;
    public PagerAdapter mPagerAdapter;
    private static TextView mScore;
    private boolean useCustomBackButton = false;
    Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewPager();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        if (tabHost != null) {
            tabHost.setup();
        }

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");

        configureTabs();
        initHeader();

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        );
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

        mPagerAdapter = new ViewPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // The back button logic
                BackButtonHelper backButtonHelper = BackButtonHelper.getInstance();
                backButtonHelper.setPreviousPosition(position);

                if (position < 5) {
                    tabHost.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void initHeader() {

        TextView profile = (TextView) findViewById(R.id.myProfileIcon);
        mScore = (TextView) findViewById(R.id.userScore);
        TextView title = (TextView) findViewById(R.id.flickPickerText);
        TextView settings = (TextView) findViewById(R.id.settingsIcon);

        profile.setTypeface(mTypeface);
        settings.setTypeface(mTypeface);
        mScore.setText(String.valueOf(App.getCurrentUser().getScore()));
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/DISTGRG_.ttf");
        title.setTypeface(titleFont);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(5);
                resetColor();

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(6);
                resetColor();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(!useCustomBackButton) {
            if (mViewPager.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
            } else {
                // Otherwise, select the previous step.
                BackButtonHelper backButtonHelper = BackButtonHelper.getInstance();
                int previousPositionSize = backButtonHelper.getPreviousPositions().size();
                if (previousPositionSize > 1) {
                    backButtonHelper.getPreviousPositions().remove(previousPositionSize - 1);
                    mViewPager.setCurrentItem(backButtonHelper.getPreviousPositions().get(backButtonHelper.getPreviousPositionSize() - 1), false);
                } else {
                    mViewPager.setCurrentItem(0, false);
                }
            }
        } else {
            useCustomBackButton = false;
        }
    }

    public void setUseCustomBackButton(boolean bool) {
        useCustomBackButton = bool;
    }

    public void configureTabs() {

        // TabHost footer

        final TabHost.TabSpec mTabSpecRecommendations = createTabSpec("Recommendations", R.id.tabRecommendations,
                R.layout.tab_recommendation, R.id.recommendationsIcon);
        tabHost.addTab(mTabSpecRecommendations);

        // Sets the active tabs color
        tabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.active_tab_color));

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
                changeColor(0);
            }
            if (tabId.equals("Community")) {
                mViewPager.setCurrentItem(1);
                changeColor(1);
            }
            if (tabId.equals("Friends")) {
                mViewPager.setCurrentItem(2);
                changeColor(2);
            }
            if (tabId.equals("MyCollection")) {
                mViewPager.setCurrentItem(3);
                changeColor(3);
            }
            if (tabId.equals("Search")) {
                mViewPager.setCurrentItem(4);
                changeColor(4);
            }

            }
        });
    }

    public void changeColor(int position) {
        resetColor();
        // Sets the selected tabs color
        this.tabHost.getTabWidget().getChildTabViewAt(position).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.active_tab_color));

    }

    public void resetColor() {
        // Set all tabs to the primary color
        for (int i = 0; i < this.tabHost.getTabWidget().getChildCount(); i++) {
            this.tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_primary));
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
    public TabHost.TabSpec createTabSpec(String tag, int viewId, int iconViewId, int iconId){

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setContent(viewId);
        View iconView = LayoutInflater.from(this).inflate(iconViewId, null);
        TextView icon = (TextView)iconView.findViewById(iconId);
        icon.setTypeface(mTypeface);
        tabSpec.setIndicator(iconView);

        return tabSpec;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.v("Activity", "Saved Main Activity");
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("ratingsChanged")) {
            updateScore();
        }
    }

    private void updateScore() {
        App.refreshCurrentUser();
        mScore.setText(String.valueOf(App.getCurrentUser().getScore()));
    }
}
