package com.typeof.flickpicker.application.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * ViewPageAdapter
 *
 * Loads and holds fragments
 */
public class ViewPageAdapter extends FragmentStatePagerAdapter {

    List<Fragment> mFragments;
    FragmentManager mFragmentManager;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragmentManager = fm;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public int addFragment(Fragment fragment) {
        mFragments.add(fragment);
        return mFragments.indexOf(fragment);
    }

    public Fragment getFragmentByClass(Class c) {
        for (Fragment fragment : mFragments) {
            if (fragment.getClass() == c) {
                return fragment;
            }
        }
        return null;
    }

    public int getFragmentPositionByClass(Class c) {
        Fragment fragment = getFragmentByClass(c);
        int pos = mFragments.indexOf(fragment);
        return mFragments.indexOf(fragment);
    }

}
