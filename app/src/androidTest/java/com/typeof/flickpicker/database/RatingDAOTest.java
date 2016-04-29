package com.typeof.flickpicker.database;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.sql.SQLMovieDAO;
import com.typeof.flickpicker.database.sql.SQLRatingDAO;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */
public class RatingDAOTest extends AndroidTestCase{

    private SQLRatingDAO mSQLRatingDAO;
    private SQLMovieDAO mSQLMovieDAO;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSQLRatingDAO = new SQLRatingDAO(getContext());
        mSQLMovieDAO = new SQLMovieDAO(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mDatabaseSeed.seedDatabase();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabaseSeed.clearDatabase();
    }

    public void testGetMovieRatings(){

        //Process:
        // create two different ratings with the same movie id
        // save those two ratings and save their corresponding ids for comparison later on
        // find those two ratings by searching for the specific movieId
        //compare the aquired list of ratings ids with the ones created earlier on to verify the method works as expected

        //two unique ids, from two different users but points to the same movie

        long id = mSQLMovieDAO.saveMovie(new Movie("Gone with the wind", 1936));

        long ratingOneId = mSQLRatingDAO.saveRating(3,id,5);
        long ratingTwoId = mSQLRatingDAO.saveRating(4,id,4);

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
        long rateId = mSQLRatingDAO.saveRating(2,id,2);
        Rating fetchedRating = mSQLRatingDAO.findRating(rateId);

        assertEquals(rateId, fetchedRating.getId() );

        long rateIdUpdated = mSQLRatingDAO.saveRating(3,id,2);
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
        long ratingId = mSQLRatingDAO.saveRating(4, id, 4);
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
            long firstDummyRatingId = mSQLRatingDAO.saveRating(4,firstDummyMovieId, userId);
            long secondDummyRatingId = mSQLRatingDAO.saveRating(5,secondDummyMovieId, userId);
            long thirdDummyRatingId = mSQLRatingDAO.saveRating(5,thirdDummyMovieId, userId);
            long fourthDummyRatingId = mSQLRatingDAO.saveRating(4,fourthDummyMovieId, userId);

            int desiredSizeOFList = 100;
            List<Movie> communityTopPicksAllTime = mSQLRatingDAO.getCommunityTopPicks(desiredSizeOFList);

            assertEquals(desiredSizeOFList,communityTopPicksAllTime.size());

            //...now that we have the lists' size check out, we need to make sure that the correct list has been given to us

            assertEquals("B",communityTopPicksAllTime.get(0).getTitle());
            assertEquals("C",communityTopPicksAllTime.get(1).getTitle());
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
        long firstDummyRatingId = mSQLRatingDAO.saveRating(1,firstDummyMovieId, userId);
        long secondDummyRatingId = mSQLRatingDAO.saveRating(2,secondDummyMovieId, userId);
        long thirdDummyRatingId = mSQLRatingDAO.saveRating(1,thirdDummyMovieId, userId);
        long fourthDummyRatingId = mSQLRatingDAO.saveRating(3,fourthDummyMovieId, userId);

        int desiredSizeOFList = 100;
        List<Movie> communityMostDislikedMovies = mSQLRatingDAO.getMostDislikedMovies(desiredSizeOFList);

        assertEquals(desiredSizeOFList,communityMostDislikedMovies.size());

        //...now that we have the lists' size check out, we need to make sure that the correct list has been given to us

        assertEquals("A",communityMostDislikedMovies.get(0).getTitle());
        assertEquals("C",communityMostDislikedMovies.get(1).getTitle());

    }

    public void testGetTopRecommendedMoviesThisYear(){


    }

}



    //--------OLD DESIGN---------------------------

    /**
     * Tests if we can find a record in the database
     * @throws Exception
     */

    /*
    public void testFind() throws Exception {
        Rating rating = mSQLRatingDAO.find(5); // This record is created via the DatabaseSeed
        assertEquals("Checking if fetching rating is successful", 4.0, rating.getRating());
    }
    */

    /**
     * Tests if a record is saved in the database
     * @throws Exception
     */

    /*
    public void testSave() throws Exception {
        Rating rating = new Rating(1.0,1,1); //int id, double rating, int movieId, int userId
        long rowId = mSQLRatingDAO.save(rating);
        assertFalse(rowId == -1);
    }

    /**
     * Tests if we can create a record and then update it
     * @throws Exception
     */
    /*
    public void testUpdate() throws Exception {
        Rating rating = new Rating(2.0,2,2);
        long ratingId = mSQLRatingDAO.save(rating);

        // We assert that the rating was saved and was given a unique ID;
        assertFalse(ratingId == -1);

        rating.updateRating(5.0);
        mSQLRatingDAO.save(rating);

        // We now look in our database for the record saved
        Rating ratingFetched = mSQLRatingDAO.find(ratingId);

        // Check if the rating has the new updated rating
        assertEquals(ratingFetched.getRating(), 5.0);
    }

    public void testDelete() throws Exception {
        Rating rating = new Rating(4.0,4,4);
        long id = mSQLRatingDAO.save(rating);

        mSQLRatingDAO.delete(rating);

        try {
            Rating foundRating = mSQLRatingDAO.find(id);
        }
        catch(DatabaseRecordNotFoundException e){
            //catch exception - test successful
        }

    }
    */


