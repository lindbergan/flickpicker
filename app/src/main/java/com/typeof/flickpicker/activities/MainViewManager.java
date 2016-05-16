package com.typeof.flickpicker.activities;


import android.app.Fragment;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-11.
 */
public class MainViewManager {
    private static MainViewManager instance;

    private MainViewManager() {
    }

    public static MainViewManager getInstance() {
        if (instance == null) {
            instance = new MainViewManager();
        }
        return instance;
    }

    public static void loadFragmentIntoMainView(Fragment fragment) {

    }

}
