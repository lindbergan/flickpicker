package com.typeof.flickpicker.database.DAO;

import android.test.ApplicationTestCase;

import com.typeof.flickpicker.BaseTest;
import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import junit.framework.Assert;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieDAOTest extends BaseTest {

    private MovieDAO mMovieDAO;
    private RatingDAO mRatingDAO;
    private Database mDatabase;
    private UserDAO mUserDAO;
    private FriendDAO mFriendDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMovieDAO = App.getMovieDAO();
        mRatingDAO = App.getRatingDAO();
        mUserDAO = App.getUserDAO();
        mFriendDAO = App.getFriendDAO();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * Tests if we can find a record in the database
     * @throws Exception
     */


    public void testFind() throws Exception {
        long movieId = mMovieDAO.saveMovie(new Movie("Shawshank", 1994));
        Movie movie = mMovieDAO.findMovie(movieId);
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
            mMovieDAO.findMovie(id);
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

    public void testGetCommunityTopPicks(){

        //create a bunch of dummy data
        createDummyData();

        //specify the length of the list and confirm that the method returns a list of the
        //specified size.
        int desiredSizeOFList = 2;
        List<Movie> communityTopPicksAllTime = mMovieDAO.getCommunityTopPicks(desiredSizeOFList);
        assertEquals(desiredSizeOFList, communityTopPicksAllTime.size());

        //confirm that the list return expected elements.
        assertEquals("F", communityTopPicksAllTime.get(0).getTitle());
        assertEquals("C", communityTopPicksAllTime.get(1).getTitle());
    }

    public void testGetMostDislikedMovies(){

        //create a bunch of dummy data
        createDummyData();

        //specify the length of the list and confirm that the method returns a list of the
        //specified size.
        int desiredSizeOFList = 2;
        List<Movie> mostDislikedMovies = mMovieDAO.getMostDislikedMovies(desiredSizeOFList);
        assertEquals(desiredSizeOFList, mostDislikedMovies.size());

        //confirm that the list return expected elements.
        assertEquals("A", mostDislikedMovies.get(0).getTitle());
        assertEquals("D", mostDislikedMovies.get(1).getTitle());

    }

    public void testGetTopRecommendedMoviesThisYear(){

        //create a bunch of dummy data
        createDummyData();

        //specify the length of the list and confirm that the method returns a list of the
        //specified size.
        int desiredSizeOFList = 2;
        List<Movie> topRecommendedThisYear = mMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOFList, 2016);
        assertEquals(desiredSizeOFList,topRecommendedThisYear.size());

        //confirm that the list return expected elements.
        assertEquals("F", topRecommendedThisYear.get(0).getTitle());
        assertEquals("B", topRecommendedThisYear.get(1).getTitle());
    }

    public void createDummyData() {

        //create dummy-movies:
        long firstDummyMovieId = mMovieDAO.saveMovie(new Movie("A", 2012));
        long secondDummyMovieId = mMovieDAO.saveMovie(new Movie("B", 2016));
        long thirdDummyMovieId = mMovieDAO.saveMovie(new Movie("C", 2015));
        long fourthDummyMovieId = mMovieDAO.saveMovie(new Movie("D", 2016));
        long fifthDummyMovieId = mMovieDAO.saveMovie(new Movie("E", 2014));
        long sixthDummyMovieId = mMovieDAO.saveMovie(new Movie("F", 2016));

        //create dummy ratings for those movies:
        mRatingDAO.saveRating(new Rating(2.2, firstDummyMovieId, 1));
        mRatingDAO.saveRating(new Rating(3.1, secondDummyMovieId, 1));
        mRatingDAO.saveRating(new Rating(4.3, thirdDummyMovieId, 1));
        mRatingDAO.saveRating(new Rating(2.3, fourthDummyMovieId, 1));
        mRatingDAO.saveRating(new Rating(3.2, fifthDummyMovieId, 1));
        mRatingDAO.saveRating(new Rating(5.0, sixthDummyMovieId, 1));
    }

    public void testGetFriendsSeenMovie() throws Exception {
        Movie movie = new Movie("Reservoir Dogs", 1992);
        long id = mMovieDAO.saveMovie(movie);

        User user = new User("pelle", "password");
        User user2 = new User("johan", "password");
        User user3 = new User("niklas", "password");
        long id1 = mUserDAO.saveUser(user);
        long id2 = mUserDAO.saveUser(user2);
        long id3 = mUserDAO.saveUser(user3);

        Friend f = new Friend(id1, id2);
        mFriendDAO.addFriend(f);

        Friend f1 = new Friend(id1, id3);
        mFriendDAO.addFriend(f1);

        Rating rating = new Rating(5.0, id, id2);
        mRatingDAO.saveRating(rating);

        Rating rating1 = new Rating(4.0, id, id3);
        mRatingDAO.saveRating(rating1);

        List<User> friends = mMovieDAO.getFriendsSeenMovie(id, id1);
        assertTrue(friends.get(0).getId() == id2 && friends.get(1).getId() == id3);

    }

    public void testGetUsersMovieCollection() {

        //create a user and three movies which that user rates. Then confirm that the size of that list == 3.

        long userId = mUserDAO.saveUser(new User("Laban", "admin"));
        long firstMovieId = mMovieDAO.saveMovie(new Movie("Frost", 2013));
        long secondMovieId = mMovieDAO.saveMovie(new Movie("12 Years a Slave", 2013));
        long thirdMovieId = mMovieDAO.saveMovie(new Movie("Gravity", 2013));

        mRatingDAO.saveRating(new Rating(3.7,firstMovieId,userId));
        mRatingDAO.saveRating(new Rating(4.2, secondMovieId,userId));
        mRatingDAO.saveRating(new Rating(3.4, thirdMovieId,userId));

        int desireSizeOfList = 4;
        List<Movie> usersMovieCollection = mMovieDAO.getMovieCollectionFromUserId(desireSizeOfList, userId);
        assertEquals(3, usersMovieCollection.size());

    }
}