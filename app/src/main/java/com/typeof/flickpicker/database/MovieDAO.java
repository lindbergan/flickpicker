package com.typeof.flickpicker.database;
import android.content.ContentValues;

import com.typeof.flickpicker.core.Movie;
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

}