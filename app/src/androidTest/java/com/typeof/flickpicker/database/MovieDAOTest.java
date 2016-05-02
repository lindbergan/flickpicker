package com.typeof.flickpicker.database;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.sql.SQLFriendDAO;
import com.typeof.flickpicker.database.sql.SQLMovieDAO;
import com.typeof.flickpicker.database.sql.SQLRatingDAO;
import com.typeof.flickpicker.database.sql.SQLUserDAO;

import junit.framework.Assert;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieDAOTest extends AndroidTestCase {

    private MovieDAO mMovieDAO;
    private RatingDAO mRatingDAO;
    private DatabaseSeed mDatabaseSeed;
    private UserDAO mUserDAO;
    private FriendDAO mFriendDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMovieDAO = new SQLMovieDAO(getContext());
        mRatingDAO = new SQLRatingDAO(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mUserDAO = new SQLUserDAO(getContext());
        mFriendDAO = new SQLFriendDAO(getContext());
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
        long movieId = mMovieDAO.saveMovie(new Movie("Shawshank", 1994));
        Movie movie = mMovieDAO.findMovie(movieId); // This record is created via the DatabaseSeed
        assertEquals("Checking if fetching movie is successful", "Shawshank", movie.getTitle());
    }

    /**
     * Tests if a record is saved in the database
     * @throws Exception
     */


    public void testSave() throws Exception {
        Movie movie = new Movie("Rocky", 1976);
        long rowId = mMovieDAO.saveMovie(movie);
        assertFalse(rowId == -1);
    }

    /**
     * Tests if we can create a record and then update it
     * @throws Exception
     */

    public void testUpdate() throws Exception {
        Movie movie = new Movie("2001: A Space Odyssey", 1968);
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
        Movie movie = new Movie("Pulp Fiction", 1994);
        long id = mMovieDAO.saveMovie(movie);

        List<Movie> results = mMovieDAO.searchMovieBy("title", "Pulp");

        assertEquals(results.size(), 1);

        Movie foundMovie = results.get(0);
        assertEquals(id, foundMovie.getId());
    }

    /**
     * Test deletion of a movie
     * Creates a movie, saves the movie
     * Then deletes the movie
     * Checks if the movie still exists in the database, expects it to fail
     * @throws Exception
     */

    public void testDelete() throws Exception {
        Movie movie = new Movie("Reservoir Dogs", 1992);
        long id = mMovieDAO.saveMovie(movie);
        mMovieDAO.deleteMovie(movie);

        try {
            Movie foundMovie = mMovieDAO.findMovie(id);
            Assert.fail("Throw record not found exception");
        } catch (DatabaseRecordNotFoundException e) {
            assertTrue(true); // success!
        }

    }

    /**
     * Testing how many of my friends has seen this movie
     * Creates a movie
     * Creates two users and a relation between user1 and user2
     * User2 rates the movie in question
     * @throws Exception
     */

    public void testNumOfFriendsHasSeenMovie() throws Exception {
        Movie movie = new Movie("Reservoir Dogs", 1992);
        long id = mMovieDAO.saveMovie(movie);

        User user = new User("pelle", "password");
        User user2 = new User("johan", "password");
        mUserDAO.saveUser(user);
        mUserDAO.saveUser(user2);

        Friend f = new Friend(user.getId(), user2.getId());
        mFriendDAO.addFriend(f);

        Rating rating = new Rating(5.0, id, user2.getId());
        mRatingDAO.saveRating(rating);

        int nrOfFriends = mMovieDAO.numOfFriendsHasSeenMovie(id, user.getId());
        assertEquals(1, nrOfFriends);

    }

}