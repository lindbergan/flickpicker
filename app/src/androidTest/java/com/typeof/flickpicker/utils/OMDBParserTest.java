package com.typeof.flickpicker.utils;
import com.typeof.flickpicker.core.Movie;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-17.
 */
public class OMDBParserTest extends TestCase {

    public void testGetMovies() {
        OMDBParser omdbParser = new OMDBParser();
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