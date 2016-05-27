package com.typeof.flickpicker.utils;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.util.List;

class RandomizedData {

    public static void createRandomizedData() {
        MovieDAO movieDAO = App.getMovieDAO();
        List<Movie> movieList = movieDAO.getCommunityTopPicks(500);
        
        for(Movie m : movieList) {

        }
    }

}
