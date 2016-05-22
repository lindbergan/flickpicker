package com.typeof.flickpicker.utils;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.RatingDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 12/05/16.
 */
public class RatingHelper {

    /**
     * static helper method for creating a new rating
     * @param rating
     * @param movieId
     * @param userId
     */
    public static void createNewRating(double rating, long movieId, long userId){

        RatingDAO ratingDAO = App.getRatingDAO();
        Rating newRating = new Rating(rating, movieId, userId);
        ratingDAO.saveRating(newRating);
        App.getFriendDAO().updateFriendMatches(newRating);

        //updateUserMatching();
    }

    //private static void updateUserMatching(){}

}
