package com.typeof.flickpicker.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.utils.OMDBParser;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SettingsFragment extends Fragment {

    private Button reSeedBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View communityView = inflater.inflate(R.layout.settings_fragment, container, false);
        reSeedBtn = (Button)communityView.findViewById(R.id.reSeedBtn);

        reSeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.createDatabase();
                App.setupCurrentUser();
                App.getDatabase().seedDatabase(getActivity());

                // Get movies from OMDB
                OMDBParser omdbParser = new OMDBParser(getActivity(), App.getMovieDAO());
                omdbParser.execute();
            }
        });

        return communityView;
    }

}
