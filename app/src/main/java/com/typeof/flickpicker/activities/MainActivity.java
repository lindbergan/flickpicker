package com.typeof.flickpicker.activities;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.v("Activity", "Saved Main Activity");
    }
}
