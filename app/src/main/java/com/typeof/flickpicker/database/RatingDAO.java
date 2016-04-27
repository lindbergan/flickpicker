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

    public List<Rating> getMovieRatings(long movieId); // int, not long right?
    public long saveRating(double RatingValue, long movieId, long userId);
    public int removeRating(long ratingId);

    List<Movie> getCommunityTopPicks(int max);
    //List<Movie> getTopRecommendedMoviesThisYear(int max);
    //List<Movie> getMostDislikedMovies(int max);
}
