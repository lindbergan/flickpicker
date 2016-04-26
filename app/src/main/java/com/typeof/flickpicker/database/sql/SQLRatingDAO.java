package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */

public class SQLRatingDAO extends SQLDAO {

    public SQLRatingDAO(Context ctx) {
        super(ctx);
    }

    public List<Rating> getMovieRatings(long movieId){

    }

    public long saveRating(double rating, int movieId, int userId) {
        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, movieId);
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, userId);

        return super.save(new Rating(rating,movieId,userId), "ratings", values);
    }

    public Cursor searchRatingBy(String column, String searchString){
        return super.search("ratings",column, searchString);
    }

    public Rating createRatingFromCursor(Cursor c) {

        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
        double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        int movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        int userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));

        Rating createdRating = new Rating(rating,movieId,userId);
        createdRating.setId(id);
        return createdRating;
    }

    public Cursor findRating(long id){
        return super.find(id,"ratings");
    }

    public int removeRating(long id){
        Cursor c = findRating(id);
        Rating ratingToDelete = createRatingFromCursor(c);

        return super.delete(ratingToDelete,"ratings");
    }

    /*
    public Rating find(long id) {
        try {
            Cursor c = super.find(id, "ratings");
            Rating rating = createRatingCursor(c);
            c.close();
            return rating;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }

    public Rating createRatingCursor(Cursor c) {
        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        Double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        int movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        int userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_USERID));
        Rating r = new Rating(rating, movieId,userId);
        r.setId(id);
        return r;
    }

    public long save(Rating rating) {
        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating.getRating());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, rating.getMovieId());
        values.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, rating.getUserId());

        return super.save(rating, "ratings", values);
    }

    /*
    public void update(Rating rating, ContentValues values) {
        super.update(rating, values);
    }
    */

    /*

    public void update(Rating rating, ContentValues values) {
        super.update(rating, values, "ratings");
    }

    public long delete(Rating rating) {
        super.delete(rating, "ratings");
        return rating.getId();
    }

    /*
    public List<Rating> getRatingFromUser(){

    }

    public List<Rating> getRatingFromMovie(){

    }
    */

}
