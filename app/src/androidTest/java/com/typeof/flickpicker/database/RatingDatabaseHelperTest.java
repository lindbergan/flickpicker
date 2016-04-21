package com.typeof.flickpicker.database;
import android.test.AndroidTestCase;
import android.util.Log;

import com.typeof.flickpicker.Movie;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class RatingDatabaseHelperTest extends AndroidTestCase {

    private RatingDatabaseHelper mRatingDatabaseHelper;
    private DatabaseSeed rDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRatingDatabaseHelper = new RatingDatabaseHelper(getContext());
        rDatabaseSeed = new DatabaseSeed(getContext());
        rDatabaseSeed.seedDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        rDatabaseSeed.clearDatabase();
    }

    /**
     * Tests if we can find a record in the database
     * @throws Exception
     */
    public void testFind() throws Exception {
        Movie movie = mRatingDatabaseHelper.find(5); // This record is created via the DatabaseSeed
        assertEquals("Checking if fetching movie is successful", "Shawshank Redemption", movie.getTitle());
    }

    /**
     * Tests if a record is saved in the database
     * @throws Exception
     */
    public void testSave() throws Exception {
        Movie movie = new Movie("Rocky");
        long rowId = mRatingDatabaseHelper.save(movie);
        assertFalse(rowId == -1);
    }

    /**
     * Tests if we can create a record and then update it
     * @throws Exception
     */
    public void testUpdate() throws Exception {
        Movie movie = new Movie("2001: A Space Odyssey");
        long movieId = mRatingDatabaseHelper.save(movie);

        // We assert that the movie was saved and was given a unique ID;
        assertFalse(movieId == -1);

        movie.setTitle("2001");
        mRatingDatabaseHelper.save(movie);

        // We now look in our database for the record saved
        Movie movieFetched = mRatingDatabaseHelper.find(movieId);

        // Check if the movie has the new updated title
        assertEquals(movieFetched.getTitle(), "2001");
    }

    /**
     * Tests if we can create a movie in the database and then find it by searching for it.
     * @throws Exception
     */
    public void testSearch() throws Exception {
        Movie movie = new Movie("Pulp Fiction");
        long id = mRatingDatabaseHelper.save(movie);

        List<Movie> results = mRatingDatabaseHelper.search("Pulp");

        assertEquals(results.size(), 1);

        Movie foundMovie = results.get(0);
        assertEquals(id, foundMovie.getId());
    }

}