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

        //first create a score log
        List<Double> score = new ArrayList<Double>();
        for(int i = 0; i<friendsLatestMovies.size(); i++){

            Movie currentMovie = friendsLatestMovies.get(i);
            double movieScore = currentMovie.getCommunityRating() + friendsRecommendations.get(i).getRating();
            score.add(movieScore);
        }

        //...then place the movies - based on score - in a correctly ordered list and return it:
        for (int i = 0; i<friendsLatestMovies.size(); i++){

            for (int j = 0; j<friendsLatestMovies.size(); j++){

                if(score.get(j+1) < score.get(j)) {

                    double lesserScore = score.get(j + 1);
                    double higherScore = score.get(j);
                    Movie lesserScoredMovie = friendsLatestMovies.get(j+1);
                    Movie higherScoredMovie = friendsLatestMovies.get(j);

                    results.set(j, lesserScoredMovie);
                    results.set(j+1, higherScoredMovie);
                    score.set(j, score.get(j + 1));
                    score.set(j + 1, score.get(j));
                }
            }
        }

        return results;
    }

}
