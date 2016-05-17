package com.typeof.flickpicker.database;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface MovieDAO {

    Movie findMovie(long id);
    long saveMovie(Movie movie);
    int deleteMovie(Movie movie);
    List<Movie> searchMovieBy(String column, String searchTerm);
    int numOfFriendsHasSeenMovie(long movieId, long userId);
    List<Movie> getCommunityTopPicks(int max);
    List<Movie> getTopRecommendedMoviesThisYear(int max, int year);
    List<Movie> getMostDislikedMovies(int max);
    List<User> getFriendsSeenMovie(long movieId, long userId);
    List<Movie> getMovieCollectionFromUserId(int max, long userId);
    List<Rating> getUserRatings(int max, long userId);

}
