package com.typeof.flickpicker.database;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.sql.SQLRatingDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */
public class RatingDAOTest extends AndroidTestCase {

    private SQLRatingDAO mSQLRatingDAO;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSQLRatingDAO = new SQLRatingDAO(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mDatabaseSeed.seedDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabaseSeed.clearDatabase();
    }

    public void testgetMovieRatings(){

        //Process:
        // create two different ratings with the same movie id
        // save those two ratings and save their corresponding ids for comparison later on
        // find those two ratings by searching for the specific movieId
        //compare the aquired list of ratings ids with the ones created earlier on to verify the method works as expected

        //two unique ids, from two different users but points to the same movie
        long ratingOneId = mSQLRatingDAO.saveRating(3,5,5);
        long ratingTwoId = mSQLRatingDAO.saveRating(4,5,4);

        List<Rating> ratingsToCompare = mSQLRatingDAO.getMovieRatings(5);
        int size = ratingsToCompare.size();
        //compares the size of the auired array to the expected value:
        assertEquals(2, ratingsToCompare.size());

        //finally verifies the ids:
        assertEquals(ratingOneId,ratingsToCompare.get(0).getId());
        assertEquals(ratingTwoId,ratingsToCompare.get(1).getId());

    }

    public void testSaveRating(){

        //process:
        //save rating and its corresponding id
        //call createRatingFromCursor() and save that rating object
        //compare that ratings id to the rating created in the beginning to verify that the object has been saved

        long rateId = mSQLRatingDAO.saveRating(2,2,2);
        Cursor c = mSQLRatingDAO.findRating(rateId);
        Rating fetchedRating = mSQLRatingDAO.createRatingFromCursor(c);

        assertEquals(rateId, fetchedRating.getId() );


    }
    public void testRemoveRating(){

        //process:
        // saves a dummy rating and confirm that it is saved
        // call deleteRating() and verify that the returned value (value of the cursor for that id) matches the expected value 0

        long ratingId = mSQLRatingDAO.saveRating(4,4,4);
        Cursor cursor = mSQLRatingDAO.findRating(ratingId);
        int CursorCountAfterSave = cursor.getCount();

        //confirms that its count equals 1 after save
        assertEquals(1, CursorCountAfterSave);

        int CursorCountAfterDelete = mSQLRatingDAO.removeRating(ratingId);

        //confirms that its count equals 0 after delete
        assertEquals(0, CursorCountAfterDelete);

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


}