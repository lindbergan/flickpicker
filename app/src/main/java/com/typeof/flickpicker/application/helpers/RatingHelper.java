package com.typeof.flickpicker.application.helpers;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

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

        RatingDAO ratingDAO = App.getRatingDAO();
        Rating existingRating = ratingDAO.getRatingObjectFromUser(userId, movieId);

        if(existingRating == null) {
            //set the user's score accordingly
            User currentUser = App.getCurrentUser();
            currentUser.updateScore();
            UserDAO userDAO = App.getUserDAO();
            userDAO.saveUser(currentUser);
            Rating newRating = new Rating(rating, movieId, userId);
            App.getRatingDAO().saveRating(newRating);
        } else {
            existingRating.updateRating(rating);
            App.getRatingDAO().saveRating(existingRating);
        }

        //update the mismatch values in relation to users new rating
        App.getFriendDAO().updateFriendMatches(userId);

        App.getEventBus().triggerEvent("rating_created");
    }
}
