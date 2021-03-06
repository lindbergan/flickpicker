package com.typeof.flickpicker.application.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.utils.MovieCacheHandler;
import com.typeof.flickpicker.utils.OMDBParser;
import com.typeof.flickpicker.utils.RandomizedData;

/**
 * SettingsFragment extends Fragment
 * Used in settings view and to reset and refill database
 */

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        Button reSeedBtn = (Button) view.findViewById(R.id.reSeedBtn);

        final Context ctx = getActivity();

        reSeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            App.createDatabase();
            App.refreshCurrentUser();
            App.getDatabase().seedDatabase();

            // If not, then we seed it
            MovieCacheHandler movieCacheHandler = new MovieCacheHandler(ctx);
            movieCacheHandler.execute();

            RandomizedData randomizedData = new RandomizedData(ctx);
            randomizedData.execute();
            }
        });

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return view;
    }

}
