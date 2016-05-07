package com.typeof.flickpicker.activities;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
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
public class recommendationsActivity extends AppCompatActivity {

    private ListView mMovieListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        //Populate list with MovieCells
        mMovieListView = (ListView) findViewById(R.id.recommendationsListView);
        List<Movie> movies = new ArrayList<Movie>();
        //movies = getRecommendations(App.getActiveUser);
        //Adapter adapter = new ArrayAdapter<Movie>(this, android.R.layout.simple_list_item_1, movies);


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
