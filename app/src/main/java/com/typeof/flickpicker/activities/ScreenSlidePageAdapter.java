package com.typeof.flickpicker.activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-17.
 */
public class ScreenSlidePageAdapter extends FragmentStatePagerAdapter {

    public ScreenSlidePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RecommendationsFragment();
            case 1:
                return new CommunityFragment();
            case 2:
                return new FriendsFragment();
            case 3:
                return new MyCollectionFragment();
            case 4:
                return new SearchFragment();
            default:
                return new RecommendationsFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
