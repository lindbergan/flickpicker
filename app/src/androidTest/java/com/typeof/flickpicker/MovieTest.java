package com.typeof.flickpicker;

import junit.framework.TestCase;

public class MovieTest extends TestCase {

    public void testMovieID() {
        Movie movie = new Movie(55);
        assertTrue(movie.getId() == 55);
    }

    public void testError() {
        assertTrue(false);
    }

}