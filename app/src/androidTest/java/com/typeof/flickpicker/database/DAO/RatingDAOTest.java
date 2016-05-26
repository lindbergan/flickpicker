package com.typeof.flickpicker.database.DAO;

import com.typeof.flickpicker.BaseTest;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import junit.framework.Assert;
import java.util.List;

/**
 * RatingDAOTest
 *
 * A test class for testing the implementation of the RatingDAO interface methods.
 */

public class RatingDAOTest extends BaseTest {

    private RatingDAO mRatingDAO;
    private MovieDAO mMovieDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRatingDAO = App.getRatingDAO();
        mMovieDAO = App.getMovieDAO();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests getMovieRatings()
     *
     * Creates a movie and saves it
     * Creates two rating for that movie by two different users and saves them
     * Fetches all ratings of the new ly created movie in a list by calling getMovieRatings() with the newly created movie as parameter
     * Asserts that the fetched list has the size of two
     * @throws Exception
     */
    public void testGetMovieRatings() throws Exception{

        long GoneWithTheWindId = mMovieDAO.saveMovie(new Movie("Gone with the wind", 1939));

        mRatingDAO.saveRating(new Rating(3, GoneWithTheWindId, 5));
        mRatingDAO.saveRating(new Rating(4, GoneWithTheWindId, 4));

        List<Rating> ratingList = mRatingDAO.getMovieRatings(GoneWithTheWindId);

        assertEquals(2, ratingList.size());
    }

    /**
     * Tests saveRating()
     *
     * Creates a movie and saves it
     * Creates a rating of the newly created movie and saves it
     * Fetches that rating by calling findRating() with the newly created rating's id as parameter
     * Asserts that the newly created rating and the fetched on has the same id
     * Updates the rating and saves it
     * Asserts that the updated rating and the old rating has the same ids, only that the rating value has changed
     * @throws Exception
     */
    public void testSaveRating() throws Exception{

        long movieId = mMovieDAO.saveMovie(new Movie("Gone with the wind", 1939));
        long ratingId = mRatingDAO.saveRating(new Rating(2,movieId,2));

        Rating rating = mRatingDAO.findRating(ratingId);
        assertEquals(ratingId, rating.getId() );

        rating.updateRating(3.2);
        long updatedRatingId = mRatingDAO.saveRating(rating);
        assertEquals(rating.getId(), updatedRatingId);
        }

    /**
     * Tests update()
     *
     * Creates a new movie and rating and saves them
     * updates the value of the rating for that rating object ans saves it
     * Fetches the rating
     * Asserts that the fetched rating's rating value corresponds to the expected value
     * @throws Exception
     */
    public void testUpdate() throws Exception{

        long movieId = mMovieDAO.saveMovie(new Movie("The Running Man", 1991));
        long currentUsersId = App.getCurrentUser().getId();

        Rating testRating = new Rating(3.0,movieId,currentUsersId);
        long ratingId = mRatingDAO.saveRating(testRating);

        testRating.updateRating(5.0);
        mRatingDAO.saveRating(testRating);

        Rating fetchedRating = mRatingDAO.findRating(ratingId);

        assertEquals(5.0,fetchedRating.getRating());
    }

    /**
     * Tests saveToMovieTable()
     *
     * Creates a new movie and rating for that movie and saves them
     * Asserts that the communityRating has updated correctly and takes the value 4.0
     * Updates the rating to a new value - 1.0 - and saves it
     * Asserts that the communityRating has updated accordingly and now is 1.0
     * Finally creates a new rating for the same movie with rating value 3.0 and saves it
     * Asserts that the communityRating has updated accordingly and now is (1.0 + 3.0)/2 = 2
     * @throws Exception
     */
    public void testSaveToMovieTable() throws Exception{

        // saveRating() also saves information about the movies new rating to the MovieTable.
        // This needs to be checked so that the method works as supposed to.

        long PrinceOfThievesId = mMovieDAO.saveMovie(new Movie("Prince of Thieves", 1991));
        Rating r1 = new Rating(4.0, PrinceOfThievesId, 1 );
        long firstRating = mRatingDAO.saveRating(r1);
        assertEquals(4.0, mMovieDAO.findMovie(PrinceOfThievesId).getCommunityRating());

        //then update that rating and check its new value. (same user)
        Rating PrinceOfThievesRating = mRatingDAO.findRating(firstRating);
        PrinceOfThievesRating.updateRating(1.0);
        mRatingDAO.saveRating(PrinceOfThievesRating);
        assertEquals(1.0, mMovieDAO.findMovie(PrinceOfThievesId).getCommunityRating());

        //...if another user saves a rating for the same movie.
        Rating r2 = new Rating(3.0, PrinceOfThievesId, 2 );
        mRatingDAO.saveRating(r2);
        assertEquals(2.0, mMovieDAO.findMovie(PrinceOfThievesId).getCommunityRating());
    }

    /**
     * Tests removeRating()
     *
     * Creates a movie and a rating for that movie and saves them
     * Confirms that the rating has been saved by calling findRating() with the newly created rating's id as parameter
     * Asserts that the newly created rating's id and the fetched rating's id match
     * Calls removeRating()
     * Asserts that an exception of type DatabaseRecordNotFoundException will be thrown when an id doesn't exist in the database
     * @throws Exception
     */
    public void testRemoveRating() throws Exception {

        long movieId = mMovieDAO.saveMovie(new Movie("Gone with the wind", 1939));
        long ratingId = mRatingDAO.saveRating(new Rating(4, movieId, 4));

        Rating fetchedRating = mRatingDAO.findRating(ratingId);
        assertEquals(ratingId, fetchedRating.getId());

        mRatingDAO.removeRating(fetchedRating);

        try {
            mRatingDAO.findRating(ratingId);
            Assert.fail("Throw record not found exception");
        } catch (DatabaseRecordNotFoundException e) {
            assertTrue(true); // success!
        }
    }

    /**
     * Tests findRating()
     *
     * Creates a movie and a rating for that movie and saves them
     * fetches that rating by calling find() with the ratings id as parameter
     * Asserts that the fetched rating's value corresponds to the newly created rating's
     * @throws Exception
     */
    public void testFindRating() throws Exception{

        long movieId = mMovieDAO.saveMovie(new Movie("The Pelican Brief", 1993));
        long userId = App.getCurrentUser().getId();

        Rating testRating = new Rating(4.2, movieId,userId);
        long ratingID = mRatingDAO.saveRating(testRating);

        Rating fetchedRating = mRatingDAO.findRating(ratingID);
        assertEquals(4.2, fetchedRating.getRating());
    }

    /**
     * Tests getRatingFromUser()
     *
     * Creates a new user, movie and rating of the movie by that user and saves them
     * Asserts that a call to getRatingFromUser() with the newly created parameters should return 3.0
     * (that is the rating value of the rating in question)
     * @throws Exception
     */
    public void testGetRatingFromUser() throws Exception {

        User u = new User("testUser", "testPassword");
        App.getUserDAO().saveUser(u);

        Movie m = new Movie("testMovie", 1990);
        mMovieDAO.saveMovie(m);

        Rating r = new Rating(3.0, m.getId(), u.getId());
        mRatingDAO.saveRating(r);

        assertEquals(mRatingDAO.getRatingFromUser(u.getId(), m.getId()), 3.0);
    }

    /**
     * Tests getAllRatingsFromUser()
     *
     * Creates and saves a user
     * Creates and saves three movies
     * Creates three ratings of those movies by that user and save those ratings
     * Asserts that a call to getAllRatingsFromUser().size() with the newly created user as a parameter should equal 3
     * @throws Exception
     */
    public void testGetAllRatingsFromUser() throws Exception {
        User u = new User("testUser", "testPassword");
        App.getUserDAO().saveUser(u);

        Movie m = new Movie("testMovie", 1990);
        mMovieDAO.saveMovie(m);

        Movie m1 = new Movie("testMovie", 1991);
        mMovieDAO.saveMovie(m1);

        Movie m2 = new Movie("testMovie", 1992);
        mMovieDAO.saveMovie(m2);

        mRatingDAO.saveRating(new Rating(1.0, m.getId(), u.getId()));
        mRatingDAO.saveRating(new Rating(2.0, m1.getId(), u.getId()));
        mRatingDAO.saveRating(new Rating(3.0, m2.getId(), u.getId()));

        assertEquals(3, mRatingDAO.getAllRatingsFromUser(u.getId()).size());
    }
}
