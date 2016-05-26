package com.typeof.flickpicker.database.sql.DAO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.sql.CoreEntityFactory;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.database.sql.tables.RatingTable;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLRatingDAO
 * Data Access Object for Ratings
 */
public class SQLRatingDAO extends SQLDAO implements RatingDAO {

    private SQLMovieDAO sqlMovieDAO;
    private SQLiteDatabase db;

    public SQLRatingDAO(Context ctx) {
        super(ctx);
        SQLiteDatabaseHelper dbhelper = SQLiteDatabaseHelper.getInstance(ctx);
        db = dbhelper.getWritableDatabase();
        sqlMovieDAO = new SQLMovieDAO(ctx);
    }

    /**
     * Returns Ratings for given moiveId
     * @param movieId - Id of movie
     * @return - List of ratings
     */
    public List<Rating> getMovieRatings(long movieId){

        List<Rating> ratingsForMovie = new ArrayList<>();
        Cursor movieIdCursor = searchRatingBy("movieId", String.valueOf(movieId));
        movieIdCursor.moveToFirst();

        for(int i = 0; i < movieIdCursor.getCount(); i++){
            ratingsForMovie.add(CoreEntityFactory.createRatingFromCursor(movieIdCursor));
            movieIdCursor.moveToNext();
        }

        movieIdCursor.close();
        return ratingsForMovie;

    }

    /**
     * Saves Rating instance to database
     * @param rating - Rating instace
     * @return - database record id
     */
    public long saveRating(Rating rating){

        //Check if previous rating exists
            String query = "SELECT * FROM ratings " +
                    " WHERE " + RatingTable.RatingEntry.COLUMN_NAME_MOVIEID + " = " +
                    String.valueOf(rating.getMovieId()) + " AND " +
                    RatingTable.RatingEntry.COLUMN_NAME_USERID + " = " +
                    String.valueOf(rating.getUserId());

            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();

        if(c.getCount() != 0) { //that is - rating for that movie by that user already exists
                Rating r = CoreEntityFactory.createRatingFromCursor(c);
                removeRating(r);
                c.close();
                double oldRatingValue = r.getRating();
                setMovieTableRating(rating.getMovieId(), oldRatingValue, rating.getRating());
        } else {
            c.close();
            double oldValue = 0;
            setMovieTableRating(rating.getMovieId(), oldValue, rating.getRating());
        }

        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating.getRating());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, rating.getMovieId());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, rating.getUserId());

        return super.save(rating, "ratings", values);
    }

    /**
     * Set the movies rating in the data base
     * @param movieId - Id of movie
     * @param oldRating - the previous rating
     * @param newRating - the new rating
     * @return - returns the new rating.
     */
    private double setMovieTableRating(long movieId, double oldRating, double newRating){

        Movie movie = sqlMovieDAO.findMovie(movieId);
        double newCommunityRating = calculateCommunityRating(movie,oldRating, newRating);
        movie.setCommunityRating(newCommunityRating);

        if(oldRating == 0){
            int oldNumberOfVotes = movie.getNumberOfVotes();
            int newNumberOfVotes = oldNumberOfVotes +1;
            movie.setNumberOfVotes(newNumberOfVotes);
        }

        sqlMovieDAO.saveMovie(movie);
        return newCommunityRating;
    }


    /**
     * Calculates the community rating
     * @param movie - Movie entity
     * @param oldRating - the old rating
     * @param newRating - new rating
     * @return - returns the new rating
     */
    public double calculateCommunityRating(Movie movie, double oldRating, double newRating){

        //expression for calculating new communityRating
        if (oldRating != 0){ // that is - it exists an old rating
            return (movie.getNumberOfVotes()*movie.getCommunityRating() - oldRating + newRating) / (movie.getNumberOfVotes());
        }
        else{
            return (movie.getNumberOfVotes()*movie.getCommunityRating() + newRating) / (movie.getNumberOfVotes()+1);
        }
    }

    /**
     * Searches the ratings column
     * @param column - column we're searching in
     * @param searchString - string we're searching for
     * @return - Returns Cursor with found database record
     */
    public Cursor searchRatingBy(String column, String searchString){
        return super.search("ratings",column, searchString);
    }


    /**
     * Returns Rating instance from database
     * @param id - given Rating ID
     * @return - Rating instance
     */
    public Rating findRating(long id){
        Cursor cursor = super.find(id,"ratings");
        cursor.moveToFirst();
        Rating ratingToReturn = CoreEntityFactory.createRatingFromCursor(cursor);
        cursor.close();
        return ratingToReturn;
    }

    /**
     * Removes rating from database
     * @param rating Rating object
     * @return Number of rows affected in db
     */
    public int removeRating(Rating rating){
        return super.delete(rating, "ratings");
    }

    /**
     * Get a users rating for a movie
     * @param userId - Given user id
     * @param movieId - Given movie id
     * @return Rating value
     */
    @Override
    public double getRatingFromUser(long userId, long movieId) {

        String query = "SELECT * FROM " + RatingTable.RatingEntry.TABLE_NAME + " WHERE " +
                RatingTable.RatingEntry.TABLE_NAME + "." + RatingTable.RatingEntry.COLUMN_NAME_USERID
                + " = " + userId +  " AND " + RatingTable.RatingEntry.TABLE_NAME + "." +
                RatingTable.RatingEntry.COLUMN_NAME_MOVIEID + " = " + movieId;

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        double result = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        c.close();
        return result;

    }

    @Override
    public List<Rating> getAllRatingsFromUser(long userId) {
        String query = "SELECT * FROM " + RatingTable.RatingEntry.TABLE_NAME + " WHERE " + RatingTable.RatingEntry.COLUMN_NAME_USERID + " = ?";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(userId)});

        c.moveToFirst();
        List<Rating> result = new ArrayList<>();
        for (int i = 0; i < c.getCount(); i++) {
            result.add(CoreEntityFactory.createRatingFromCursor(c));
        }
        c.close();
        return result;
    }
}
