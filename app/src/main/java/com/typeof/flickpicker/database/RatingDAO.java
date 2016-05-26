package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.Rating;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface RatingDAO {

    List<Rating> getMovieRatings(long movieId);
    long saveRating(Rating rating);
    int removeRating(Rating rating);
    Rating findRating(long ratingId);
    double getRatingFromUser(long userId, long movieId);
    List<Rating> getAllRatingsFromUser(long userId);
}
