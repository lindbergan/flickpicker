package com.typeof.flickpicker.activities;

import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.MovieAlgorithm;
import com.typeof.flickpicker.core.User;
import java.util.ArrayList;
import java.util.List;


/**
 * FlickPicker
 * Group 22
 * Created on 03/05/16.
 */

public class RecommendationsFragment extends Fragment {

    private ListView mListViewFeed;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View recommendationsView = inflater.inflate(R.layout.activity_recommendations, container, false);
        hookUpViews(recommendationsView);
        populateListView();

        return recommendationsView;
    }

    public void hookUpViews(View view){
        mListViewFeed = (ListView) view.findViewById(R.id.recommendationsListView);
    }

    public void populateListView(){
        List<Movie> recommendedMovies = getRecommendations(App.getCurrentUser());
        ListAdapter adapter = new MovieAdapter(getActivity(), recommendedMovies.toArray());
        mListViewFeed.setAdapter(adapter);
    }

    /**
     * Method for getting movies recommended for the user.
     * @param user
     * @return recommendedMovies List of movies recommended for the user.
     */
    public List<Movie> getRecommendations(User user){

        //create and return list of recommended movies based on algorithm
        List<Movie> recommendedMovies = MovieAlgorithm.getRecommendations(user);
        return recommendedMovies;
    }
}
