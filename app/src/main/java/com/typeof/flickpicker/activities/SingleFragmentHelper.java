package com.typeof.flickpicker.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

/**
 * SingleFragmentHelper
 *
 * Loads Fragment of single type (Single movie, single user) int to the main activity's
 * ViewPager
 */
public class SingleFragmentHelper {
    public static void setFragment(MainActivity mainActivity, Fragment fragment) {
        ViewPager viewPager = mainActivity.getViewPager();
        ScreenSlidePageAdapter pagerAdapter = (ScreenSlidePageAdapter)viewPager.getAdapter();
        int fragmentIndex = pagerAdapter.addFragment(fragment);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(fragmentIndex, false);
    }
}
