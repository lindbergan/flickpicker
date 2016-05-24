package com.typeof.flickpicker.application.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.typeof.flickpicker.application.MainActivity;
import com.typeof.flickpicker.application.adapters.ViewPageAdapter;

/**
 * SingleFragmentHelper
 *
 * Loads Fragment of single type (Single movie, single user) int to the main activity's
 * ViewPager
 */
public class SingleFragmentHelper {
    public static void setFragment(MainActivity mainActivity, Fragment fragment) {
        ViewPager viewPager = mainActivity.getViewPager();
        ViewPageAdapter pagerAdapter = (ViewPageAdapter)viewPager.getAdapter();
        int fragmentIndex = pagerAdapter.addFragment(fragment);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(fragmentIndex, false);
    }
}
