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

    private SQLRatingDAO mRatingDatabaseHelper;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRatingDatabaseHelper = new SQLRatingDAO(getContext());
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
        // create two different ratings with same movie id
        // save those two ratings and save their ids for comparison later on
        // find those two ratings by searching for the specific movieId
        //compare the aquired list of ratings with the ones created earlier on to verify the method

        long ratingOneId = mRatingDatabaseHelper.saveRating(3.0,5,5);
        long ratingTwoId = mRatingDatabaseHelper.saveRating(4.0,5,4);

        List<Rating> uniqeRatings = new ArrayList<>();
        Cursor tempCursor = mRatingDatabaseHelper.searchRatingBy("movieId","5");

        while(tempCursor.moveToNext()){
            uniqeRatings.add(new Rating(1,1,1));
        }

        assertEquals(2, uniqeRatings.size());


    }

    public void testSaveRating(){

        //process:
        //save rating and its corresponding id
        //call createRatingFromCursor() and save that rating object
        //compare that ratings id to the rating created in the beginning to verify that the object has been saved

        long rateId = mRatingDatabaseHelper.saveRating(2,2,2);
        Cursor c = mRatingDatabaseHelper.findRating(rateId);
        Rating fetchedRating = mRatingDatabaseHelper.createRatingFromCursor(c);

        assertEquals(rateId, fetchedRating.getId() );


    }
    public void testremoveRating(){

        //process:
        // saves a dummy rating and confirm that it is saved
        // call deleteRating() and verify that the returned value (value of the cursor for that id) matches the expected value 0

        long ratingId = mRatingDatabaseHelper.saveRating(4,4,4);
        Cursor cursor = mRatingDatabaseHelper.findRating(ratingId);
        int CursorCountAfterSave = cursor.getCount();

        //confirms that its count equals 1 after save
        assertEquals(1, CursorCountAfterSave);

        int CursorCountAfterDelete = mRatingDatabaseHelper.removeRating(ratingId);

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
        Rating rating = mRatingDatabaseHelper.find(5); // This record is created via the DatabaseSeed
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
        long rowId = mRatingDatabaseHelper.save(rating);
        assertFalse(rowId == -1);
    }

    /**
     * Tests if we can create a record and then update it
     * @throws Exception
     */
    /*
    public void testUpdate() throws Exception {
        Rating rating = new Rating(2.0,2,2);
        long ratingId = mRatingDatabaseHelper.save(rating);

        // We assert that the rating was saved and was given a unique ID;
        assertFalse(ratingId == -1);

        rating.updateRating(5.0);
        mRatingDatabaseHelper.save(rating);

        // We now look in our database for the record saved
        Rating ratingFetched = mRatingDatabaseHelper.find(ratingId);

        // Check if the rating has the new updated rating
        assertEquals(ratingFetched.getRating(), 5.0);
    }

    public void testDelete() throws Exception {
        Rating rating = new Rating(4.0,4,4);
        long id = mRatingDatabaseHelper.save(rating);

        mRatingDatabaseHelper.delete(rating);

        try {
            Rating foundRating = mRatingDatabaseHelper.find(id);
        }
        catch(DatabaseRecordNotFoundException e){
            //catch exception - test successful
        }

    }
    */


}