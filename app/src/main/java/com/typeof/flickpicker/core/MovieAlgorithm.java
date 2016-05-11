package com.typeof.flickpicker.core;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 03/05/16.
 */
public class MovieAlgorithm {

    /**
     * Method for getting movies recommended for the user based on algorithm.
     * @return List of movies recommended for the user.
     */
    public static List<Movie> getRecommendations(User user){

        //algorithm
        List<Movie> results = new ArrayList<>();

        //fetch friends latest movie ratings;
        List<Rating> friendsRecommendations = App.getFriendDAO().getFriendsLatestActivities(user.getId());

        //convert them into movies:
        List<Movie> friendsLatestMovies = new ArrayList<Movie>();

        //loop through the ratings and extract the movies:
        for (int i = 0; i<friendsLatestMovies.size(); i++){

            long currentMovieId = friendsRecommendations.get(i).getMovieId();
            Movie currentMovie = App.getMovieDAO().findMovie(currentMovieId);
            friendsLatestMovies.add(currentMovie);
        }

        //...now do the magic:
        for(int i = 0; i<friendsLatestMovies.size(); i++){

            Movie currentMovie = friendsLatestMovies.get(i);
            //currentMovie

        }

        return results;
    }

}
