package com.typeof.flickpicker.utils;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-17.
 */
public class OMDBParserTest extends AndroidTestCase {

    public void testGetMovies() {
        OMDBParser omdbParser = new OMDBParser(getContext(), App.getMovieDAO());

        // Three IMDB ids
        omdbParser.setMovieIds(new ArrayList<String>(){{
          add("tt0111161");
          add("tt0068646");
          add("tt0071562");
        }});

        // Movies is fetched from OMDB
        omdbParser.requestAllMoviesFromOMDB();

        List<Movie> movies = omdbParser.getMovies();

        assertEquals(3, movies.size());
    }

}