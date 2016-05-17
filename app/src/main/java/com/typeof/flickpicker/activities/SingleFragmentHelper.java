package com.typeof.flickpicker.activities;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-17.
 */
public class SingleFragmentHelper {
    public static void setFragment(MainActivity mainActivity, Fragment fragment) {
        ViewPager viewPager = mainActivity.getViewPager();
        ScreenSlidePageAdapter pagerAdapter = (ScreenSlidePageAdapter)viewPager.getAdapter();
        int fragmentIndex = pagerAdapter.addFragment(fragment);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(fragmentIndex);
    }
}
