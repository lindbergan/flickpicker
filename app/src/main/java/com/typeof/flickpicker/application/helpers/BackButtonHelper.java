package com.typeof.flickpicker.application.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * BackButtonHelper keeps track of the current fragments position in the ViewPager
 */
public class BackButtonHelper {

    private static BackButtonHelper instance;
    private final List<Integer> mPreviousPositions = new ArrayList<>();
    private BackButtonHelper() {}

    public static BackButtonHelper getInstance() {
        if (instance == null) {
            instance = new BackButtonHelper();
        }
        return instance;
    }

    public void setPreviousPosition(Integer pos) {
        if (mPreviousPositions.indexOf(pos) != -1) {
            mPreviousPositions.remove(mPreviousPositions.indexOf(pos));
        }
        mPreviousPositions.add(pos);
    }

    public List<Integer> getPreviousPositions() {
        return mPreviousPositions;
    }

    public int getPreviousPositionSize() {
        return mPreviousPositions.size();
    }

}
