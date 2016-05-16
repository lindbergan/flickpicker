package com.typeof.flickpicker.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.typeof.flickpicker.R;

/**
 * FlickPicker
 * Group 22
 * Created on 16/05/16.
 */
public class MyProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myProfileView = inflater.inflate(R.layout.activity_my_profile, container, false);




        return myProfileView;


    }
}
