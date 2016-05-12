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

// TODO: weight: have user and friends seen same movies - and rated them equally (--> make a score of this users impact on movie recommendation)
//TODO: Need to check to make sure that there arent any duplicates of movies returned. HAve to take this into consideration - that several friends have different ratings to same movie

public class MovieAlgorithm {

    /**
     * Method for getting movies recommended for the user based on algorithm.
     * @return List of movies recommended for the user.
     */
    public static List<Movie> getRecommendations(User user){

        List<Movie> results = new ArrayList<>();

        //fetch friends latest movie ratings;
        List<Rating> friendsRecommendations = App.getFriendDAO().getFriendsLatestActivities(user.getId());

        //convert them into movies:
        List<Movie> friendsLatestMovies = new ArrayList<Movie>();
        List<Double> friendsCombinedRecommendations = new ArrayList<Double>();

        //loop through the ratings and extract the  uniqe movies and save the users friends' recommendations for those movies:
        for (int i = 0; i<friendsRecommendations.size(); i++){

            long currentMovieId = friendsRecommendations.get(i).getMovieId();
            Movie currentMovie = App.getMovieDAO().findMovie(currentMovieId);
            String name = currentMovie.getTitle();

            //adds current movie if not previously in the list
            if(!(friendsLatestMovies.contains(currentMovie))){
                friendsLatestMovies.add(currentMovie);
            }
        }

        //...now do the magic:

        //first create a score log
        List<Double> score = new ArrayList<Double>();
        for(int i = 0; i<friendsLatestMovies.size(); i++) {

            Movie currentMovie = friendsLatestMovies.get(i);

            if (currentMovie.getNumberOfVotes() != 0) {
                double friendsScore = friendsRecommendations.get(i).getRating();
                double communityScore = currentMovie.getCommunityRating();
                String currentMovieName = currentMovie.getTitle();
                double movieScore = (communityScore + friendsScore) / 2;

                score.add(movieScore);
            } else {
                score.add(friendsRecommendations.get(i).getRating());
            }
        }

        //...then place the movies - based on score - in a correctly ordered list and return it:
        for (int i = 0; i<friendsLatestMovies.size()-1; i++){

            for (int j = 0; j<friendsLatestMovies.size()-1; j++){

                if(score.get(j+1) > score.get(j)) {

                    double lesserScore = score.get(j);
                    double higherScore = score.get(j+1);
                    Movie lesserScoredMovie = friendsLatestMovies.get(j);
                    Movie higherScoredMovie = friendsLatestMovies.get(j+1);

                    friendsLatestMovies.set(j, higherScoredMovie);
                    friendsLatestMovies.set(j+1, lesserScoredMovie);
                    score.set(j, score.get(j + 1));
                    score.set(j + 1, score.get(j));
                }
            }
        }
        return friendsLatestMovies;
    }
}
