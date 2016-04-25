package com.typeof.flickpicker.database;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.sql.SQLMovieDAO;

import junit.framework.Assert;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class SQLMovieDAOTest extends AndroidTestCase {

    private MovieDAO mMovieDAO;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMovieDAO = new SQLMovieDAO(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mDatabaseSeed.seedDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabaseSeed.clearDatabase();
    }

    /**
     * Tests if we can find a record in the database
     * @throws Exception
     */
    public void testFind() throws Exception {
        Movie movie = mMovieDAO.findMovie(5); // This record is created via the DatabaseSeed
        assertEquals("Checking if fetching movie is successful", "Shawshank Redemption", movie.getTitle());
    }

    /**
     * Tests if a record is saved in the database
     * @throws Exception
     */
    public void testSave() throws Exception {
        Movie movie = new Movie("Rocky");
        long rowId = mMovieDAO.saveMovie(movie);
        assertFalse(rowId == -1);
    }

    /**
     * Tests if we can create a record and then update it
     * @throws Exception
     */
    public void testUpdate() throws Exception {
        Movie movie = new Movie("2001: A Space Odyssey");
        long movieId = mMovieDAO.saveMovie(movie);

        // We assert that the movie was saved and was given a unique ID;
        assertFalse(movieId == -1);

        movie.setTitle("2001");
        mMovieDAO.saveMovie(movie);

        // We now look in our database for the record saved
        Movie movieFetched = mMovieDAO.findMovie(movieId);

        // Check if the movie has the new updated title
        assertEquals(movieFetched.getTitle(), "2001");
    }

    /**
     * Tests if we can create a movie in the database and then find it by searching for it.
     * @throws Exception
     */
    public void testSearch() throws Exception {
        Movie movie = new Movie("Pulp Fiction");
        long id = mMovieDAO.saveMovie(movie);

        List<Movie> results = mMovieDAO.searchMovieBy("title", "Pulp");

        assertEquals(results.size(), 1);

        Movie foundMovie = results.get(0);
        assertEquals(id, foundMovie.getId());
    }

    public void testDelete() throws Exception {
        Movie movie = new Movie("Reservoir Dogs");
        long id = mMovieDAO.saveMovie(movie);
        mMovieDAO.deleteMovie(movie);

        try {
            Movie foundMovie = mMovieDAO.findMovie(id);
            Assert.fail("Throw record not found exception");
        } catch (DatabaseRecordNotFoundException e) {
            assertTrue(true); // success!
        }

    }

}