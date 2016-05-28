package com.typeof.flickpicker.utils;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import java.util.List;
import java.util.Random;

public class RandomizedData {

    public static void createRandomizedData() {

        Random rand = new Random();

        MovieDAO movieDAO = App.getMovieDAO();
        UserDAO userDAO = App.getUserDAO();
        RatingDAO ratingDAO = App.getRatingDAO();

        List<Movie> movieList = movieDAO.getCommunityTopPicks(500);
        movieList.size();
        List<User> userList = userDAO.getAllUsers();
        
        // create 100 ratings
        for(int i = 0; i < 1000; i++) {
            int randomMovieIndex = rand.nextInt(movieList.size());
            int randomUserIndex = rand.nextInt(userList.size());
            double randomRating = rand.nextInt(5 + 1); //max rating + 1 because bound is exclusive

            long randomMovieId = movieList.get(randomMovieIndex).getId();
            long randomUserId = userList.get(randomUserIndex).getId();

            Rating rating = new Rating(randomRating, randomMovieId, randomUserId);
            ratingDAO.saveRating(rating);
        }
    }

}
