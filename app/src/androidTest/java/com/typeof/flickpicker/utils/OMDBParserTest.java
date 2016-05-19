package com.typeof.flickpicker.utils;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Movie;
import junit.framework.TestCase;
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
        omdbParser.setMovieIds(new ArrayList<String>(){{
          add("tt0111161");
          add("tt0068646");
          add("tt0071562");
        }});
        omdbParser.requestAllMoviesFromOMDB();
        List<Movie> movies = omdbParser.getMovies();

        assertEquals(3, movies.size());
    }


}