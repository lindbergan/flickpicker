package com.typeof.flickpicker.database;

import android.test.AndroidTestCase;
import android.util.Log;

import com.typeof.flickpicker.Movie;

import junit.framework.TestCase;

import java.sql.SQLDataException;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieDatabaseHelperTest extends AndroidTestCase {

    private MovieDatabaseHelper mMovieDatabaseHelper;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMovieDatabaseHelper = new MovieDatabaseHelper(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mDatabaseSeed.seedDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabaseSeed.clearDatabase();
    }

    public void testFind() throws Exception {
        Movie movie = mMovieDatabaseHelper.find(5);
        assertEquals("Checking if fetching movie is successful", "Shawshank Redemption", movie.getTitle());
    }

    public void testSave() throws Exception {
        Movie movie = new Movie("Rocky");
        long rowId = mMovieDatabaseHelper.save(movie);

        Movie fetchedMovie = mMovieDatabaseHelper.find((int)rowId);
        assertEquals("Check if we can retrieve saved movie", "Rocky", fetchedMovie.getTitle());
    }

    public void testUpdate() throws Exception {
        Movie movie = new Movie("2001: A Space Odyssey");
        long movieId = 0;

        try {
            movieId = mMovieDatabaseHelper.save(movie);
        } catch (SQLDataException e) {
            e.printStackTrace();
        }

        movie.setTitle("2001");

        try {
            mMovieDatabaseHelper.save(movie);
        } catch (SQLDataException e) {
            e.printStackTrace();
        }

        // We assert that the movie was saved and was given a unique ID;
        assertFalse(movieId == 0);

        Movie movieFetched = mMovieDatabaseHelper.find(movieId);

        assertEquals(movieFetched.getTitle(), "2001");
    }

}