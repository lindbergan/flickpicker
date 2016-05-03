package com.typeof.flickpicker.database;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.activities.App;
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

        //save movie
        long GoneWithTheWindId = mSQLMovieDAO.saveMovie(new Movie("Gone with the wind", 1939));

        //two users rate that movie but with different ratings
        long ratingOneId = mSQLRatingDAO.saveRating(new Rating(3, GoneWithTheWindId, 5));
        long ratingTwoId = mSQLRatingDAO.saveRating(new Rating(4, GoneWithTheWindId, 4));

        //save all ratings for specified movie
        List<Rating> ratingList = mSQLRatingDAO.getMovieRatings(GoneWithTheWindId);

        //compares the size of the list to the expected value:
        assertEquals(2, ratingList.size());

    }

    public void testSaveRating(){

        //save a movie and a rating to the database
        long movieId = mSQLMovieDAO.saveMovie(new Movie("Gone with the wind", 1939));
        long rateId = mSQLRatingDAO.saveRating(new Rating(2,movieId,2));

        // create a rating object by calling findRating() with the same id as above. compare the
        // fetched rating objects id to the one in the database. If successful, this confirms that
        // findRating() works as it should as well as the fact that the rating has been saved to
        // the database.

        Rating rating = mSQLRatingDAO.findRating(rateId);
        assertEquals(rateId, rating.getId() );

        //test to make sure that a rating does not get a new id when updated.
        rating.updateRating(3.2);
        long updatedRatingId = mSQLRatingDAO.saveRating(rating);
        assertEquals(rating.getId(), updatedRatingId);

        }

    public void testSaveToMovieTable(){

        //Finally - saveRating() also saves information about the movies new rating to the
        // MovieTable. This needs to be checked so that the method works as supposed to.

        // create a new movie and a rating for that movie. Confirm that the movies communityRating
        // is the correct after a first rating has been given to that movie.
        long PrinceOfThievesId = mSQLMovieDAO.saveMovie(new Movie("Prince of Thieves", 1991));
        Rating r1 = new Rating(4.0, PrinceOfThievesId, 1 );
        long firstRating = mSQLRatingDAO.saveRating(r1);
        assertEquals(4.0, mSQLMovieDAO.findMovie(PrinceOfThievesId).getCommunityRating());


        //then update that rating and check its new value. (same user)
        Rating PrinceOfThievesRating = mSQLRatingDAO.findRating(firstRating);
        PrinceOfThievesRating.updateRating(1.0);
        mSQLRatingDAO.saveRating(PrinceOfThievesRating);
        assertEquals(1.0, mSQLMovieDAO.findMovie(PrinceOfThievesId).getCommunityRating());


        //...if another user saves a rating for the same movie.
        Rating r2 = new Rating(3.0, PrinceOfThievesId, 2 );
        long secondRating = mSQLRatingDAO.saveRating(r2);
        assertEquals(2.0, mSQLMovieDAO.findMovie(PrinceOfThievesId).getCommunityRating());

    }


    public void testRemoveRating() {

        //save a movie and a rating for that movie to the database
        long movieId = mSQLMovieDAO.saveMovie(new Movie("Gone with the wind", 1939));
        long ratingId = mSQLRatingDAO.saveRating(new Rating(4, movieId, 4));

        //create an instance of that rating
        Rating ratingAfterSave = mSQLRatingDAO.findRating(ratingId);

        //compare created instans' id to the one in the database - that is, confirm that it exists.
        assertEquals(ratingId, ratingAfterSave.getId());

        //remove that rating and make sure that no such record is found.
        mSQLRatingDAO.removeRating(ratingId);

        try {
            Rating foundRating = mSQLRatingDAO.findRating(ratingId);
            Assert.fail("Throw record not found exception");
        } catch (DatabaseRecordNotFoundException e) {
            assertTrue(true); // success!
        }
    }

}
