package com.typeof.flickpicker.database;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;

import junit.framework.Assert;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */
public class RatingDAOTest extends AndroidTestCase{

    private RatingDAO mSQLRatingDAO;
    private MovieDAO mSQLMovieDAO;
    private Database mDatabase;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSQLRatingDAO = App.getRatingDAO();
        mSQLMovieDAO = App.getMovieDAO();
        mDatabase = App.getDatabaseSeed();
        mDatabase.setUpTables();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabase.dropTables();
    }

    public void testGetMovieRatings(){

        //Process:
        // create two different ratings with the same movie id
        // save those two ratings and save their corresponding ids for comparison later on
        // find those two ratings by searching for the specific movieId
        //compare the aquired list of ratings ids with the ones created earlier on to verify the method works as expected

        //two unique ids, from two different users but points to the same movie

        long id = mSQLMovieDAO.saveMovie(new Movie("Gone with the wind", 1936));

        //different users rate the same movie with differen ratings
        long ratingOneId = mSQLRatingDAO.saveRating(new Rating(3, id, 5));
        long ratingTwoId = mSQLRatingDAO.saveRating(new Rating(4, id, 4));

        List<Rating> ratingsToCompare = mSQLRatingDAO.getMovieRatings(id);
        int size = ratingsToCompare.size();
        //compares the size of the aquired array to the expected value:
        assertEquals(2, ratingsToCompare.size());

    }

    public void testSaveRating(){

        //process:
        //save rating and its corresponding id
        //call findRating() and save that rating object
        //compare that ratings id of that object to the rating created in the beginning to verify that the object has been saved

        long id = mSQLMovieDAO.saveMovie(new Movie("Gone with the wind", 1936));
        long rateId = mSQLRatingDAO.saveRating(new Rating(2,id,2));
        Rating fetchedRating = mSQLRatingDAO.findRating(rateId);

        assertEquals(rateId, fetchedRating.getId() );

        long rateIdUpdated = mSQLRatingDAO.saveRating(fetchedRating);
        Rating fetchedUpdatedRating = mSQLRatingDAO.findRating(rateIdUpdated);
        assertEquals(fetchedRating.getId(),fetchedUpdatedRating.getId());
        assertEquals(fetchedUpdatedRating.getMovieId(),fetchedRating.getMovieId());

        //NOTE: need to test if saveRating can detemine createRating() && updateRating()



    }
    public void testRemoveRating() {

        //process:
        // saves a dummy rating and confirm that it is saved
        // call deleteRating() and verify that it has been deleted

        long id = mSQLMovieDAO.saveMovie(new Movie("Gone with the wind", 1936));
        long ratingId = mSQLRatingDAO.saveRating(new Rating(4, id, 4));
        Rating ratingAfterSave = mSQLRatingDAO.findRating(ratingId);

        //confirms that the rating has been saved
        assertEquals(ratingId, ratingAfterSave.getId());

        mSQLRatingDAO.removeRating(ratingId);

        try {
            Rating foundRating = mSQLRatingDAO.findRating(ratingId);
            Assert.fail("Throw record not found exception");
        } catch (DatabaseRecordNotFoundException e) {
            assertTrue(true); // success!
        }
    }

    public void testGetCommunityTopPicks(){


            //Process:
            //The method getCommunityTopPicks in RatingDAO shall sort the rating table and loop through
            // the table (int desiredSizeOfList) and return a list containing the movies. Theese movies need
            // to match the expected movies to confirm that the method is valid.

            //dummy-movies:
            long firstDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("A", 2016));
            long secondDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("B", 2016));
            long thirdDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("C", 2016));
            long fourthDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("D", 2016));

            //dummy ratings for those movies
            int userId = 1;
            long firstDummyRatingId = mSQLRatingDAO.saveRating(new Rating(2.0,firstDummyMovieId, userId));
            long secondDummyRatingId = mSQLRatingDAO.saveRating(new Rating(3.0,secondDummyMovieId, userId));
            long thirdDummyRatingId = mSQLRatingDAO.saveRating(new Rating(4.0,thirdDummyMovieId, userId));
            long fourthDummyRatingId = mSQLRatingDAO.saveRating(new Rating(2.0,fourthDummyMovieId, userId));

        //..............
        double communityRating = mSQLMovieDAO.findMovie(firstDummyMovieId).getCommunityRating();

        //doesnt save the communityrating, thats why confusion in what movie to retrieve??
        //-----------

        int desiredSizeOFList = 2;
        List<Movie> communityTopPicksAllTime = mSQLRatingDAO.getCommunityTopPicks(desiredSizeOFList);

        //the CURSOR ISSUE
        assertEquals(desiredSizeOFList, communityTopPicksAllTime.size());
        assertEquals("C", communityTopPicksAllTime.get(0).getTitle());
        assertEquals("B", communityTopPicksAllTime.get(1).getTitle());
    }


    public void testGetMostDislikedMovies(){

        //Process:
        //The method getCommunityTopPicks in RatingDAO shall sort the rating table and loop through
        // the table (int desiredSizeOfList) and return a list containing the movies. Theese movies need
        // to match the expected movies to confirm that the method is valid.

        //dummy-movies:
        long firstDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("A", 2016));
        long secondDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("B", 2016));
        long thirdDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("C", 2016));
        long fourthDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("D", 2016));

        //dummy ratings for those movies
        int userId = 1;
        long firstDummyRatingId = mSQLRatingDAO.saveRating(new Rating(1,firstDummyMovieId, userId));
        long secondDummyRatingId = mSQLRatingDAO.saveRating(new Rating(2,secondDummyMovieId, userId));
        long thirdDummyRatingId = mSQLRatingDAO.saveRating(new Rating(1,thirdDummyMovieId, userId));
        long fourthDummyRatingId = mSQLRatingDAO.saveRating(new Rating(3,fourthDummyMovieId, userId));

        int desiredSizeOFList = 3;
        List<Movie> communityMostDislikedMovies = mSQLRatingDAO.getMostDislikedMovies(desiredSizeOFList);

        assertEquals(desiredSizeOFList,communityMostDislikedMovies.size());

        //...now that we have the lists' size check out, we need to make sure that the correct list has been given to us

        //assertEquals("A",communityMostDislikedMovies.get(0).getTitle());
        //assertEquals("A",communityMostDislikedMovies.get(1).getTitle());
        //assertEquals("C",communityMostDislikedMovies.get(1).getTitle());


    }

    public void testGetTopRecommendedMoviesThisYear(){

        //dummy-movies:
        long firstDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("A", 2016));
        long secondDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("B", 2016));
        long thirdDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("C", 2015));
        long fourthDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("D", 2016));
        long fifthDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("E", 2016));
        long sixthDummyMovieId = mSQLMovieDAO.saveMovie(new Movie("F", 2016));

        Movie testMovie = mSQLMovieDAO.findMovie(firstDummyMovieId);
        assertEquals(2016, testMovie.getYear());

        //dummy ratings for those movies
        int userId = 1;
        long firstDummyRatingId = mSQLRatingDAO.saveRating(new Rating(3,firstDummyMovieId, userId));
        long secondDummyRatingId = mSQLRatingDAO.saveRating(new Rating(3,secondDummyMovieId, userId));
        long thirdDummyRatingId = mSQLRatingDAO.saveRating(new Rating(4,thirdDummyMovieId, userId));
        long fourthDummyRatingId = mSQLRatingDAO.saveRating(new Rating(5,fourthDummyMovieId, userId));

        int desiredSizeOFList = 5;
        List<Movie> topRecommendedThisYear = mSQLRatingDAO.getTopRecommendedMoviesThisYear(desiredSizeOFList, 2016);

        assertEquals(desiredSizeOFList,topRecommendedThisYear.size());
        assertEquals("D", topRecommendedThisYear.get(0).getTitle());

    }

}