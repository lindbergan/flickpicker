package com.typeof.flickpicker.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * ScreenSlidePageAdapter
 *
 * Loads and holds fragments
 */
public class ScreenSlidePageAdapter extends FragmentStatePagerAdapter {

    List<Fragment> mFragments;

    public ScreenSlidePageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        mFragments.size();
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public int addFragment(Fragment fragment) {
        int fragmentIndex = mFragments.indexOf(fragment);

        if (fragmentIndex == -1) {
            mFragments.add(fragment);
            return mFragments.indexOf(fragment);
        }

        mFragments.set(fragmentIndex, fragment);

        return fragmentIndex;

    }

}
