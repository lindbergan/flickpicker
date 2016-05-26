package com.typeof.flickpicker.application.helpers;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.application.activities.MainActivity;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;

import java.util.List;

/**
 * RatingHelper
 *
 * A class that saves the users rating and updates the users score.
 * It also sets the mismatch value accordingly.
 */

public class RatingHelper {

    /**
     * A static helper method which is called when a new rating is created.
     * The method saves the newly created rating, update the mismatch value of the user
     * and its friends accordingly and also sets the correct score.
     * @param rating the new Rating
     * @param movieId the id of the movie the rating refers to
     * @param userId the id of the user the id refers to
     */

    public static void createNewRating(double rating, long movieId, long userId){

        Rating newRating = new Rating(rating, movieId, userId);
        App.getRatingDAO().saveRating(newRating);

        //update the mismatch values in relation to users new rating
        App.getFriendDAO().updateFriendMatches(newRating);

        if(!previousRatingExists(newRating.getId())) {

            //set the user's score accordingly
            User currentUser = App.getCurrentUser();
            int oldScore = currentUser.getScore();
            int newScore = oldScore + 10;
            currentUser.setScore(newScore);
            MainActivity.setScore(newScore);
        }
    }

    /**
     * A method the determines whether or not the user has rated the movie before.
     * @return returns true if movie was previously rated by user.
     */
    private static boolean previousRatingExists(long ratingId){

        //extract the movie and userId from the rating object and check if the movie already exists in the users movie collection
        try {
            App.getRatingDAO().findRating(ratingId);
        }
        catch (DatabaseRecordNotFoundException e) {
            return false;
        }
        return true;
    }
}
