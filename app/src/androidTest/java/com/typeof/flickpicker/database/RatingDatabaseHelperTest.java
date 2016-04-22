package com.typeof.flickpicker.database;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */
public class RatingDatabaseHelperTest extends AndroidTestCase {

    private RatingDatabaseHelper mRatingDatabaseHelper;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRatingDatabaseHelper = new RatingDatabaseHelper(getContext());
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
        Rating rating = mRatingDatabaseHelper.find(5); // This record is created via the DatabaseSeed
        assertEquals("Checking if fetching rating is successful", 4.0, rating.getRating());
    }

    /**
     * Tests if a record is saved in the database
     * @throws Exception
     */
    public void testSave() throws Exception {
        Rating rating = new Rating(1.0,1,1); //int id, double rating, int movieId, int userId
        long rowId = mRatingDatabaseHelper.save(rating);
        assertFalse(rowId == -1);
    }

    /**
     * Tests if we can create a record and then update it
     * @throws Exception
     */
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

}