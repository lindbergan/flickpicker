package com.typeof.flickpicker.application;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * UnSwipeableViewPager
 *
 * A View Pager that prevents swiping between fragments.
 */
public class UnSwipeableViewPager extends ViewPager {

    public UnSwipeableViewPager(Context ctx, AttributeSet attributeSet) {
        super(ctx, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

}
