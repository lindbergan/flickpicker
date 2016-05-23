package com.typeof.flickpicker.database.DAO;

import com.typeof.flickpicker.BaseTest;
import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import junit.framework.Assert;
import java.util.List;

/**
 * MovieDAOTest
 *
 * A test class for testing the implementation of the MovieDAO interface methods.
 */

public class MovieDAOTest extends BaseTest {

    private MovieDAO mMovieDAO;
    private RatingDAO mRatingDAO;
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
     * Tests findMovie()
     *
     * Saves a movie
     * Fetches a movie by calling find
     * Asserts that the fetched movie's title corresponds to the one saved
     * @throws Exception
     */


    public void testFindMovie() throws Exception {
        long movieId = mMovieDAO.saveMovie(new Movie("Shawshank", 1994));
        Movie movie = mMovieDAO.findMovie(movieId);
        assertEquals("Checking if fetching movie is successful", "Shawshank", movie.getTitle());
    }

    /**
     * Test saveMovie()
     *
     * Saves a movie and corresponding fetches the column in database table (e.g. the movieId)
     * Asserts that the column number/movieId does't equal -1
     * @throws Exception
     */


    public void testSaveMovie() throws Exception {
        Movie movie = new Movie("Rocky", 1976);
        long rowId = mMovieDAO.saveMovie(movie);
        assertFalse(rowId == -1);
    }

    /**
     * Tests update()
     *
     * Creates a movie and saves it
     * Asserts that the movie has been saved an has a uniqe id
     * Changes the title of the movie
     * Fetches the movie based on id
     * Asserts that the fetched movie's title corresponds to the updated title
     * @throws Exception
     */

    public void testUpdate() throws Exception {
        Movie movie = new Movie("2001: A Space Odyssey", 1968);
        long movieId = mMovieDAO.saveMovie(movie);

        assertFalse(movieId == -1);

        movie.setTitle("2001");
        mMovieDAO.saveMovie(movie);

        Movie movieFetched = mMovieDAO.findMovie(movieId);

        assertEquals(movieFetched.getTitle(), "2001");
    }

    /**
     * Tests searchMovieBy()
     *
     * Creates a movie and saves it
     * Searches for the title of that movie in the database by calling search()
     * Saves the number of movies corresponding to the searchString in a list
     * Asserts that the size of that list equals one
     * @throws Exception
     */

    public void testSearchMovieBy() throws Exception {
        Movie movie = new Movie("Pulp Fiction", 1994);
        long id = mMovieDAO.saveMovie(movie);

        List<Movie> results = mMovieDAO.searchMovieBy("title", "Pulp");

        assertEquals(results.size(), 1);

        Movie foundMovie = results.get(0);
        assertEquals(id, foundMovie.getId());
    }

    /**
     * Tests deleteMovie()
     *
     * Creates a movie and saves it
     * calls delete() in order to delete the movie
     * Asserts that the movie isn't in the database and that the exception works as it's supposed to
     * @throws Exception
     */

    public void testDeleteMovie() throws Exception {
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
     *
     * Tests numOfFriendsHasSeenMovie()
     *
     * Creates a movie and saves it
     * Creates two users and saves them
     * Creates a friend object where user follows user2
     * Creates a rating of the newly created movie by user2 and saves it
     * Fetches number of friends that has seen the newly created movie by calling numOfFriendsHasSeenMovie()
     * Asserts that nrOfFriends equals 1
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

    /**
     * Tests getCommunityTopPicks()
     *
     * Creates six dummy movies and saves them
     * Creates six dummy ratings, one for each movie, and saves them
     * Saves all movies returned from call to getCommunityTopPicks() in a list
     * Asserts that the first movie in the list is the highest rated and the second the second highest rated movie
     * @throws Exception
     */

    public void testGetCommunityTopPicks(){

        //create a bunch of dummy data
        createDummyData();

        int desiredSizeOFList = 2;
        List<Movie> communityTopPicksAllTime = mMovieDAO.getCommunityTopPicks(desiredSizeOFList);

        assertEquals("F", communityTopPicksAllTime.get(0).getTitle());
        assertEquals("C", communityTopPicksAllTime.get(1).getTitle());
    }

    /**
     * Tests getMostDislikedMovies()
     *
     * Creates six dummy movies and saves them
     * Creates six dummy ratings, one for each movie, and saves them
     * Saves all movies returned from call to getMostDislikedMovies() in a list
     * Asserts that the first movie in the list is the lowest rated and the second the second lowest rated movie
     * @throws Exception
     */

    public void testGetMostDislikedMovies(){

        //create a bunch of dummy data
        createDummyData();

        int desiredSizeOFList = 2;
        List<Movie> mostDislikedMovies = mMovieDAO.getMostDislikedMovies(desiredSizeOFList);

        assertEquals("A", mostDislikedMovies.get(0).getTitle());
        assertEquals("D", mostDislikedMovies.get(1).getTitle());
    }

    /**
     * Tests getTopRecommendedMoviesThisYear()
     *
     * Creates six dummy movies and saves them
     * Creates six dummy ratings, one for each movie, and saves them
     * Saves all movies returned from call to getTopRecommendedMoviesThisYear() in a list
     * Asserts that the first movie in the list is the highest rated movie that specified year,
     * the second the second highest rated movie that year
     * @throws Exception
     */
    public void testGetTopRecommendedMoviesThisYear(){

        //create a bunch of dummy data
        createDummyData();

        int desiredSizeOFList = 2;
        List<Movie> topRecommendedThisYear = mMovieDAO.getTopRecommendedMoviesThisYear(desiredSizeOFList, 2016);

        assertEquals("F", topRecommendedThisYear.get(0).getTitle());
        assertEquals("B", topRecommendedThisYear.get(1).getTitle());
    }

    /**
     * Tests getFriendsSeenMovie()
     *
     * Creates movie and saves it
     * Creates three users and saves them
     * Adds user2 and user3 as friends to user
     * user2 and user3 rates the newly created movie
     * Saves a list of friends by calling getFriendsSeenMovie() with user2 and user3 as parameters
     * Asserts that the fetched list consits of two users, user2 and user3. Confirms this by comparing ids to expected values
     * @throws Exception
     */

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

    /**
     * Tests getMovieCollectionFromUserId()
     *
     * Creates a user and saves it
     * Creates three ratings by that user and saves them
     * Saves a list of movies by calling getMovieCollectionFromUserId() with the user as parameter
     * Asserts that the size of the list corresponds to the expected value - three
     */

    public void testGetMovieCollectionFromUserId() {

        long userId = mUserDAO.saveUser(new User("User", "admin"));
        long firstMovieId = mMovieDAO.saveMovie(new Movie("Oblivion", 2013));
        long secondMovieId = mMovieDAO.saveMovie(new Movie("12 Years a Slave", 2013));
        long thirdMovieId = mMovieDAO.saveMovie(new Movie("Gravity", 2013));

        mRatingDAO.saveRating(new Rating(3.7,firstMovieId,userId));
        mRatingDAO.saveRating(new Rating(4.2, secondMovieId,userId));
        mRatingDAO.saveRating(new Rating(3.4, thirdMovieId,userId));

        int desireSizeOfList = 4;
        List<Movie> usersMovieCollection = mMovieDAO.getMovieCollectionFromUserId(desireSizeOfList, userId);
        assertEquals(3, usersMovieCollection.size());
    }

    private void createDummyData() {

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
}