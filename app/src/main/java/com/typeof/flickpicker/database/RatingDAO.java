package com.typeof.flickpicker.database;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface RatingDAO {

    public List<Rating> getMovieRatings(long movieId);
    public long saveRating(double RatingValue, long movieId, long userId);
    public void removeRating(long ratingId);
}
