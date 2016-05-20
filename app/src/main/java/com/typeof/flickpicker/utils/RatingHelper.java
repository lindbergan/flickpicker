package com.typeof.flickpicker.utils;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.RatingDAO;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 12/05/16.
 */
public class RatingHelper {

    /**
     * A static helper method which is called when a new rating is created.
     * The method saves the newly created rating, update the dismatch value of the user
     * and its friends accordingly and also sets the correct score.
     * @param rating
     * @param movieId
     * @param userId
     */
    public static void createNewRating(double rating, long movieId, long userId){

        Rating newRating = new Rating(rating, movieId, userId);
        App.getRatingDAO().saveRating(newRating);

        //update the dismatch values in relation to users new rating
        App.getFriendDAO().updateFriendMatches(newRating);

        if(!previousRatingExists(newRating)) {

            //set the user's score accordingly
            User currentUser = App.getCurrentUser();
            int oldScore = currentUser.getScore();
            int newScore = oldScore + 10;
            currentUser.setScore(newScore);
        }
    }

    /**
     * A method the determines whether or not the user of the rating has rated the movie before.
     * @param rating
     * @return true if movie was previously rated by user. Otherwise false.
     */
    private static boolean previousRatingExists(Rating rating){

        //extract the movie && userId from the raiting object and check if the movie already exists in the users movie collection
        long ratingsUser = rating.getUserId();
        Movie ratedMovie = App.getMovieDAO().findMovie(rating.getMovieId());
        int desiredSizeOfList = 100;

        List<Movie> usersMovieCollection = App.getMovieDAO().getMovieCollectionFromUserId(desiredSizeOfList, ratingsUser);

        return usersMovieCollection.contains(ratedMovie);

    }
}
