package com.typeof.flickpicker.application.fragments;

import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.adapters.MovieAdapter;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.utils.MovieAlgorithm;
import com.typeof.flickpicker.core.User;
import java.util.List;


/**
 * RecommendationsFragment
 *
 * A controller class for the recommendation view
 */

public class RecommendationsFragment extends Fragment {

    private ListView mListViewFeed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View recommendationsView = inflater.inflate(R.layout.activity_recommendations, container, false);
        hookUpViews(recommendationsView);
        populateListView();

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        return recommendationsView;
    }

    //Setup the views in the corresponding XML-file
    private void hookUpViews(View view){
        mListViewFeed = (ListView) view.findViewById(R.id.recommendationsListView);
    }

    //Populate the listView with the elements from getRecommendations()
    private void populateListView(){
        List<Movie> recommendedMovies = getRecommendations(App.getCurrentUser());
        ListAdapter adapter = new MovieAdapter(getActivity(), recommendedMovies.toArray());
        mListViewFeed.setAdapter(adapter);
    }

    //A method that uses MovieAlgorithm helper class to return a list of recommended movies for the user
    private List<Movie> getRecommendations(User user){

        return MovieAlgorithm.getRecommendations(user);
    }
}
