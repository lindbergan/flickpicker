package com.typeof.flickpicker.database;

import android.test.AndroidTestCase;
import android.util.Log;

import com.typeof.flickpicker.Movie;

import junit.framework.TestCase;

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
        Log.d("Test", movie.getTitle());
        assertEquals("Checking if fetching movie is successful", "Shawshank Redemption", movie.getTitle());
    }


}