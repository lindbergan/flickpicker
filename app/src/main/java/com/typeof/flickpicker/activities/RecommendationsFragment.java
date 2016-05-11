package com.typeof.flickpicker.activities;

import android.app.Fragment;
import android.app.TabActivity;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.typeof.flickpicker.R;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.MovieAlgorithm;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.database.sql.SQLDAO;
import com.typeof.flickpicker.database.sql.SQLUserDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * FlickPicker
 * Group 22
 * Created on 03/05/16.
 */
public class RecommendationsFragment extends Fragment {

    private ListView mMovieListView;

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
        mMovieListView = (ListView) view.findViewById(R.id.recommendationsListView);
    }

    public void populateListView(){
        List<Movie> recommendedMovies = getRecommendations(App.getCurrentUser());
        ListAdapter adapter = new MovieAdapter(getActivity(), recommendedMovies.toArray());
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
